package com.example.onlineshop.service;

import com.example.onlineshop.model.*;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.security.LoginResponse;
import com.example.onlineshop.security.jwt.JwtUtils;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(InstanceNotFoundException::new);
    }

    public LoginResponse signing(String username, String password, AuthenticationManager authenticationManager) {
        UserDetails userDetails = loadUserByUsername(username);
        if (!userDetails.getPassword().equals(password)) {
            throw new BadCredentialsException("");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String token = jwtUtils.generateJwtToken(userDetails);
        return new LoginResponse(token);
    }

    @SneakyThrows
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(InstanceNotFoundException::new);
    }

    @SneakyThrows
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(InstanceNotFoundException::new);
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
        return savedUser;
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

    public int totalPriceOfCart(User user) {
        return user.getCart().totalPrice();
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
        throw new DuplicateRequestException();
    }

    public Integer deposit(User user, int amount) {
        return walletService.deposit(amount, user.getWallet());
    }

    public Transaction order(User user, String explains) {
        int totalPrice = user.getCart().totalPrice();
        if (totalPrice == 0)
            throw new IllegalArgumentException();
        walletService.withdraw(totalPrice, user.getWallet());
        cartService.clear(user.getCart());
        Transaction transaction = new Transaction(totalPrice, explains);
        transaction.setUser(user);
        return transactionService.save(transaction);
    }
}
