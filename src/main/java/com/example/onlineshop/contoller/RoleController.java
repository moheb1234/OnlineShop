package com.example.onlineshop.contoller;

import com.example.onlineshop.model.Role;
import com.example.onlineshop.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("role/all")
    public ResponseEntity<List<Role>> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("role/{name}")
    public ResponseEntity<Role> findByName(@PathVariable String name){
        return ResponseEntity.ok(roleService.findByName(name));
    }

    @PostMapping("role/create")
    public ResponseEntity<Role> create(@RequestBody  Role role){
        return new ResponseEntity<>(roleService.save(role), HttpStatus.CREATED);
    }

    @DeleteMapping("role/delete/{id}")
    public ResponseEntity<Role> delete(@PathVariable long id){
        return ResponseEntity.ok(roleService.delete(id));
    }
}
