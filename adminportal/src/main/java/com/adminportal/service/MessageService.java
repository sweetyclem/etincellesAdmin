package com.adminportal.service;

import java.util.List;

import com.adminportal.entities.Message;

public interface MessageService {
    Message findById( Long id );

    Message createMessage( Message message );

    List<Message> searchAll( String text );

    List<Message> findAll();

    void removeOne( Long id );
}
