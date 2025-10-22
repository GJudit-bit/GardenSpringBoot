package com.example.garden.security.service;

import com.example.garden.security.model.Role;
import com.example.garden.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role getRoleById(Integer id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public void deleteRole(Integer id) {
        roleRepository.deleteById(id);
    }

    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    public java.util.List<Role> getAllRoles(Sort sort) {
        return roleRepository.findAll(sort);
    }
}
