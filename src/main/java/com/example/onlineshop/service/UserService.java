package com.example.onlineshop.service;

import com.example.onlineshop.security.jwt.JwtUtils;
import com.example.onlineshop.security.LoginResponse;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.SneakyThrows;
import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.Role;
import com.example.onlineshop.model.User;
import com.example.onlineshop.repository.UserRepository;
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
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProductService productService;
    private final RoleService roleService;
    private final CartService cartService;

    private final JwtUtils jwtUtils;


    public UserService(UserRepository userRepository, ProductService productService, RoleService roleService
            , CartService cartService, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.productService = productService;
        this.roleService = roleService;
        this.cartService = cartService;
        this.jwtUtils = jwtUtils;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(InstanceNotFoundException::new);
    }

    public LoginResponse signing(String username, String password,AuthenticationManager authenticationManager) {
        UserDetails userDetails = loadUserByUsername(username);
        if (!userDetails.getPassword().equals(password)) {
            throw new BadCredentialsException("");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
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
        Cart savedCart = cartService.save(cart);
        user.setCart(savedCart);
        User savedUser = save(user);
        cart.setUser(user);
        cartService.save(savedCart);
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

    public int totalPriceOfCart(User user){
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
}
