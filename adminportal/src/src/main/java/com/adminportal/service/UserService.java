package com.adminportal.service;

import java.util.List;
import java.util.Set;

import com.adminportal.entities.PasswordResetToken;
import com.adminportal.entities.User;
import com.adminportal.entities.UserSkill;
import com.adminportal.entities.security.UserRole;
import com.adminportal.enumeration.Category;
import com.adminportal.enumeration.City;

public interface UserService {
    PasswordResetToken getPasswordResetToken( final String token );

    void createPasswordResetTokenForUser( final User user, final String token );

    User findByEmail( String email );

    User findById( Long id );

    User createUser( User user, Set<UserRole> userRoles, Set<UserSkill> userSkills ) throws Exception;

    User save( User user );

    List<User> findAll();

    List<User> findByCategory( Category category );

    List<User> findByCity( City city );

    public List<User> blurrySearch( String name );
}
