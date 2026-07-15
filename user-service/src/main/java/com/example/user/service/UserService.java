package com.example.user.service;

import com.example.user.entity.User;
import com.example.user.mapper.UserMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    public boolean validatePassword(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            System.out.println("PASSWORD_VALIDATION user not found: " + username);
            return false;
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        System.out.println("PASSWORD_VALIDATION username=" + username + ", inputPassword=" + password + ", storedHash=" + user.getPassword() + ", matches=" + matches);
        return matches;
    }

    public boolean exists(String username) {
        User user = userMapper.findByUsername(username);
        return user != null;
    }

    public boolean register(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }
        if (exists(username)) {
            return false;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(1);
        user.setNickname(username);
        return userMapper.insert(user) > 0;
    }

    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}