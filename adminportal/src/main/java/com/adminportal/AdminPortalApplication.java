package com.adminportal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.adminportal.repository.RoleRepository;

@SpringBootApplication
public class AdminPortalApplication extends SpringBootServletInitializer implements CommandLineRunner {
    @Autowired
    RoleRepository roleReposiroty;

    @Override
    protected SpringApplicationBuilder configure( SpringApplicationBuilder application ) {
        return application.sources( AdminPortalApplication.class );
    }

    public static void main( String[] args ) {
        SpringApplication.run( AdminPortalApplication.class, args );
    }

    @Override
    public void run( String... args ) throws Exception {
    }
}
