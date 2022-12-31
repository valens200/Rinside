package com.example.demo.controller;


import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.AppUser;
import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    @Autowired
    UserService userService;
    @PostMapping("/add_comment/{postId}/{userId}")
    public ResponseEntity<Post> addComment(@RequestBody Comment comment, @PathVariable long postId, @PathVariable int userId){
        Post post = null;
        AppUser user = null;
        try{
            post = userService.getPostById(postId).get();
            user = userService.getUserById(userId).get();
            Comment comment1 = userService.saveComment(comment);
            user.getComments().add(comment1);
            userService.registerUser(user);
            post.getComments().add(comment1);
            userService.savePost(post);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping("/remove_comment/{postId}/{userId}")
    public void remove_comment(HttpServletResponse response, HttpServletRequest request, @PathVariable long commentId, @PathVariable int userId) throws NotFoundException, IOException {
        AppUser user = null;
        Comment comment1 = null;
        try{
            user = userService.getUserById(userId).get();
            comment1 = userService.getComment(userId);
            if(userService.getCommenterId(comment1.getCommentId()) == user.getUserId()){
                user.getComments().remove(comment1);
            }
            userService.registerUser(user);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById( @PathVariable  int id){
        ResponseEntity<Comment> commentResponseEntity = null;
        try{
            commentResponseEntity = ResponseEntity.ok(userService.getCommentByCommentId(id));
        }catch (Exception e){
            log.error("error {}",e.getMessage());
        }
        return  commentResponseEntity;
    }
}
