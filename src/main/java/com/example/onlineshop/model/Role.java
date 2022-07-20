package com.example.onlineshop.model;

import lombok.Data;
import com.example.onlineshop.enums.RoleName;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ROLE", schema = "shop")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true , nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;
}
