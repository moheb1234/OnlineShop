package com.example.onlineshop.security;

import com.example.onlineshop.enums.RoleName;
import com.example.onlineshop.security.jwt.JwtTokenFilter;
import com.example.onlineshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Deprecated
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(UserService userService, JwtTokenFilter jwtTokenFilter) {
        this.userService = userService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()).and().authorizeRequests()
                .antMatchers(adminAuthorizeHttp()).hasAnyAuthority(RoleName.ADMIN.toString())
                .antMatchers(userAuthorizeHttp()).hasAnyAuthority(RoleName.USER.toString())
                .antMatchers(permitAllAuthorizeHttp()).permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().logout()
                .and().csrf().disable();
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return provider;
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    public String[] adminAuthorizeHttp() {
        return new String[]{"/product/**", "/role/**", "user/**"};
    }

    public String[] userAuthorizeHttp() {
        return new String[]{"/user/cart/**", "/user/add-product/**", "user/remove-product/**"};
    }

    public String[] permitAllAuthorizeHttp() {
        return new String[]{"/products/**", "/signing", "/signup","/user/verifying/**","/email/forget-password/**","/email/resend-verifying-code/**"};
    }
}
