package com.example.demo.service;

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

    @Override
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
    public AppUser registerUser(AppUser user) {
        return userRepository.save(user);
    }

    @Override
    public Role registerRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public AppUser addRoleTOUser(String email) {
        AppUser user  = userRepository.findByEmail(email);
        Role role  = roleRepository.findByRoleName("ADMIN");
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public AppUser getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Collection<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Collection<Role> getAllRoles() {
        return roleRepository.findAll();
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
    public Optional<Post> getPostById(long id) {
        return postRepository.findById(id);
    }


    @Override
    public Comment getCommentByCommentId(long id) {
        return commentRepository.findByCommentId(id);
    }

    @Override
    public SubUser getSubUserByEmail(String email) {
        return subUserRepository.findByEmail(email);
    }

    @Override
    public SubUser findUserById(int id) {
        return subUserRepository.getById(id);
    }

}
