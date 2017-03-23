package com.adminportal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.entities.User;
import com.adminportal.enumeration.Category;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail( String email );

    List<User> findByCategory( Category category );

}
