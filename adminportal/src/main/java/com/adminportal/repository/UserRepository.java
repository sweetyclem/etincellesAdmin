package com.adminportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
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

    @Query( "select u from User u order by u.lastName" )
    List<User> findAll();

    @Query( "select u.email from User u where u.lastName is null OR u.lastName = '' " )
    List<String> findUnfilled();
}
