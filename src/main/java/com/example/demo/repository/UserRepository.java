package com.example.demo.repository;

import com.example.demo.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Component
public interface UserRepository  extends JpaRepository<AppUser , Integer> {
     public  AppUser findByEmail(String username);
     @Query(
             nativeQuery = true,
             value = "select * from users where user_id in(select following_id from users_followers where follower_id = ?1)"
     )
     public List<AppUser> getFollowings(int followerId);
     @Query(
             nativeQuery = true,
             value = "select * from users where user_id in(select follower_id from users_followers where following_id = ?1)"
     )
     public  List<AppUser> getFollowers(int userId);
     @Modifying
     @Transactional
     @Query(
             nativeQuery = true,
             value = "insert into users_followers(follower_id, following_id) values(?1,?2)"
     )
     public void followUser(int followerId, int followingId);
}
