package com.adminportal.repository;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.entities.Skill;

public interface SkillRespository extends CrudRepository<Skill, Long> {
    Skill findByname( String name );
}
