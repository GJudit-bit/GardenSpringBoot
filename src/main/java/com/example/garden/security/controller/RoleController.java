package com.example.garden.security.controller;


import com.example.garden.security.model.Role;
import com.example.garden.security.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/garden")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/addRole")
    public Role create(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @PostMapping("/updateRole")
    public Role update(@RequestBody Role role) {
        return roleService.updateRole(role);

    }

    @DeleteMapping("/deleteRole/{id}")
    public void delete(@PathVariable Integer id) {
        roleService.deleteRole(id);
    }

    @GetMapping("/findRoleById/{id}")
    public Role findById(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/findRoleByName/{name}")
    public Role findByName(@PathVariable String name) {
        return roleService.getRoleByName(name);
    }

    @GetMapping("/findAllRole")
    public List<Role> findAll() {
        return roleService.getAllRoles(Sort.by("name"));
    }

}
