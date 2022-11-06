package com.example.demo.repository;

import com.example.demo.utils.SubUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubUserRepository  extends JpaRepository<SubUser, Integer> {
    public SubUser findByEmail(String email);
}
