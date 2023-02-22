package com.example.demo.controller;
import com.example.demo.models.Message;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    UserService userService;
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message greet(@Payload Message message){
        log.info("hello");
        log.info("message {}", message);
        return   message;
    }
    @GetMapping("/messages")
    public  List<Message> getMessages(){
        return userService.getAllMessages();
    }
    @GetMapping("/messages/{room}")
    public  List<Message> getMessagesByRoom(@PathVariable String room){
        return userService.getMessageByRoom(room);
    }
    @GetMapping("/{room}")
    public List<Message> getMessages(@PathVariable String room){
        return userService.getAllMessages();
    }
    @PostMapping("/save_message")
    public Message postMessage(@RequestBody Message message){
        log.info("message : {}", message);
        return userService.postMessage(message);
    }

}
