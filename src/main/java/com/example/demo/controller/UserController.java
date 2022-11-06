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

public class UserController {

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

    @PostMapping("/main")
    public String wellcome() {
        return "hello user";
    }

    @GetMapping("/")
    public String greeting() {
        System.out.println("hello");
        return "hello user";
    }
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
    @PostMapping("/register")
    public AppUser registerUser(@RequestBody AppUser user, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String inputs[] = new String[]{user.getPassword(), user.getEmail(), user.getUserName()};
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == "" || inputs[i] == null) {
                messages.put("status", "failed");
                messages.put("error_message", "Please fill out all the fields are required");
                response.setStatus(400);
                new ObjectMapper().writeValue(response.getOutputStream(), messages);
                return null;
            }
        }
        AppUser isUserAvailable = userService.getByEmail(user.getEmail());
        if (isUserAvailable != null) {
            messages.clear();
            response.setStatus(400);
            messages.put("error_message", "Account with that email already registered please try another");
            new ObjectMapper().writeValue(response.getOutputStream(), messages);
            return null;
        }
        messages.clear();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("active");
        user.getFollowers().add(new SubUser(user.getUserName(), user.getEmail(), user.getProfilePicture()));
        AppUser registeredUser = userService.registerUser(user);

        Collection<Role> roles = new ArrayList<>();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });

        SignUpTokenGenerator signUpTokenGenerator = new SignUpTokenGenerator(registeredUser, roles, authorities, algorithmGenerator);
        String access_token = signUpTokenGenerator.getAccessTOken(response, request);
        String refresh_token = signUpTokenGenerator.getRefreshToken(request, response);
        messages.put("acccess_Token", access_token);
        messages.put("refresh_Token", refresh_token);
        messages.put("username", registeredUser.getUserName());
        messages.put("email", registeredUser.getEmail());
        new ObjectMapper().writeValue(response.getOutputStream(), messages);
        return null;
    }

    @PostMapping("/login")

    public void  loginUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String inputs[] = new String[]{loginRequest.getEmail(), loginRequest.getPassword()};
            for (int i = 0; i < inputs.length; i++) {
                if (inputs[i] == "" || inputs[i] == null) {
                    response.setStatus(400);
                    messages.put("error_message", "Invalid inputs please fill out all the fields");
                    new ObjectMapper().writeValue(response.getOutputStream(), messages);
                    return;
                }

            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            TokenGenerator tokenGenerator1 = new TokenGenerator(user, algorithmGenerator);
            String access_token = tokenGenerator1.getAccessToken(response, request);
            String refresh_token = tokenGenerator1.getfreshToken(request, response);

            AppUser availableUser = userService.getByEmail(loginRequest.getEmail());
            availableUser.setStatus("active");
            userService.registerUser(availableUser);
            messages.put("access_token", access_token);
            messages.put("refresh_token", refresh_token);
            messages.put("message", "Your logged in successfully");
            messages.put("username", availableUser.getUserName());
            messages.put("email", availableUser.getEmail());
            new ObjectMapper().writeValue(response.getOutputStream(), messages);
        } catch (Exception exception) {
            response.setStatus(400  );
            messages.put("error_message", exception.getMessage());
            messages.put("message", "Invalid email or password");
            new ObjectMapper().writeValue(response.getOutputStream(), messages);

        }

    }

    @PostMapping("/addadminRole/{email}")
    public AppUser addRolToUser( @PathVariable String email){
        return userService.addRoleTOUser(email);
    }


}
