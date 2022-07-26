package com.example.onlineshop.service;

import com.example.onlineshop.model.*;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.security.LoginResponse;
import com.example.onlineshop.security.jwt.JwtUtils;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.InstanceNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.onlineshop.ex_handler.ExceptionMessage.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProductService productService;
    private final RoleService roleService;
    private final CartService cartService;
    private final JwtUtils jwtUtils;
    private final WalletService walletService;
    private final TransactionService transactionService;

    @Resource
    private final JavaMailSender javaMailSender;


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new InstanceNotFoundException(USER_NOT_FOUND));
    }

    public LoginResponse signing(String username, String password, AuthenticationManager authenticationManager) {
        UserDetails userDetails = loadUserByUsername(username);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String token = jwtUtils.generateJwtToken(userDetails);
        return new LoginResponse(token, userDetails.getAuthorities());
    }

    @SneakyThrows
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(USER_NOT_FOUND));
    }

    @SneakyThrows
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new InstanceNotFoundException(USER_NOT_FOUND));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User signup(User user) {
        Role role = roleService.findByName("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setVerifyingCode(RandomString.make(20));
        user.setEnabled(false);
        Cart cart = new Cart();
        Wallet wallet = walletService.save(new Wallet(0));
        Cart savedCart = cartService.save(cart);
        user.setCart(savedCart);
        user.setWallet(wallet);
        User savedUser = save(user);
        cart.setUser(user);
        wallet.setUser(user);
        cartService.save(savedCart);
        walletService.save(wallet);
        sendVerifyingEmil(user);
        return savedUser;
    }

    public void sendVerifyingEmil(User user) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("mohebmoallem1376@gmail.com");
        msg.setSubject("verifying emile");
        msg.setText("verifying code: \n" + user.getVerifyingCode());
        javaMailSender.send(msg);
    }

    public String verifyingCode(String verifyingCode) {
        User user = userRepository.findByVerifyingCode(verifyingCode);
        if (user == null || user.isEnabled()) {
            throw new IllegalArgumentException(CODE_NOT_VALID);
        }
        user.setVerifyingCode(null);
        user.setEnabled(true);
        save(user);
        return "Verification was successful";
    }

    public String sendForgottenPassword(String email) {
        User user = findByEmail(email);
        if (user.isEnabled()) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Forget Password");
            msg.setText("Hi  " + user.getUsername() + "\n\n your password is: \n" + user.getPassword());
            javaMailSender.send(msg);
            return "We Send Your password in " + email;
        }
        throw new IllegalArgumentException(USER_NOT_Enables);
    }

    public User delete(long id) {
        User user = findById(id);
        userRepository.delete(user);
        return user;
    }

    public Cart getCart(User user) {
        return user.getCart();
    }

    public List<Transaction> getAllTransactions(User user) {
        return user.getTransactions().stream().toList();
    }

    public Product addToCart(User user, long productId) {
        return productService.addToCart(user.getCart(), productId);
    }

    public Product removeFromCart(User user, long productId) {
        return productService.removeFromCart(user.getCart(), productId);
    }

    public Role addRole(User user, long roleId) {
        Role role = roleService.findById(roleId);
        if (user.getRoles().add(role)) {
            return role;
        }
        throw new DuplicateRequestException(DUPLICATE_ROLE);
    }

    public Integer deposit(User user, int amount) {
        return walletService.deposit(amount, user.getWallet());
    }

    public Transaction order(User user, String explains) {
        Cart userCart = user.getCart();
        List<Product> nonExist =  userCart.nonExistProducts();
        if (nonExist.size()!=0)
            throw new IllegalArgumentException(nonExist.get(0).getName()+"  Is Not Exist");
        int totalPrice = userCart.totalPrice();
        if (totalPrice == 0)
            throw new IllegalArgumentException(EMPTY_CART);
        if (totalPrice > user.getWallet().getBalance())
            throw new IllegalArgumentException(NOT_ENOUGH_BALANCE);
        productService.ReduceInventory(userCart.getProducts().keySet());
        cartService.clear(userCart);
        walletService.withdraw(totalPrice, user.getWallet());
        Transaction transaction = new Transaction(totalPrice, explains);
        transaction.setUser(user);
        return transactionService.save(transaction);
    }
}
