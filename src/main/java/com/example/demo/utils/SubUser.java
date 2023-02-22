package com.example.demo.utils;


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
@Table(name = "subUsers")
public class SubUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "email")
    private String email;
    @Column(name = "userName")
    private String userName;
    @Column(name = "profileImage")
    private String profileImage;

    public SubUser(String email, String userName, String profilePicture) {
        this.userName = userName;
        this.email = email;
        this.profileImage = profilePicture;
    }
}
