package com.example.demo.models;


import ch.qos.logback.classic.db.names.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "postDescription")
    private String postDescription;
    @Column(name = "postImage")
    private String postImage;
    @Column(name = "email")
    private String email;
//    @JsonIgnore
//    @OneToMany(
//            cascade = CascadeType.ALL,
//            fetch = FetchType.EAGER
//    )
//    @JoinColumn(
//            name = "postId",
//            referencedColumnName = "postId"
//    )
//    private List<Comment> comments;


}
