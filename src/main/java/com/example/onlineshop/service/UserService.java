package com.example.onlineshop.service;

import com.example.onlineshop.enums.RoleName;
import com.example.onlineshop.model.*;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.security.LoginResponse;
import com.example.onlineshop.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.onlineshop.ex_handler.ExceptionMessage.CODE_NOT_VALID;
import static com.example.onlineshop.ex_handler.ExceptionMessage.USER_NOT_FOUND;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CartService cartService;
    private final JwtUtils jwtUtils;
    private final WalletService walletService;

    private final EmailService emailService;

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
        Role role = roleService.findByName(RoleName.USER.toString());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        int verifyingCode = (int) ((Math.random() * 900000) + 100000);
        user.setVerifyingCode(verifyingCode + "");
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
        emailService.setUserService(this);
        emailService.sendVerifyingEmil(user);
        return savedUser;
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
}
