package com.example.user.controller;

import com.example.user.dto.LoginRequest;
import com.example.user.dto.LoginResponse;
import com.example.user.service.UserService;
import com.example.user.util.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Value("${jwt.expiration}")
    private Long expiration;

    public UserController(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
        
        if (!userService.validatePassword(loginRequest.getUsername(), loginRequest.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        
        String token = jwtUtils.generateToken(claims, userDetails.getUsername());

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiration / 1000)
                .build();

        return ResponseEntity.ok(response);
    }
}
