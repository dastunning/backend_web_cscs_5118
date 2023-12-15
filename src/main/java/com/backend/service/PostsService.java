package com.backend.service;

import com.backend.entity.PostsEntity;

import java.util.List;

public interface PostsService {
    // Create a new post
    PostsEntity createPost(PostsEntity post);

    // Get all posts
    List<PostsEntity> getAllPosts();

    // Get a single post by ID
    PostsEntity getPostById(Long postId);

    // Update a post by ID
    PostsEntity updatePost(Long postId, PostsEntity updatedPost);

    // Delete a post by ID
    boolean deletePost(Long postId);

    // Get posts by author
    List<PostsEntity> getPostsByAuthor(String author);

    // Create multiple posts
    List<PostsEntity> createMultiplePosts(List<PostsEntity> posts);

    // Custom method: Get posts created in the last N days
    List<PostsEntity> getPostsCreatedInLastNDays(int days);
}
