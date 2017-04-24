package com.adminportal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adminportal.entities.PasswordResetToken;
import com.adminportal.entities.User;
import com.adminportal.entities.UserSkill;
import com.adminportal.entities.security.UserRole;
import com.adminportal.enumeration.Category;
import com.adminportal.enumeration.City;
import com.adminportal.repository.PasswordResetTokenRepository;
import com.adminportal.repository.RoleRepository;
import com.adminportal.repository.SkillRespository;
import com.adminportal.repository.UserRepository;
import com.adminportal.service.UserService;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private static final Logger          LOG = LoggerFactory.getLogger( UserService.class );

    @Autowired
    private UserRepository               userRepository;

    @Autowired
    private RoleRepository               roleRepository;

    @Autowired
    private SkillRespository             skillRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken getPasswordResetToken( final String token ) {
        return passwordResetTokenRepository.findByToken( token );
    }

    @Override
    public void createPasswordResetTokenForUser( final User user, final String token ) {
        final PasswordResetToken myToken = new PasswordResetToken( token, user );
        passwordResetTokenRepository.save( myToken );
    }

    @Override
    public User findByEmail( String email ) {
        return userRepository.findByEmail( email );
    }

    @Override
    public User createUser( User user, Set<UserRole> userRoles, Set<UserSkill> userSkills ) {
        User localUser = userRepository.findByEmail( user.getEmail() );

        if ( localUser != null ) {
            LOG.info( "user {} already exists. Nothing will be done.", user.getEmail() );
        } else {
            for ( UserRole ur : userRoles ) {
                roleRepository.save( ur.getRole() );
            }
            for ( UserSkill userSkill : userSkills ) {
                skillRepository.save( userSkill.getSkill() );
            }

            user.getUserRoles().addAll( userRoles );
            user.getUserSkills().addAll( userSkills );

            localUser = userRepository.save( user );
        }

        return localUser;
    }

    @Override
    public User save( User user ) {
        return userRepository.save( user );
    }

    @Override
    public User findById( Long id ) {
        return userRepository.findOne( id );
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public List<User> findByCategory( Category category ) {
        return userRepository.findByCategory( category );
    }

    @Override
    public List<User> blurrySearch( String name ) {
        List<User> firstNameList = userRepository.findByfirstNameContaining( name );
        List<User> lastNameList = userRepository.findBylastNameContaining( name );
        List<User> activeUserList = new ArrayList<>();

        for ( User user : firstNameList ) {
            if ( user.getEnabled() ) {
                activeUserList.add( user );
            }
        }

        for ( User user : lastNameList ) {
            if ( user.getEnabled() && !firstNameList.contains( user ) ) {
                activeUserList.add( user );
            }
        }

        return activeUserList;
    }

    @Override
    public List<User> findByCity( City city ) {
        return userRepository.findByCity( city );
    }

}
