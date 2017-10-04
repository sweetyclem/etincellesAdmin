package com.adminportal.service;

import java.util.List;

import com.adminportal.entities.Post;

public interface PostService {
    Post findById( Long id );

    Post createPost( Post post );

    List<Post> searchAll( String text );

    List<Post> findAll();

    void removeOne( Long id );

    Post save( Post post );
}
