package com.adminportal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.entities.security.Role;
import com.adminportal.repository.RoleRepository;
import com.adminportal.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByname( String name ) {
        return roleRepository.findByname( name );
    }

}
