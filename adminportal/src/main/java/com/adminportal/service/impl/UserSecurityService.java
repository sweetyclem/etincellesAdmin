package com.adminportal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adminportal.entities.User;
import com.adminportal.repository.UserRepository;

@Transactional
@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = userRepository.findByEmail( username );

        if ( null == user ) {
            throw new UsernameNotFoundException( "Username not found" );
        }

        if ( user.getUserRoles().isEmpty() ) {
            System.out.println( "userRoles is empty" );
        }

        return user;
    }

    // @Override
    // public UserDetails loadUserByUsername( String username ) throws
    // UsernameNotFoundException {
    // User user = userRepository.findByEmail( username );
    //
    // if ( null == user ) {
    // throw new UsernameNotFoundException( "Email not found" );
    // }
    //
    // List<String> roles = new ArrayList<>();
    //
    // if ( user.getUserRoles().isEmpty() ) {
    // System.out.println( "userRoles is empty" );
    // }
    //
    // for ( UserRole userRole : user.getUserRoles() ) {
    // System.out.println( userRole.getRole().getName() );
    // roles.add( userRole.getRole().getName() );
    // }
    //
    // return new org.springframework.security.core.userdetails.User(
    // user.getEmail(), user.getPassword(), user.getEnabled(), true, true,
    // true, getGrantedAuthorities( roles ) );
    // }
    //
    // private List<GrantedAuthority> getGrantedAuthorities( List<String> roles
    // ) {
    // List<GrantedAuthority> authorities = new ArrayList<>();
    // for ( String role : roles ) {
    // authorities.add( new SimpleGrantedAuthority( role ) );
    // }
    // return authorities;
    // }
}