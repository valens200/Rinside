package com.example.demo.service;

import com.example.demo.models.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceImplTest {


    @Autowired
    UserService userService;

    @Test
    void loadUserByUsername() {
    }

    @Test
    void registerUser() {

    }

    @Test
    void registerRole() {
    }

    @Test
    void getRoleById() {
        assertEquals(ResponseEntity.ok(new Role()), userService.getRoleById(4));
    }

    @Test
    void addRoleTOUser() {
    }

    @Test
    void getByEmail() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getAllRoles() {
    }

    @Test
    void getCommenterId() {
    }

    @Test
    void savePost() {
    }

    @Test
    void getAllMessages() {
    }

    @Test
    void getMessageByRoom() {
    }

    @Test
    void postMessage() {
    }

    @Test
    void saveComment() {
    }

    @Test
    void getPostById() {
    }

    @Test
    void getCommentByCommentId() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getSubUserByEmail() {
    }

    @Test
    void findUserById() {
    }

    @Test
    void getComment() {
    }

    @Test
    void getFollowings() {
    }

    @Test
    void getFollowers() {
    }
}