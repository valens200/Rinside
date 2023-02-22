package com.example.demo.service;

import com.example.demo.exceptions.NotsavedException;
import com.example.demo.exceptions.SaveUSerException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.*;
import com.example.demo.repository.*;
import com.example.demo.utils.SubUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    SubUserRepository subUserRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{

            AppUser user = userRepository.findByEmail(username);
            Collection<Role> roles = user.getRoles();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.stream().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            });
            return new User(user.getEmail(), user.getPassword(), authorities);
        }catch(Exception exception){
            log.error("error : {}", exception.getMessage());
            return null;
        }
    }

    @Override
    public AppUser registerUser(AppUser user)  throws SaveUSerException{
        user.setStatus("active");
        AppUser savedUSer = userRepository.save(user);
        if(savedUSer.equals(null)){
            throw  new SaveUSerException("Enable to save the user");
        }else{
            return savedUSer;
        }
    }

    @Override
    public Role registerRole(Role role) throws NotsavedException {
        Role savedRole = roleRepository.save(role);
        if(role == null)
            throw new NotsavedException("Enable to save the role");
        return savedRole;
    }
    @Override
    public Optional<Role> getRoleById(int id) throws  NotFoundException{
        Optional<Role> role = roleRepository.findById(id);
        if(role == null){
            throw  new NotFoundException("role with that id not found");
        }
        return role;
    }

    @Override
    public AppUser addRoleTOUser(String email) throws  NotFoundException {
        AppUser user  = userRepository.findByEmail(email);
        Role role  = roleRepository.findByRoleName("ADMIN");
        if(role == null)
             new NotFoundException("That role with   not found" );
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public AppUser getByEmail(String email)  throws NotFoundException {
        AppUser user = userRepository.findByEmail(email);
        if(user == null)
               throw new NotFoundException("user with that email not found");
        return user;
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public int getCommenterId(long commentId) {
        return commentRepository.getCommenterId(commentId);
    }

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> getMessageByRoom(String room) {
        return messageRepository.findByRoom(room);
    }

    @Override
    public Message postMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Post> getPostById(long id) throws  NotFoundException {
        Optional<Post> post =  postRepository.findById(id);
        if(post.get().getEmail() == null || post.get().getEmail() == ""){
            throw  new NotFoundException("The post with that id not found");
        }else{
            return post;
        }
    }
    @Override
    public Comment getCommentByCommentId(long id) throws NotFoundException {
        Comment comment = commentRepository.findByCommentId(id);
        if(comment == null)
            throw  new NotFoundException("Comment with that id not found");
        return comment;
    }
    @Override
    public Optional<AppUser> getUserById(int id)  throws NotFoundException {
        AppUser user = userRepository.findById(id).get();
        if(user == null)
            throw new NotFoundException("No user with that email in database");
        return userRepository.findById(id);
    }
    @Override
    public SubUser getSubUserByEmail(String email) throws NotFoundException {
        SubUser subUser =  subUserRepository.findByEmail(email);
        if(subUser == null)
            throw  new NotFoundException("User with that email not found");
        return subUser;
    }
    @Override
    public AppUser findUserById(int id)  throws  NotFoundException{
        AppUser user =  userRepository.findById(id).get();
        if(user == null)
            throw new NotFoundException("User with that email not found");
        return user;
    }
    @Override
    public Comment getComment(long id) throws  NotFoundException{
        Comment comment = commentRepository.getComment(id);
        if(comment == null){
            throw  new NotFoundException("comment with that id is not found");
        }
        return  comment;
    }
    @Override
    public List<AppUser> getFollowings(int followerId) {
        return userRepository.getFollowings(followerId);
    }

    @Override
    public List<AppUser> getFollowers(int userId) {
        return userRepository.getFollowers(userId);
    }

    @Override
    public boolean followUser(int followerId, int followingId) {
        boolean result = true;
        try{
           userRepository.followUser(followerId, followingId);
        }catch (Exception exception){
            result = false;
            System.out.println("Error : " + exception.getMessage());
        }
        return result;
    }

}
