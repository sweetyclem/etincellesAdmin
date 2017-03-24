package com.adminportal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.entities.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Message findById( Long id );

    List<Message> findByTitleContaining( String title );

    List<Message> findByTextContaining( String text );
}
