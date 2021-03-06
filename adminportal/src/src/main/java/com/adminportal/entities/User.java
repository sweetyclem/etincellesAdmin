package com.adminportal.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.adminportal.entities.security.Authority;
import com.adminportal.entities.security.UserRole;
import com.adminportal.enumeration.Category;
import com.adminportal.enumeration.City;
import com.adminportal.enumeration.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "id", nullable = false, updatable = false )
    private Long              id;
    private String            firstName;
    private String            lastName;
    @Column( name = "email", nullable = false )
    private String            email;
    @Column( columnDefinition = "text" )
    private String            description;
    @Enumerated( EnumType.STRING )
    private City              city;
    @Transient
    private MultipartFile     picture;
    private String            password;
    private boolean           enabled          = true;
    private int               promo;
    private String            twitter;
    private String            facebook;
    private String            linkedin;
    private boolean           hasPicture       = false;

    private String            sector;

    @Enumerated( EnumType.STRING )
    private Category          category;

    @Enumerated( EnumType.STRING )
    private Type              type;

    @OneToMany( mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JsonIgnore
    private Set<UserRole>     userRoles        = new HashSet<>();

    @OneToMany( mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JsonIgnore
    private Set<UserSkill>    userSkills       = new HashSet<>();

    public String getSector() {
        return sector;
    }

    public void setSector( String sector ) {
        this.sector = sector;
    }

    public Set<UserSkill> getUserSkills() {
        return userSkills;
    }

    public void setUserSkills( Set<UserSkill> userSkills ) {
        this.userSkills = userSkills;
    }

    public void setHasPicture( boolean hasPicture ) {
        this.hasPicture = hasPicture;
    }

    public boolean getHasPicture() {
        return hasPicture;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter( String twitter ) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook( String facebook ) {
        this.facebook = facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin( String linkedin ) {
        this.linkedin = linkedin;
    }

    public Type getType() {
        return type;
    }

    public void setType( Type type ) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory( Category category ) {
        this.category = category;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles( Set<UserRole> userRoles ) {
        this.userRoles = userRoles;
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public City getCity() {
        return city;
    }

    public void setCity( City city ) {
        this.city = city;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture( MultipartFile picture ) {
        this.picture = picture;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        userRoles.forEach( p -> authorities.add( new Authority( p.getRole().getName() ) ) );
        return authorities;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getPromo() {
        return promo;
    }

    public void setPromo( int promo ) {
        this.promo = promo;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return enabled;
    }
}
