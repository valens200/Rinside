package com.example.demo.controller;


import com.example.demo.models.AppUser;
import com.example.demo.models.Role;
import com.example.demo.service.UserService;
import com.example.demo.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController extends com.example.demo.controller.Controller {
    Map<String, String> messages = new HashMap<>();
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AlgorithmGenerator algorithmGenerator;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @GetMapping("/users")
    public Collection<AppUser> getUsers(HttpServletRequest request, HttpServletResponse response) {
        Collection<AppUser> users = userService.getAllUsers();
        response.setStatus(200);
        return users;
    }
    @PostMapping("/user")
    public ResponseEntity<AppUser> getUserByEmail( @RequestBody RetrieveClass retrieveClass ,  HttpServletRequest request, HttpServletResponse response) throws IOException {
        AppUser user1 = userService.getByEmail(retrieveClass.getEmail());
        return ResponseEntity.ok(user1);
    }
    @GetMapping("/roles")
    public Collection<Role> getAllRoles(HttpServletResponse response, HttpServletRequest request) {
        Collection<Role> roles = userService.getAllRoles();
        return roles;
    }
    @PostMapping("/follow/{follower}/{following}")
    public  ResponseEntity<?> follow(@PathVariable int follower, @PathVariable int following) throws InterruptedException {
        AppUser user = userService.getUserById(follower).get();
        AppUser user1 = userService.getUserById(following).get();
        if(user.getEmail() == null || user1.getEmail() == null || user.getEmail() == ""){
            return  ResponseEntity.badRequest().body(messages.put("Error_message", "Please provide valid id's"));
        }
        System.out.println(follower + " " + following);
       if(userService.followUser(follower,following))
              return ResponseEntity.ok().body(messages.put("message", "Successfully followed user"));
        return  ResponseEntity.badRequest().body(messages.put("Error_message", "We are enable to follow the user"));

    }

    @PutMapping("/adminRole/{email}")
    public AppUser addRolToUser( @PathVariable String email){
        return userService.addRoleTOUser(email);
    }
    @GetMapping("/following/{followerId}")
    public  ResponseEntity<List<AppUser>> getFollowings( @PathVariable  int followerId){
        return ResponseEntity.ok(userService.getFollowings(followerId));

    }
    @GetMapping("/followers/{userId}")
    public  ResponseEntity<List<AppUser>> getFollowers( @PathVariable  int userId){
       return ResponseEntity.ok(userService.getFollowers(userId));

    }
    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById( @PathVariable  int id){
        ResponseEntity<AppUser>  appUserResponseEntity = null;
        try{
            appUserResponseEntity = ResponseEntity.ok(userService.getUserById(id).get());

        }catch (Exception exception){
            log.error("error {}", exception.getMessage());
        }
        return appUserResponseEntity;
    }



}
