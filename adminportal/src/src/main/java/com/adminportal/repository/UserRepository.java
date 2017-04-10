package com.adminportal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.entities.User;
import com.adminportal.enumeration.Category;
import com.adminportal.enumeration.City;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail( String email );

    List<User> findByCategory( Category category );

    List<User> findByCity( City city );

    List<User> findByfirstNameContaining( String firstName );

    List<User> findBylastNameContaining( String lastName );
}
