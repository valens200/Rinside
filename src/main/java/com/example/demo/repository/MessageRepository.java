package com.example.demo.repository;


import com.example.demo.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository  extends JpaRepository<Message, Integer> {
        List<Message> findByRoom(String room);
}
