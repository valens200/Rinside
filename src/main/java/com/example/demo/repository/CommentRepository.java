package com.example.demo.repository;

import com.example.demo.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
    public  Comment findByCommentId(long id);

}
