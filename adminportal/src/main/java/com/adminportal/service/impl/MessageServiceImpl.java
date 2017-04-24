package com.adminportal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adminportal.entities.Message;
import com.adminportal.repository.MessageRepository;
import com.adminportal.service.MessageService;

@Transactional
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Message findById( Long id ) {
        return messageRepository.findById( id );
    }

    @Override
    public List<Message> searchAll( String text ) {
        List<Message> TitleList = messageRepository.findByTitleContaining( text );
        List<Message> TextList = messageRepository.findByTextContaining( text );
        List<Message> messageList = new ArrayList<>();

        for ( Message message : TextList ) {
            if ( !TitleList.contains( message ) ) {
                messageList.add( message );
            }
        }

        return messageList;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messageList = (List<Message>) messageRepository.findAll();
        return messageList;
    }

    @Override
    public Message createMessage( Message message ) {
        Message localMessage = messageRepository.save( message );
        return localMessage;
    }

    @Override
    public void removeOne( Long id ) {
        messageRepository.delete( id );
    }

    @Override
    public Message save( Message message ) {
        return messageRepository.save( message );
    }

}
