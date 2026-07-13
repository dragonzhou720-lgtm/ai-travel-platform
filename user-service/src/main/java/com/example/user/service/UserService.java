package com.example.user.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {

    private final Map<String, String> userStore = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        userStore.put("admin", passwordEncoder.encode("123456"));
        userStore.put("user", passwordEncoder.encode("123456"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userStore.containsKey(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return new org.springframework.security.core.userdetails.User(
                username,
                userStore.get(username),
                authorities
        );
    }

    public boolean validatePassword(String username, String password) {
        String encodedPassword = userStore.get(username);
        return encodedPassword != null && passwordEncoder.matches(password, encodedPassword);
    }

    public boolean exists(String username) {
        return userStore.containsKey(username);
    }

    public boolean register(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }
        if (userStore.containsKey(username)) {
            return false;
        }
        userStore.put(username, passwordEncoder.encode(password));
        return true;
    }
}
