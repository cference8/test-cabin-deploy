package com.cf.cabinescaperoom.service;

import com.cf.cabinescaperoom.models.Role;
import com.cf.cabinescaperoom.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repo;


    public Role getRoleById(long id) {
        return repo.findById(id).orElse(null);
    }

    public Role getRoleByRole(String r) {
        return repo.getRoleByRole(r);
    }

    public List<Role> getAllRoles() {
        return (List<Role>) repo.findAll();
    }
}
