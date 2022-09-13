package com.example.onlineshop.service;

import com.example.onlineshop.enums.RoleName;
import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.Role;
import com.example.onlineshop.model.Transaction;
import com.example.onlineshop.model.User;
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

import static com.example.onlineshop.ex_handler.ExceptionMessage.*;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    private final CartService cartService;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new InstanceNotFoundException(userNotFound("username", username)));
    }

    public LoginResponse signing(String username, String password, AuthenticationManager authenticationManager) {
        UserDetails userDetails = loadUserByUsername(username);
        if (!userDetails.isEnabled()){
            throw new IllegalArgumentException(USER_NOT_ENABLES);
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String token = jwtUtils.generateJwtToken(userDetails);
        return new LoginResponse(token, userDetails.getAuthorities());
    }

    @SneakyThrows
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException(userNotFound("id", id + "")));
    }

    @SneakyThrows
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InstanceNotFoundException(userNotFound("email", email)));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public String signup(User user) {
        Role role = roleService.findByName(RoleName.USER.toString());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        int verifyingCode = (int) ((Math.random() * 900000) + 100000);
        user.setVerifyingCode(verifyingCode + "");
        user.setEnabled(false);
        user.getCart().setUser(user);
        user.getFavorite().setUser(user);
        user.getWallet().setUser(user);
        emailService.setUserService(this);
        userRepository.saveAndFlush(user);
        emailService.sendVerifyingEmil(user);
        return "signup done successfully we sent a 6 digit code for your email please verify";
    }

    public String verifyingCode(String verifyingCode) {
        User user = userRepository.findByVerifyingCode(verifyingCode);
        if (user == null || user.isEnabled()) {
            throw new IllegalArgumentException(notValidCode(verifyingCode));
        }
        user.setVerifyingCode(null);
        user.setEnabled(true);
        save(user);
        return "Verification was successful";
    }

    public User delete(long id) {
        User user = findById(id);
        cartService.delete(user.getCart());
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
