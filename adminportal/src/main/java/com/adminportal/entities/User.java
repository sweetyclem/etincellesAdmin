package com.adminportal.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.SortNatural;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(columnDefinition = "text")
    private String description;
    @Enumerated(EnumType.STRING)
    private City city;
    @Transient
    private MultipartFile picture;
    private String password;
    private boolean enabled = true;
    private int promo;
    private String twitter;
    private String facebook;
    private String linkedin;
    private String website;
    private boolean hasPicture = false;
    private boolean noContact = false;
    private String sector;
    private String currentPosition;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    private Set<UserRole> userRoles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_skill", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"))
    @SortNatural
    @javax.persistence.OrderBy("name")
    private List<Skill> skills;

    public String getCurrentPosition() {
        return this.currentPosition;
    }

    public void setCurrentPosition(final String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public List<Skill> getSkills() {
        return this.skills;
    }

    public void setSkills(final List<Skill> skills) {
        this.skills = skills;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(final String website) {
        this.website = website;
    }

    public boolean isNoContact() {
        return this.noContact;
    }

    public void setNoContact(final boolean noContact) {
        this.noContact = noContact;
    }

    public String getSector() {
        return this.sector;
    }

    public void setSector(final String sector) {
        this.sector = sector;
    }

    public void setHasPicture(final boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public boolean getHasPicture() {
        return this.hasPicture;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public void setTwitter(final String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public void setFacebook(final String facebook) {
        this.facebook = facebook;
    }

    public String getLinkedin() {
        return this.linkedin;
    }

    public void setLinkedin(final String linkedin) {
        this.linkedin = linkedin;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(final Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(final City city) {
        this.city = city;
    }

    public MultipartFile getPicture() {
        return this.picture;
    }

    public void setPicture(final MultipartFile picture) {
        this.picture = picture;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> authorities = new HashSet<>();
        this.userRoles.forEach(p -> authorities.add(new Authority(p.getRole().getName())));
        return authorities;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getPromo() {
        return this.promo;
    }

    public void setPromo(final int promo) {
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
        return this.enabled;
    }
}
