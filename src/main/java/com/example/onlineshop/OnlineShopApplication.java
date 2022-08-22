package com.example.onlineshop;

import com.example.onlineshop.enums.RoleName;
import com.example.onlineshop.model.Role;
import com.example.onlineshop.model.User;
import com.example.onlineshop.repository.RoleRepository;
import com.example.onlineshop.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@AllArgsConstructor
public class OnlineShopApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Role adminRole = new Role();
        Role userRole = new Role();
        if (roleRepository.findByName(RoleName.ADMIN).isEmpty()) {
            adminRole.setName(RoleName.ADMIN);
            roleRepository.save(adminRole);
        }
        if (roleRepository.findByName(RoleName.USER).isEmpty()) {
            userRole.setName(RoleName.USER);
            roleRepository.save(userRole);
        }
        String email = "mohebmoallem@gmail.com";
        if (userRepository.findByEmail(email).isEmpty()) {
            User admin = new User("admin", "Password12@", "moheb", "moallem", email
                    , "madrid", "address1", 26, null, true);
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            userRepository.save(admin);
        }
    }
}
