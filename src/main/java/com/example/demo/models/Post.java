package com.example.demo.models;


import ch.qos.logback.classic.db.names.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.jpa.repository.Modifying;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "postId")
    private long postId;
    @Column(name = "poster_id", nullable = false, unique = false)
    private long posterId;
    @Column(name = "postDescription")
    private String postDescription;
    @Column(name = "postImage")
    private String postImage;
    @Column(name = "email")
    private String email;
    @OneToMany
    @JoinColumn(
            name = "post_id",
            referencedColumnName = "postId"
    )
    private  Set<Comment> comments = new HashSet<>();

    @OneToMany
    @JoinColumn(
            name = "liked_post_id",
            referencedColumnName = "postId"
    )
    private Set<AppUser> likes = new HashSet<>();
    @OneToMany
    @JoinColumn(
            name = "disliked_post_id",
            referencedColumnName = "postId"
    )
    private Set<AppUser> dislikes = new HashSet<>();

}
