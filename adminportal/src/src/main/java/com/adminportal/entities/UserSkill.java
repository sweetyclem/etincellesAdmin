package com.adminportal.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "user_skill" )
public class UserSkill {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long  userSkillId;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "user_id" )
    private User  user;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "skill_id" )
    private Skill skill;

    public UserSkill() {
    }

    public UserSkill( User user, Skill skill ) {
        this.user = user;
        this.skill = skill;
    }

    public Long getUserSkillId() {
        return userSkillId;
    }

    public void setUserSkillId( Long userSkillId ) {
        this.userSkillId = userSkillId;
    }

    public User getUser() {
        return user;
    }

    public void setUser( User user ) {
        this.user = user;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill( Skill skill ) {
        this.skill = skill;
    }

}