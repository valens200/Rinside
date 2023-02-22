package com.example.demo.repository;

import com.example.demo.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
@Component
public interface CommentRepository  extends JpaRepository<Comment, Long> {
    public  Comment findByCommentId(long id);
    @Query(
            nativeQuery = true,
            value = "select * from comments where commenter_id =?1"
    )
    public Comment getComment(long id);
    @Query(
            nativeQuery = true,
            value = "select commenter_id from comments where comment_id =?1"
    )
    public  int getCommenterId(long commentId);

}
