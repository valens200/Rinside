package com.example.demo.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.utils.AlgorithmGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/token")
public class authController {

    @Autowired
    AlgorithmGenerator algorithmGenerator;

    Map<String, String > messages = new HashMap<>();
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
