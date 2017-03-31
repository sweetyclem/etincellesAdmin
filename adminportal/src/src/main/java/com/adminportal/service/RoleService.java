package com.adminportal.service;

import com.adminportal.entities.security.Role;

public interface RoleService {
    Role findByname( String name );
}
