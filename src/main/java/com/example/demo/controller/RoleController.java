package com.example.demo.controller;


import com.example.demo.models.Role;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {
    @Autowired
    UserService userService;
    @PostMapping("/register")
    public Role registerRole(@RequestBody  Role role){
        log.info("role {}", role);
        return userService.registerRole(role);
    }
}
