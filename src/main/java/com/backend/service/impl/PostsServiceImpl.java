package com.backend.service.impl;

import com.backend.entity.PostsEntity;
import com.backend.repository.PostsRepository;
import com.backend.repository.UsersRepository;
import com.backend.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsServiceImpl implements PostsService {
    public final PostsRepository postsRepository;
    public final UsersRepository usersRepository;

    // Create a new post
    @Override
    public PostsEntity createPost(PostsEntity post) {
        return postsRepository.save(post);
    }
    @Override// Get all posts
    public List<PostsEntity> getAllPosts() {
        return postsRepository.findAll();
    }
    // Get a single post by ID
    @Override
    public PostsEntity getPostById(Long postId) {
        return postsRepository.findById(postId).orElse(null);
    }

    // Update a post by ID
    @Override
    public PostsEntity updatePost(Long postId, PostsEntity updatedPost) {
        PostsEntity existingPost = postsRepository.findById(postId).orElse(null);
        if (existingPost != null) {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setText(updatedPost.getText());
            // Update other fields as needed
            return postsRepository.save(existingPost);
        }
        return null;
    }

    // Delete a post by ID
    @Override
    public boolean deletePost(Long postId) {
        if (postsRepository.existsById(postId)) {
            postsRepository.deleteById(postId);
            return true;
        }
        return false;
    }

    // Get posts by author
    @Override
    public List<PostsEntity> getPostsByAuthor(String author) {
        return postsRepository.findByAuthor(author);
    }

    // Create multiple posts
    @Override
    public List<PostsEntity> createMultiplePosts(List<PostsEntity> posts) {
        List<PostsEntity> createdPosts = new ArrayList<>();
        for (PostsEntity post : posts) {
            createdPosts.add(postsRepository.save(post));
        }
        return createdPosts;
    }

    // Custom method: Get posts created in the last N days
    @Override
    public List<PostsEntity> getPostsCreatedInLastNDays(int days) {
        LocalDateTime date = LocalDateTime.now().plusDays(days);
        return postsRepository.findByCreatedAtAfter(date);
    }
}
