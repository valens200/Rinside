package com.example.demo.controller;


import com.example.demo.models.Role;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles(){
        return ResponseEntity.ok(userService.getAllRoles());
    }
    @GetMapping("/{id}")
    public  ResponseEntity<Role> getRole(int id){
        ResponseEntity<Role> roleResponseEntity = null;
        try{
            roleResponseEntity = ResponseEntity.ok(userService.getRoleById(id).get());
        }catch (Exception exception){
            log.error("error {}" , exception.getMessage());
        }
        return roleResponseEntity;
    }
}

