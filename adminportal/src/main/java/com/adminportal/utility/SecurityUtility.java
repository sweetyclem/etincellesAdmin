package com.adminportal.utility;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtility {
    @Autowired
    private Environment env;

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder( 12, new SecureRandom( env.getProperty( "SALT" ).getBytes() ) );
    }

    public String randomPassword() {
        String SALTCHARS = env.getProperty( "SALTCHARS" );
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while ( salt.length() < 18 ) {
            int index = (int) ( rnd.nextFloat() * SALTCHARS.length() );
            salt.append( SALTCHARS.charAt( index ) );
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}