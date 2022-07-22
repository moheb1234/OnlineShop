package com.example.onlineshop.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "USER", schema = "shop")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "[a-zA-Z_0-9]{5,20}")
    @Column(nullable = false, unique = true)
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    private String password;

    @Pattern(regexp = "[a-zA-Z]{3,10}")
    private String firstname;

    @Pattern(regexp = "[a-zA-Z]{3,10}")
    private String lastname;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    @Min(8)
    @Max(100)
    private int age;

    @NotNull
    private boolean enabled;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date lastModifiedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Cart cart;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Wallet wallet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "USER_ROLE", schema = "shop",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().toString())));
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
