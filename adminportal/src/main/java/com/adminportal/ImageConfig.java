package com.adminportal;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ImageConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers( ResourceHandlerRegistry registry ) {
        super.addResourceHandlers( registry );
        registry.addResourceHandler( "/user_resources/**" )
                .addResourceLocations( "file:///home/clem/etincelles/user_resources/" );
        registry.addResourceHandler( "/user/**" )
                .addResourceLocations( "file:///home/clem/etincelles/user_resources/" );
    }
}
