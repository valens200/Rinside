package com.example.demo.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.utils.AlgorithmGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    AlgorithmGenerator algorithmGenerator;

    Map<String, String> messages = new HashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if( request.getServletPath().equals("/auth/login") || request.getServletPath().equals("/post/posts") || request.getServletPath().equals("/auth/register") || request.getServletPath().equals("/user") ||  request.getServletPath().equals("/token/refresh")){
            filterChain.doFilter(request, response);
            return;
        }else{
            String authorization = request.getHeader("Authorization");
                try{
                    if(authorization != null && authorization.startsWith("Bearer ")) {
                    String token = authorization.substring("Bearer ".length());
                    JWTVerifier jwtVerifier = JWT.require(algorithmGenerator.getAlgorithm()).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(token);
                    String roles[] = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    Arrays.stream(roles).forEach(role ->{
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, authorities));
                    }
                }catch(Exception exception) {
                    response.setStatus(403);
                    messages.put("error_message", exception.getMessage());
                    new ObjectMapper().writeValue(response.getOutputStream(), messages);
                    log.info("error {}", exception.getMessage());

                }

            filterChain.doFilter(request, response);
                return;
        }

    }
}








