package com.example.demo.controller;


import com.example.demo.models.AppUser;
import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.utils.SubUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@Slf4j
public class PostController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    Map<String, String> messages = new HashMap<>();
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok().body(posts);
    }
//    @PostMapping("/addPost")
//    public AppUser addPost(@RequestBody Post post, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        AppUser user = userRepository.findByEmail(post.getEmail());
//        if (user == null) {
//            messages.put("error_message", "invalid inputs");
//            new ObjectMapper().writeValue(response.getOutputStream(), messages);
////        }
////        userService.savePost(post);
////        user.getPosts().add(post);
////        return userRepository.save(user);
//        }
//
    @PostMapping("/follow/{follower}/{following}")
    public  AppUser follow(@PathVariable String follower, String following){
        log.info("email {}", follower);
        AppUser user = null;
        AppUser user2 = null;
            user = userService.getByEmail(follower);
            user2 = userService.getByEmail(following);
            SubUser user3 = new SubUser(user.getEmail(), user.getUserName(), user.getProfilePicture());
            user2.getFollowers().add(user3);
            user2.getMains().add(user3);
        return userService.registerUser(user2);
    }

}