package com.example.exam.controller;

import com.example.exam.dto.AuthRequest;
import com.example.exam.dto.AuthResponse;
import com.example.exam.dto.RegisterRequest;
import com.example.exam.dto.RegisterResponse;
import com.example.exam.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Optional<AuthResponse> response = authService.login(request);
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}

