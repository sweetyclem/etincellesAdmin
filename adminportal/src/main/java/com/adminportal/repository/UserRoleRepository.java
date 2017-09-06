
package com.adminportal.repository;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.entities.security.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

}