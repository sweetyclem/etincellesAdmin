package com.adminportal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adminportal.entities.Post;
import com.adminportal.repository.PostRepository;
import com.adminportal.service.PostService;

@Transactional
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public Post findById( Long id ) {
        return postRepository.findById( id );
    }

    @Override
    public List<Post> searchAll( String text ) {
        List<Post> TitleList = postRepository.findByTitleContaining( text );
        List<Post> TextList = postRepository.findByTextContaining( text );
        List<Post> postList = new ArrayList<>();

        for ( Post post : TextList ) {
            if ( !TitleList.contains( post ) ) {
                postList.add( post );
            }
        }

        return postList;
    }

    @Override
    public List<Post> findAll() {
        List<Post> postList = (List<Post>) postRepository.findAllByOrderByDateDesc();
        return postList;
    }

    @Override
    public Post createPost( Post post ) {
        Post localPost = postRepository.save( post );
        return localPost;
    }

    @Override
    public void removeOne( Long id ) {
        postRepository.delete( id );
    }

    @Override
    public Post save( Post post ) {
        return postRepository.save( post );
    }

}
