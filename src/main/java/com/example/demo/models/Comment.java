package com.example.demo.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
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
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "commentId")
    private long commentId;
    @Column(name = "comment")
    private String comment;
    @OneToMany
    @JoinColumn(
            name = "liked_comment_id",
            referencedColumnName = "commentId"
    )
    private Set<AppUser> likes = new HashSet<>();
    @OneToMany
    @JoinColumn(
            name = "disliked_comment_id",
            referencedColumnName = "commentId"
    )
    private Set<AppUser> dislikes = new HashSet<>();

}


