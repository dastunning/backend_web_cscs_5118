package com.backend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "posts")
public class PostsEntity { //Posts table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String text;
    private String author;

    @CreationTimestamp
    private LocalDateTime createdAt; //Automated created date timestamp

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}