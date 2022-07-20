package com.example.onlineshop.service;

import lombok.SneakyThrows;
import com.example.onlineshop.enums.RoleName;
import com.example.onlineshop.model.Role;
import com.example.onlineshop.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @SneakyThrows
    public Role findByName(String roleName) {
        return roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase())).orElseThrow(InstanceNotFoundException::new);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    @SneakyThrows
    public Role findById(long id) {
        return roleRepository.findById(id).orElseThrow(InstanceNotFoundException::new);
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public Role delete(long id){
        Role role = findById(id);
        roleRepository.delete(role);
        return role;
    }
}
