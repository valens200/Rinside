package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   private  int id;
    @Column(name = "text")
   private  String text;
    @Column(name = "senderName")
   private  String senderName;
    @Column(name = "receiverName")
    private  String receiverName;
    @Column(name = "room")
   private String room;

}
