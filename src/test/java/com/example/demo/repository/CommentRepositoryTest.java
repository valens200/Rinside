package com.example.demo.repository;

import com.example.demo.models.AppUser;
import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    public void registerCOmment(){

        AppUser user1 = AppUser.builder()
                .userName("vava")
                .password("vava2003")
                .email("uwavalens2003@gmail.com")
                .build();
        userRepository.save(user1);
        AppUser user = AppUser.builder()
                .userName("vava")
                .password("vava2003")
                .email("uwavalens2003@gmail.com")
                .Followers(new HashSet<>(List.of(user1)))
                .build();
        userRepository.save(user);

        Comment comment = Comment.builder()
                .comment("hello")
                .build();
        commentRepository.save(comment);
        Post post = Post.builder()
                .postDescription("hello world")
                .build();
        postRepository.save(post);
        System.out.println("helooooooooooooooooooooooooo");
        System.out.println( postRepository.findAll());

    }

}