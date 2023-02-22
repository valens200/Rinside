package com.example.demo.repository;

import com.example.demo.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


@Component
public interface PostRepository extends JpaRepository<Post, Long> {

}
