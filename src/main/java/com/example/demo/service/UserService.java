package com.example.demo.service;

import com.example.demo.models.*;
import com.example.demo.utils.SubUser;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public AppUser registerUser(AppUser user);
    public Role registerRole(Role role);
    public  AppUser addRoleTOUser(String email);
    public  AppUser getByEmail(String email);
    public  AppUser findByEmail(String email);
    public AppUser findUserById(int id);
    public Collection<AppUser> getAllUsers();
    public  List<Role> getAllRoles();
    public  int getCommenterId(long commentId);
    public Optional<Role> getRoleById(int id);
    public Post savePost(Post post);
    public List<Message> getAllMessages();
    public List<Message> getMessageByRoom(String room);
    public Message postMessage(Message message);
    public Comment saveComment(Comment comment);
    public Optional<Post> getPostById(long id);
    public  Comment getCommentByCommentId(long id);
    public Optional<AppUser> getUserById(int id);
    public SubUser getSubUserByEmail(String email);
    public Comment getComment(long id);
    public List<AppUser> getFollowings(int followerId);
    public  List<AppUser> getFollowers(int userId);

}
