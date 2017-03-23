package com.adminportal.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.adminportal.entities.User;
import com.adminportal.entities.security.Role;
import com.adminportal.entities.security.UserRole;
import com.adminportal.enumeration.Category;
import com.adminportal.enumeration.Type;
import com.adminportal.service.UserService;
import com.adminportal.utility.MailConstructor;
import com.adminportal.utility.SecurityUtility;

@Controller
public class HomeController {

    @Autowired
    private JavaMailSender      mailSender;

    @Autowired
    private MailConstructor     mailConstructor;

    @Autowired
    private UserService         userService;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
            .compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE );

    public static boolean validate( String emailStr ) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher( emailStr );
        return matcher.find();
    }

    @RequestMapping( "/" )
    public String index() {
        return "redirect:/directory";
    }

    @RequestMapping( "/login" )
    public String login( Model model ) {
        model.addAttribute( "classActiveLogin", true );
        return "login";
    }

    @RequestMapping( value = "/addUsers", method = RequestMethod.POST )
    public String newUserPost(
            HttpServletRequest request,
            @ModelAttribute( "email" ) String emails, @RequestParam( value = "category" ) Category category,
            Model model )
            throws Exception {
        model.addAttribute( "classActiveNewAccount", true );
        List<String> emailList = Arrays.asList( emails.split( "\\r\\n|\\n|\\r" ) );
        List<String> rejectedEmails = new ArrayList<>();
        List<String> emailExists = new ArrayList<>();

        model.addAttribute( "coach", Category.COACH );
        model.addAttribute( "etincelle", Category.ETINCELLE );
        model.addAttribute( "staff", Category.STAFF );
        model.addAttribute( "mentor", Category.MENTOR );
        model.addAttribute( "career", Type.CAREER );
        model.addAttribute( "startup", Type.STARTUP );

        for ( String email : emailList ) {
            if ( validate( email ) ) {
                if ( userService.findByEmail( email ) == null ) {
                    User user = new User();
                    user.setEmail( email );
                    user.setCategory( category );

                    System.out.println( "User email : " + email );
                    System.out.println( "Category param : " + category );
                    System.out.println( "User category : " + user.getCategory() );

                    if ( request.getParameterMap().containsKey( "type" ) ) {
                        String type = request.getParameter( "type" );
                        if ( type == Type.CAREER.name() ) {
                            user.setType( Type.CAREER );
                        } else
                            user.setType( Type.STARTUP );
                    } else {
                        if ( user.getCategory().equals( Category.ETINCELLE )
                                || user.getCategory().equals( Category.MENTOR ) ) {
                            model.addAttribute( "noType", "Merci de sélectionner un type (carrière ou startup)" );
                            return "addUsers";
                        }
                    }

                    String password = SecurityUtility.randomPassword();

                    String encryptedPassword = SecurityUtility.passwordEncoder().encode( password );
                    user.setPassword( encryptedPassword );

                    Role role = new Role();
                    role.setRoleId( 1 );
                    role.setName( "ROLE_USER" );

                    Set<UserRole> userRoles = new HashSet<>();
                    userRoles.add( new UserRole( user, role ) );
                    userService.createUser( user, userRoles );

                    String token = UUID.randomUUID().toString();
                    userService.createPasswordResetTokenForUser( user, token );

                    String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
                            + request.getContextPath();

                    SimpleMailMessage mail = mailConstructor.constructResetTokenEmail( appUrl, request.getLocale(),
                            token,
                            user,
                            password );

                    mailSender.send( mail );
                    model.addAttribute( "emailSent", "true" );
                } else {
                    emailExists.add( email );
                }
            } else {
                rejectedEmails.add( email );
            }
        }

        if ( !( rejectedEmails.isEmpty() ) )

        {
            model.addAttribute( "rejectedEmails", rejectedEmails );
        }

        if ( !( emailExists.isEmpty() ) ) {
            model.addAttribute( "emailExists", emailExists );
        }
        return "addUsers";
    }

    @RequestMapping( "/updateUser" )
    public String newUser( Locale locale, Model model, @RequestParam( "id" ) Long id ) {
        User user = userService.findById( id );

        model.addAttribute( "classActiveEdit", true );
        model.addAttribute( "user", user );
        return "updateUser";
    }

    @RequestMapping( "/updateUserInfo" )
    public String updateGet( Model model, Principal principal ) {
        User activeUser = (User) ( (Authentication) principal ).getPrincipal();
        User user = userService.findByEmail( activeUser.getEmail() );
        model.addAttribute( "user", user );
        model.addAttribute( "classActiveEdit", true );
        return "myProfile";
    }

    @RequestMapping( value = "/updateUserInfo", method = RequestMethod.POST )
    public String updateUserInfo( @ModelAttribute( "user" ) User user, HttpServletRequest request, Model model )
            throws Exception {

        User currentUser = userService.findById( user.getId() );
        if ( currentUser == null ) {
            throw new Exception( "User not found" );
        }

        /* check email already exists */
        if ( userService.findByEmail( user.getEmail() ) != null ) {
            if ( userService.findByEmail( user.getEmail() ).getId() != currentUser.getId() ) {
                model.addAttribute( "emailExists", true );
                return "myProfile";
            }
        }

        /*
         * To do : save pictures inside Etincelles app
         * 
         * MultipartFile picture = user.getPicture(); if ( !( picture.isEmpty()
         * ) ) { try { byte[] bytes = picture.getBytes(); String name =
         * user.getId() + ".png"; if ( Files.exists( Paths.get(
         * "src/main/resources/static/images/user/" + name ) ) ) { Files.delete(
         * Paths.get( "src/main/resources/static/images/user/" + name ) ); }
         * BufferedOutputStream stream = new BufferedOutputStream( new
         * FileOutputStream( new File( "src/main/resources/static/images/user/"
         * + name ) ) ); stream.write( bytes ); stream.close();
         * currentUser.setHasPicture( true ); } catch ( Exception e ) {
         * System.out.println( "Erreur ligne 152" ); e.printStackTrace(); } }
         */

        currentUser.setFirstName( user.getFirstName() );
        currentUser.setLastName( user.getLastName() );
        currentUser.setEmail( user.getEmail() );
        currentUser.setCategory( user.getCategory() );
        currentUser.setCity( user.getCity() );
        currentUser.setDescription( user.getDescription() );
        currentUser.setFacebook( user.getFacebook() );
        currentUser.setTwitter( user.getTwitter() );
        currentUser.setLinkedin( user.getLinkedin() );
        currentUser.setPhone( user.getPhone() );
        currentUser.setPromo( user.getPromo() );
        currentUser.setType( user.getType() );
        currentUser.setOrganization( user.getOrganization() );
        currentUser.setJob_title( user.getJob_title() );

        userService.save( currentUser );

        model.addAttribute( "updateSuccess", true );
        model.addAttribute( "user", currentUser );
        model.addAttribute( "classActiveEdit", true );

        return "updateUser";

    }

    @RequestMapping( "/directory" )
    public String directory( Model model ) {
        List<User> userList;
        userList = userService.findAll();

        model.addAttribute( "userList", userList );
        model.addAttribute( "career", Type.CAREER );
        model.addAttribute( "startup", Type.STARTUP );
        return "directory";
    }

    @RequestMapping( "/deleteUser" )
    public String deleteUser( Model model, @RequestParam( "id" ) Long id ) {
        User user = userService.findById( id );
        user.setEnabled( false );
        userService.save( user );
        List<User> userList;
        userList = userService.findAll();

        model.addAttribute( "userList", userList );
        model.addAttribute( "career", Type.CAREER );
        model.addAttribute( "startup", Type.STARTUP );
        return "directory";
    }

    @RequestMapping( "/activateUser" )
    public String activateUser( Model model, @RequestParam( "id" ) Long id ) {
        User user = userService.findById( id );
        user.setEnabled( true );
        userService.save( user );
        List<User> userList;
        userList = userService.findAll();

        model.addAttribute( "userList", userList );
        model.addAttribute( "career", Type.CAREER );
        model.addAttribute( "startup", Type.STARTUP );
        return "directory";
    }

}
