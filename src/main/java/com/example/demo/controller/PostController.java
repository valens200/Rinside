package com.example.demo.controller;
import com.example.demo.models.AppUser;
import com.example.demo.models.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@Slf4j
@RequestMapping("/post")
@CrossOrigin("*")
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
    @PostMapping("/add_post")
    public ResponseEntity<?> addPost(@RequestBody Post post, HttpServletRequest request, HttpServletResponse response) throws IOException {
        AppUser user = userRepository.findByEmail(post.getEmail());
        if (user == null) {
            messages.put("error_message", "invalid inputs");
            new ObjectMapper().writeValue(response.getOutputStream(), messages);
        }
        post.setPosterId(user.getUserId());
        userService.savePost(post);
        return ResponseEntity.ok().body(post);
        }
        @PostMapping("/like/{postId}/{id}")
    public ResponseEntity<?> likePost(@PathVariable long postId, @PathVariable int id) {
            System.out.println(postId + " " + id);
            Post post = null;
            AppUser user = null;
            Post updatedPost = new Post();
            try {
                post = userService.getPostById(postId).get();
                if(post.getEmail() == null || post == null || post.getEmail() == ""){
                    return  ResponseEntity.badRequest().body(new HashMap<String, String>().put("message","Invalid inputs ! please enter the right id's"));
                }
                user = userService.findUserById(id);
                post.getLikes().add(user);
                updatedPost = userService.savePost(post);
            } catch (Exception exception) {
                System.out.println("some thing went wrong "  + exception);
                System.out.println(exception.getMessage());
            }
            return ResponseEntity.ok(post);
        }
    @PostMapping("/dislike/{postId}/{id}")
    public ResponseEntity<Post> dislikePost(@PathVariable int postId, @PathVariable int id){
        Post post = null;
        AppUser user = null;
        try{
            post = userService.getPostById(postId).get();
            user = userService.findUserById(id);
            post.getLikes().remove(user);
            userService.savePost(post);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return ResponseEntity.ok(post);
    }

}