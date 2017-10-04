package com.adminportal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.entities.Post;

public interface PostRepository extends CrudRepository<Post, Long> {
    Post findById( Long id );

    List<Post> findByTitleContaining( String title );

    List<Post> findByTextContaining( String text );
}
