package com.backend.controller;

import com.backend.entity.PostsEntity;
import com.backend.service.PostsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@AllArgsConstructor
@RequestMapping("/api/posts")
public class PostsController {

    private PostsService postService;

    // Create a new post
    @PostMapping
    public ResponseEntity<PostsEntity> createPost(@RequestBody PostsEntity post) {
        PostsEntity createdPost = postService.createPost(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // Get all posts
    @GetMapping
    public ResponseEntity<List<PostsEntity>> getAllPosts() {
        List<PostsEntity> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Get a single post by ID
    @GetMapping("/{postId}")
    public ResponseEntity<PostsEntity> getPostById(@PathVariable Long postId) {
        PostsEntity post = postService.getPostById(postId);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update a post by ID
    @PutMapping("/{postId}")
    public ResponseEntity<PostsEntity> updatePost(@PathVariable Long postId, @RequestBody PostsEntity updatedPost) {
        PostsEntity post = postService.updatePost(postId, updatedPost);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a post by ID
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        if (postService.deletePost(postId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // Get posts by author
    @GetMapping("/byAuthor/{author}")
    public ResponseEntity<List<PostsEntity>> getPostsByAuthor(@PathVariable String author) {
        List<PostsEntity> posts = postService.getPostsByAuthor(author);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Create multiple posts
    @PostMapping("/multiple")
    public ResponseEntity<List<PostsEntity>> createMultiplePosts(@RequestBody List<PostsEntity> posts) {
        List<PostsEntity> createdPosts = postService.createMultiplePosts(posts);
        return new ResponseEntity<>(createdPosts, HttpStatus.CREATED);
    }

    // Custom method: Get posts created in the last N days
    @GetMapping("/createdInLastNDays/{days}")
    public ResponseEntity<List<PostsEntity>> getPostsCreatedInLastNDays(@PathVariable int days) {
        List<PostsEntity> posts = postService.getPostsCreatedInLastNDays(days);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}