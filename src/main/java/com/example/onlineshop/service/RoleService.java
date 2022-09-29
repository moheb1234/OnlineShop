package com.example.onlineshop.service;

import com.example.onlineshop.enums.RoleName;
import com.example.onlineshop.model.Role;
import com.example.onlineshop.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.management.InstanceNotFoundException;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.example.onlineshop.ex_handler.ExceptionMessage.roleNotFound;

@Validated
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @SneakyThrows
    public Role findByName(@NotEmpty String roleName) {
        return roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase()))
                .orElseThrow(() -> new InstanceNotFoundException(roleNotFound(roleName)));
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role delete(@NotEmpty String roleName) {
        Role role = findByName(roleName);
        roleRepository.delete(role);
        return role;
    }
}
