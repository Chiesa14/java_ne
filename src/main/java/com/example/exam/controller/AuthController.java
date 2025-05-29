package com.example.exam.controller;

import com.example.exam.dto.AuthRequest;
import com.example.exam.dto.AuthResponse;
import com.example.exam.dto.RegisterRequest;
import com.example.exam.dto.RegisterResponse;
import com.example.exam.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication and authorization")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new employee", description = "Registers a new employee in the system")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Optional<AuthResponse> response = authService.login(request);
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}

