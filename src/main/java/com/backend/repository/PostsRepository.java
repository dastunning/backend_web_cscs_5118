package com.backend.repository;

import com.backend.entity.PostsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PostsRepository extends JpaRepository<PostsEntity, Long> {
    List<PostsEntity> findByAuthor(String author);
    List<PostsEntity> findByCreatedAtAfter(LocalDateTime date);
}
