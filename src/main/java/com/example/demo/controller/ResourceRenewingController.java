package com.example.demo.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.models.Role;
import com.example.demo.utils.AlgorithmGenerator;
import com.example.demo.utils.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@Controller
@Slf4j
@RequiredArgsConstructor
public class ResourceRenewingController {
    Map<String, String> messages = new HashMap<>();
    @Autowired
    AlgorithmGenerator algorithmGenerator;

    @Autowired
    UserDetailsService userDetailsService;

    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
           String authorization = request.getHeader("Authorization");
           if(authorization != null && authorization.startsWith("Bearer ")){
               try{
                   String token = authorization.substring("Bearer ".length());
                   JWTVerifier verifier = JWT.require(algorithmGenerator.getAlgorithm()).build();
                   DecodedJWT decodedJWT = verifier.verify(token);
                   String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                   Collection<GrantedAuthority> authorities = new ArrayList<>();
                   Arrays.stream(roles).forEach(role -> {
                       authorities.add(new SimpleGrantedAuthority(role));
                   });
                   UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());
                   TokenGenerator tokenGenerator1 = new TokenGenerator(userDetails, algorithmGenerator);
                   String accessToken = tokenGenerator1.getAccessToken(response, request);
                   String refresh_token = tokenGenerator1.getfreshToken(request, response);
                   messages.put("access_token", accessToken);
                   messages.put("refresh_token", refresh_token);
                   new ObjectMapper().writeValue(response.getOutputStream(), messages);
               }catch(Exception exception){
                   log.info("excepton {}", exception.getMessage());

               }
           }
    }


}
