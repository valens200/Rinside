package com.example.demo.repository;

import com.example.demo.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

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
}
