package com.example.demo.controller;


import com.example.demo.models.Message;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    UserService userService;
    @MessageMapping("/message")
    @SendTo("/chartroom/public")
    public Message greet(@Payload Message message){
        log.info("hello");
       log.info("message {}", message);
        return message;
    }
    @GetMapping("/{room}")
    public List<Message> getMessages(@PathVariable String room){
        return userService.getAllMessages();

    }
    @GetMapping
    public  String helloUSer(){
        return "hello users ";
    }
    @PostMapping("/post")
    public Message postMessage(@RequestBody Message message){
        return userService.postMessage(message);
    }


}
