package com.adminportal.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adminportal.entities.Post;
import com.adminportal.entities.User;
import com.adminportal.entities.security.Role;
import com.adminportal.entities.security.UserRole;
import com.adminportal.enumeration.Category;
import com.adminportal.enumeration.Type;
import com.adminportal.service.PostService;
import com.adminportal.service.RoleService;
import com.adminportal.service.UserService;
import com.adminportal.utility.FileUtility;
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

    @Autowired
    private PostService         postService;

    @Autowired
    private RoleService         roleService;

    @Autowired
    private FileUtility         fileUtility;

    @Autowired
    private SecurityUtility     securityUtility;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
            .compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE );

    public static boolean validate( final String emailStr ) {
        final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher( emailStr );
        return matcher.find();
    }

    @RequestMapping( "/" )
    public String index() {
        return "redirect:/directory";
    }

    @RequestMapping( "/login" )
    public String login( final Model model, final HttpServletRequest request ) {
        if ( request.getParameterMap().containsKey( "accessDenied" ) ) {
            if ( request.getParameter( "accessDenied" ) != null ) {
                model.addAttribute( "accessDenied", true );
            }
        }
        model.addAttribute( "classActiveLogin", true );
        return "login";
    }

    @RequestMapping( value = "/addUsers", method = RequestMethod.POST )
    public String newUserPost( final HttpServletRequest request, @ModelAttribute( "email" ) final String emails,
            @RequestParam( value = "category" ) final Category category, final Model model )
            throws Exception {
        model.addAttribute( "classActiveNewAccount", true );
        final List<String> emailList = Arrays.asList( emails.split( "\\r\\n|\\n|\\r" ) );
        final List<String> rejectedEmails = new ArrayList<>();
        final List<String> emailExists = new ArrayList<>();

        for ( final String email : emailList ) {
            if ( validate( email ) ) {
                if ( this.userService.findByEmail( email ) == null ) {
                    final User user = new User();
                    user.setEmail( email );
                    user.setCategory( category );

                    if ( request.getParameterMap().containsKey( "type" ) ) {
                        final String type = request.getParameter( "type" );

                        if ( Type.Carriere == Type.valueOf( type ) ) {
                            user.setType( Type.Carriere );
                        } else if ( Type.Startup == Type.valueOf( type ) ) {
                            user.setType( Type.Startup );
                        } else if ( Type.DataMarketing == Type.valueOf( type ) ) {
                            user.setType( Type.DataMarketing );
                        } else if ( Type.DigitalBuisnessDevelopment == Type.valueOf( type ) ) {
                            user.setType( Type.DigitalBuisnessDevelopment );
                        } else if ( Type.SalesforceDatabaseAdmin == Type.valueOf( type ) ) {
                            user.setType( Type.SalesforceDatabaseAdmin );
                        }
                    } else {
                        if ( user.getCategory().equals( Category.Etincelle )
                                || user.getCategory().equals( Category.Mentore ) ) {
                            model.addAttribute( "noType", "Merci de sélectionner un type (carrière ou startup)" );
                            return "addUsers";
                        }
                    }

                    final String password = securityUtility.randomPassword();

                    final String encryptedPassword = securityUtility.passwordEncoder().encode( password );
                    user.setPassword( encryptedPassword );

                    final Role role = this.roleService.findByname( "ROLE_USER" );

                    final Set<UserRole> userRoles = new HashSet<>();
                    userRoles.add( new UserRole( user, role ) );
                    user.setSkills( new ArrayList<>() );
                    this.userService.createUser( user, userRoles );

                    // String token = UUID.randomUUID().toString();
                    // userService.createPasswordResetTokenForUser( user, token
                    // );

                    // String appUrl = "http://" + request.getServerName() + ":"
                    // + request.getServerPort() + request.getContextPath();

                    final SimpleMailMessage mail = this.mailConstructor.constructNewAccountEmail( request.getLocale(),
                            user, password );

                    this.mailSender.send( mail );
                    model.addAttribute( "emailSent", "true" );
                } else {
                    emailExists.add( email );
                }
            } else {
                rejectedEmails.add( email );
            }
        }

        if ( !rejectedEmails.isEmpty() )

        {
            model.addAttribute( "rejectedEmails", rejectedEmails );
        }

        if ( !emailExists.isEmpty() ) {
            model.addAttribute( "emailExists", emailExists );
        }
        return "addUsers";
    }

    @RequestMapping( "/updateUser" )
    public String newUser( final Locale locale, final Model model, @RequestParam( "id" ) final Long id ) {
        final User user = this.userService.findById( id );

        model.addAttribute( "classActiveEdit", true );
        model.addAttribute( "user", user );
        return "updateUser";
    }

    @RequestMapping( "/updateUserInfo" )
    public String updateGet( final Model model, final Principal principal ) {
        final User activeUser = (User) ( (Authentication) principal ).getPrincipal();
        final User user = this.userService.findByEmail( activeUser.getEmail() );
        model.addAttribute( "user", user );
        model.addAttribute( "classActiveEdit", true );
        return "updateUser";
    }

    @RequestMapping( value = "/updateUserInfo", method = RequestMethod.POST )
    public String updateUserInfo( @ModelAttribute( "user" ) final User user, final HttpServletRequest request,
            final Model model ) throws Exception {

        final User currentUser = this.userService.findById( user.getId() );
        if ( currentUser == null ) {
            throw new Exception( "User not found" );
        }

        /* check email already exists */
        if ( this.userService.findByEmail( user.getEmail() ) != null ) {
            if ( this.userService.findByEmail( user.getEmail() ).getId() != currentUser.getId() ) {
                model.addAttribute( "emailExists", true );
                return "updateUser";
            }
        }

        final MultipartFile picture = user.getPicture();
        if ( !picture.isEmpty() ) {
            try {
                // Crop the image (uploadfile is an object of type
                // MultipartFile)
                final BufferedImage croppedImage = this.fileUtility.cropImageSquare( picture.getBytes() );

                final String name = user.getId() + ".png";
                if ( Files.exists( Paths.get( "/home/clem/etincelles/images/user/" + name ) ) ) {
                    Files.delete( Paths.get( "/home/clem/etincelles/images/user/" + name ) );
                }
                // Save the file locally
                final File outputfile = new File( "/home/clem/etincelles/images/user/" + name );
                ImageIO.write( croppedImage, "png", outputfile );
                currentUser.setHasPicture( true );
            } catch ( final Exception e ) {
                System.out.println( "Erreur ligne 198" );
                e.printStackTrace();
            }
        }

        currentUser.setFirstName( user.getFirstName() );
        currentUser.setLastName( user.getLastName() );
        currentUser.setEmail( user.getEmail() );
        currentUser.setDescription( user.getDescription() );
        currentUser.setCity( user.getCity() );
        currentUser.setCategory( user.getCategory() );
        currentUser.setFacebook( user.getFacebook() );
        currentUser.setTwitter( user.getTwitter() );
        currentUser.setLinkedin( user.getLinkedin() );
        currentUser.setPromo( user.getPromo() );
        currentUser.setType( user.getType() );
        currentUser.setSector( user.getSector() );
        currentUser.setCurrentPosition( user.getCurrentPosition() );

        this.userService.save( currentUser );

        model.addAttribute( "updateSuccess", true );
        model.addAttribute( "user", currentUser );
        model.addAttribute( "classActiveEdit", true );

        return "updateUser";

    }

    @RequestMapping( "/directory" )
    public String directory( final Model model ) {
        List<User> userList;
        userList = this.userService.findAll();
        model.addAttribute( "userList", userList );
        return "directory";
    }

    @RequestMapping( value = "/removeUser" )
    public String removeUser( @RequestParam( "id" ) final Long id, final Model model ) {
        this.userService.removeOne( id );
        final List<Post> postList = this.postService.findAll();
        model.addAttribute( "postList", postList );

        return "redirect:/directory";
    }

    @RequestMapping( "/addUsers" )
    public String addUser( final Model model ) {
        final User user = new User();
        model.addAttribute( "user", user );
        model.addAttribute( "classActiveNewAccount", true );
        return "addUsers";
    }

    @RequestMapping( "/news" )
    public String news( final Model model ) {
        final List<Post> posts = this.postService.findAll();
        model.addAttribute( "postList", posts );
        return "news";
    }

    @RequestMapping( value = "/remove", method = RequestMethod.POST )
    public String remove( @ModelAttribute( "id" ) final Long id, final Model model ) {
        this.postService.removeOne( id );
        final List<Post> postList = this.postService.findAll();
        model.addAttribute( "postList", postList );

        return "redirect:/news";
    }

    @RequestMapping( value = "/updatePost", method = RequestMethod.GET )
    public String updatePost( final Model model, @RequestParam( "id" ) final Long id ) {
        final Post post = this.postService.findById( id );

        model.addAttribute( "classActiveEdit", true );
        model.addAttribute( "post", post );
        return "updatePost";
    }

    @RequestMapping( value = "/updatePost", method = RequestMethod.POST )
    public String updatePostPost( @ModelAttribute( "post" ) final Post post,
            final HttpServletRequest request, final Model model ) throws Exception {

        final Post currentPost = this.postService.findById( post.getId() );
        if ( currentPost == null ) {
            throw new Exception( "Post not found" );
        }

        final MultipartFile picture = post.getPicture();
        if ( !picture.isEmpty() ) {
            try {
                final byte[] bytes = picture.getBytes();
                final String name = post.getId() + ".png";
                if ( Files.exists( Paths.get( "/home/clem/etincelles/images/post/" + name ) ) ) {
                    Files.delete( Paths.get( "/home/clem/etincelles/images/post/" + name ) );
                }
                final BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream( new File( "/home/clem/etincelles/images/post/" + name ) ) );
                stream.write( bytes );
                stream.close();
                currentPost.setHasPicture( true );
            } catch ( final Exception e ) {
                System.out.println( "Erreur ligne 338 sauvegarde de l'image" );
                e.printStackTrace();
            }
        }

        currentPost.setTitle( post.getTitle() );
        currentPost.setText( post.getText() );

        this.postService.save( currentPost );

        model.addAttribute( "updateSuccess", true );
        model.addAttribute( "post", currentPost );
        model.addAttribute( "classActiveEdit", true );

        return "updatePost";
    }

    @RequestMapping( value = "/createPost", method = RequestMethod.GET )
    public String createPost( final Model model ) {
        model.addAttribute( "classActiveEdit", true );
        return "createPost";
    }

    @RequestMapping( value = "/createPost", method = RequestMethod.POST )
    public String createPostPost( @ModelAttribute( "post" ) final Post post,
            final HttpServletRequest request, final Model model ) throws Exception {
        if ( post != null ) {
            // this.postService.save( post );

            if ( post.getPicture() != null ) {
                final MultipartFile picture = post.getPicture();
                if ( !picture.isEmpty() ) {
                    try {
                        final byte[] bytes = picture.getBytes();
                        final String name = post.getId() + ".png";
                        if ( Files.exists( Paths.get( "/home/clem/etincelles/images/post/" + name ) ) ) {
                            Files.delete( Paths.get( "/home/clem/etincelles/images/post/" + name ) );
                        }
                        final BufferedOutputStream stream = new BufferedOutputStream(
                                new FileOutputStream( new File( "/home/clem/etincelles/images/post/" + name ) ) );
                        stream.write( bytes );
                        stream.close();
                        post.setHasPicture( true );
                    } catch ( final Exception e ) {
                        System.out.println( "Erreur ligne 381 sauvegarde de l'image" );
                        e.printStackTrace();
                    }
                } else {
                    post.setHasPicture( false );
                    System.out.println( "picture is empty" );
                }
            }

            post.setDate( new Date() );
            this.postService.save( post );
            model.addAttribute( "createSuccess", true );
            model.addAttribute( "classActiveEdit", true );
        }

        return "redirect:/news";
    }

    @RequestMapping( value = "/forgetPassword", method = RequestMethod.GET )
    public String forgetPassword( @RequestParam( "id" ) final Long id, final Model model,
            final HttpServletRequest request ) {
        final User user = this.userService.findById( id );

        if ( user == null ) {
            model.addAttribute( "emailNotSent", true );
            return "redirect:/directory";
        }

        final String password = securityUtility.randomPassword();

        final String encryptedPassword = securityUtility.passwordEncoder().encode( password );
        user.setPassword( encryptedPassword );

        this.userService.save( user );

        // String token = UUID.randomUUID().toString();
        // userService.createPasswordResetTokenForUser( user, token );

        // String appUrl = "http://" + request.getServerName() + ":" +
        // request.getServerPort() + request.getContextPath();

        final SimpleMailMessage newEmail = this.mailConstructor.constructNewAccountEmail( request.getLocale(), user,
                password );

        this.mailSender.send( newEmail );

        model.addAttribute( "emailSent", "true" );

        return "redirect:/directory";
    }

    @RequestMapping( "/addAdmin" )
    public String addAdmin() {
        return "redirect:/addUsers";
    }

    @RequestMapping( value = "/addAdmin", method = RequestMethod.POST )
    public String newAdminPost( final HttpServletRequest request, @ModelAttribute( "email" ) final String emails,
            @RequestParam( value = "category" ) final Category category, final Model model )
            throws Exception {
        model.addAttribute( "classActiveNewAdmin", true );
        final List<String> emailList = Arrays.asList( emails.split( "\\r\\n|\\n|\\r" ) );
        final List<String> rejectedEmails = new ArrayList<>();
        final List<String> emailExists = new ArrayList<>();
        final List<String> roleAdded = new ArrayList<>();

        for ( final String email : emailList ) {
            if ( validate( email ) ) {
                if ( this.userService.findByEmail( email ) == null ) {
                    final User user = new User();
                    user.setEmail( email );
                    user.setCategory( category );

                    final String password = securityUtility.randomPassword();

                    final String encryptedPassword = securityUtility.passwordEncoder().encode( password );
                    user.setPassword( encryptedPassword );

                    final Role role = this.roleService.findByname( "ROLE_ADMIN" );
                    final Set<UserRole> userRoles = new HashSet<>();
                    userRoles.add( new UserRole( user, role ) );
                    this.userService.createUser( user, userRoles );

                    // String token = UUID.randomUUID().toString();
                    // userService.createPasswordResetTokenForUser( user, token
                    // );

                    // String appUrl = "http://" + request.getServerName() + ":"
                    // + request.getServerPort() + request.getContextPath();

                    final SimpleMailMessage mail = this.mailConstructor.constructNewAccountEmail( request.getLocale(),
                            user, password );
                    this.mailSender.send( mail );
                    model.addAttribute( "emailSent", "true" );
                } else {
                    final User user = this.userService.findByEmail( email );
                    final Role role = this.roleService.findByname( "ROLE_ADMIN" );
                    final Set<UserRole> userRoles = user.getUserRoles();
                    userRoles.add( new UserRole( user, role ) );
                    this.userService.save( user );
                    emailExists.add( email );
                    roleAdded.add( email );
                }
            } else {
                rejectedEmails.add( email );
            }
        }

        if ( !rejectedEmails.isEmpty() )

        {
            model.addAttribute( "rejectedEmails", rejectedEmails );
        }

        if ( !emailExists.isEmpty() ) {
            model.addAttribute( "userEmailExists", emailExists );
        }

        if ( !roleAdded.isEmpty() ) {
            model.addAttribute( "roleAdded", roleAdded );
        }

        return "addUsers";
    }

    @RequestMapping( "/accessDenied" )
    public String AccessDenied( final RedirectAttributes redirectAttributes ) {
        redirectAttributes.addAttribute( "accessDenied", true );
        return "redirect:/login";
    }

    @RequestMapping( "/unfilled" )
    public String findUnfilled( final Model model ) {
        List<String> emails = userService.findUnfilled();
        model.addAttribute( "emailList", emails );
        return "unfilled";
    }

}
