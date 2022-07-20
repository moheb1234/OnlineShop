package com.example.onlineshop.repository;

import com.example.onlineshop.enums.RoleName;
import com.example.onlineshop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName roleName);
}
