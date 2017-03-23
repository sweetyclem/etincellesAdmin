package com.adminportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adminportal.entities.User;
import com.adminportal.enumeration.Category;
import com.adminportal.enumeration.Type;

@Controller
public class UserController {
    @RequestMapping( "/addUsers" )
    public String addUser( Model model ) {
        User user = new User();
        model.addAttribute( "user", user );
        model.addAttribute( "classActiveNewAccount", true );
        model.addAttribute( "coach", Category.COACH );
        model.addAttribute( "etincelle", Category.ETINCELLE );
        model.addAttribute( "staff", Category.STAFF );
        model.addAttribute( "mentor", Category.MENTOR );
        model.addAttribute( "career", Type.CAREER );
        model.addAttribute( "startup", Type.STARTUP );
        return "addUsers";
    }

}
