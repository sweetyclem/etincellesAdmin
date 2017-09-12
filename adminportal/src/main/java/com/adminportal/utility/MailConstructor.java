package com.adminportal.utility;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.adminportal.entities.User;

@Component
public class MailConstructor {
    @Autowired
    private Environment env;

    public SimpleMailMessage constructNewAccountEmail( Locale locale, User user, String password ) {

        String url = "https://etincelles.co/login";
        String message = "Bonjour,\n\nVotre compte a été créé sur la plateforme communautaire Etincelles de Social Builder.\n\n"
                + "C'est le moment de compléter votre profil ! Plus vous partagerez d'informations sur votre parcours, vos compétences, vos projets, plus vous boosterez les échanges avec les autres membres de notre communauté.\n\nPour accéder à votre compte, cliquer sur ce lien : ";
        String mess2 = "\n\nVotre mot de passe actuel est : \n" + password
                + "\n\nPour des raisons de sécurité, nous vous conseillons de changer ce mot de passe.\n\nA très vite sur etincelles.co !\n\nL'équipe Social Builder";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo( user.getEmail() );
        email.setSubject( "Plateforme Etincelles- Nouveau compte" );
        email.setText( message + url + mess2 );
        email.setFrom( env.getProperty( "support.email" ) );
        return email;

    }
}
