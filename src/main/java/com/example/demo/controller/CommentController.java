package com.example.demo.controller;


import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.AppUser;
import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController extends  Controller {
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

    @DeleteMapping("/remove_comment/{commentId}/{userId}")
    public ResponseEntity<?> remove_comment(HttpServletResponse response, HttpServletRequest request, @PathVariable long commentId, @PathVariable int userId) throws NotFoundException, IOException {
        AppUser user = null;
        Comment comment1 = null;
        try{
            user = userService.getUserById(userId).get();
            comment1 = userService.getCommentByCommentId(commentId);
            if(userService.getCommenterId(comment1.getCommentId()) == user.getUserId()){
                user.getComments().remove(comment1);
            }
            userService.registerUser(user);

        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return ResponseEntity.ok(new HashMap<String, String>().put("Error :  " , exception.getMessage()));
        }
        return ResponseEntity.ok(new HashMap<String, String>().put("Success " , "Comment deleted successfuly"));
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

    @PostMapping("/like_comment/{commentId}/{userId}")
    public  ResponseEntity<?> likeComment(@PathVariable int userId, @PathVariable long commentId){
        AppUser user = null;
        Comment comment = null;
        try{
            user = userService.findUserById(userId);
            comment = userService.getCommentByCommentId(commentId);
            if (comment.getLikes().contains(user)) {
                comment.setLikes(comment.getLikes());
            }else{
                comment.getLikes().add(user);
            }
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
        return ResponseEntity.ok(userService.saveComment(comment));
    }
    @DeleteMapping("/unlike_comment/{commentId}/{userId}")
    public  ResponseEntity<?> unlikeComment(@PathVariable int userId, @PathVariable long commentId){
        AppUser user = null;
        Comment comment = null;
        try{
            user = userService.findUserById(userId);
            comment = userService.getCommentByCommentId(commentId);
            if (comment.getLikes().contains(user)) {
                comment.getLikes().remove(user);
            }
            userService.registerUser(user);
        }catch (Exception exception){
            message.clear();
            message.put("Error " , exception.getMessage());
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
        return ResponseEntity.ok(comment);
    }

}
