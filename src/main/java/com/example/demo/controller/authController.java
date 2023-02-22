package com.example.demo.controller;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.models.AppUser;
import com.example.demo.models.Role;
import com.example.demo.service.UserService;
import com.example.demo.utils.AlgorithmGenerator;
import com.example.demo.utils.LoginRequest;
import com.example.demo.utils.SignUpTokenGenerator;
import com.example.demo.utils.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/auth")
public class authController {
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


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AppUser user, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String inputs[] = new String[]{user.getPassword(), user.getEmail(), user.getUserName()};
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == "" || inputs[i] == null) {
                messages.put("status", "failed");
                messages.put("error_message", "Please fill out all the fields are required");
                response.setStatus(400);
                new ObjectMapper().writeValue(response.getOutputStream(), messages);
                return ResponseEntity.ok(messages);
            }
        }
        AppUser isUserAvailable = userService.findByEmail(user.getEmail());
        if (isUserAvailable != null) {
            messages.clear();
            response.setStatus(400);
            messages.put("error_message", "Account with that email already registered please try another");
            new ObjectMapper().writeValue(response.getOutputStream(), messages);
            return ResponseEntity.ok(messages);
        }
        messages.clear();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("active");
        AppUser registeredUser = userService.registerUser(user);

        Collection<Role> roles = new ArrayList<>();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });

        SignUpTokenGenerator signUpTokenGenerator = new SignUpTokenGenerator(registeredUser, roles, authorities, algorithmGenerator);
        String access_token = signUpTokenGenerator.getAccessTOken(response, request);
        String refresh_token = signUpTokenGenerator.getRefreshToken(request, response);

        registeredUser.setAccessToken(access_token);
        registeredUser.setRefreshToken(refresh_token);
        new ObjectMapper().writeValue(response.getOutputStream(), messages);
        response.setStatus(200);
        return ResponseEntity.ok(registeredUser);
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
            System.out.println("user" + user);

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
            response.setStatus(200);
        } catch (Exception exception) {
            response.setStatus(400  );
            messages.put("error_message", exception.getMessage());
            messages.put("message", "Invalid email or password");
            new ObjectMapper().writeValue(response.getOutputStream(), messages);
        }
    }
    @PostMapping("/validate/{token}")
    public ResponseEntity validateRoken (@PathVariable  String token, HttpServletResponse response, HttpServletRequest request){
        log.info("token {}", token);
        try{
            JWTVerifier jwtVerifier = JWT.require(algorithmGenerator.getAlgorithm()).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            log.info(decodedJWT.getAlgorithm());
        }catch(Exception exception){
            messages.put("error_message", exception.getMessage());
            return ResponseEntity.badRequest().body(messages);
        }
        messages.put("message ", "Token is valid");
        response.setStatus(200);
        return ResponseEntity.ok(messages);

    }
}
