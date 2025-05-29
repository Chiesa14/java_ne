package com.example.exam.service;

import com.example.exam.config.JwtUtil;
import com.example.exam.dto.AuthRequest;
import com.example.exam.dto.AuthResponse;
import com.example.exam.dto.RegisterRequest;
import com.example.exam.dto.RegisterResponse;
import com.example.exam.entity.Employee;
import com.example.exam.entity.Role;
import com.example.exam.exception.AuthException;
import com.example.exam.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Optional<AuthResponse> login(AuthRequest request) {
        return employeeRepository.findByEmail(request.getEmail())
                .filter(employee -> passwordEncoder.matches(request.getPassword(), employee.getPassword()))
                .map(employee -> {
                    AuthResponse response = new AuthResponse();
                    response.setToken(jwtUtil.generateToken(employee.getEmail(), employee.getRoles().iterator().next().name()));
                    response.setEmail(employee.getEmail());
                    response.setRole(employee.getRoles().iterator().next().name());
                    return response;
                });
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        try {
            if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new AuthException("Email is already registered");
            }

            if (request.getPassword().length() < 8) {
                throw new AuthException("Password must be at least 8 characters long");
            }

            var employee = Employee.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .mobile(request.getMobile())
                    .dob(request.getDob())
                    .roles(Set.of(request.getDefaultRole()))
                    .status(Employee.Status.ACTIVE)
                    .build();

            employeeRepository.save(employee);

            return new RegisterResponse("Employee registered successfully", employee.getEmail());
        } catch (Exception e) {
            throw new AuthException("Registration failed: " + e.getMessage());
        }
    }

    public void updatePassword(String email, String newEncodedPassword) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Employee not found"));
        employee.setPassword(newEncodedPassword);
        employeeRepository.save(employee);
    }
}
