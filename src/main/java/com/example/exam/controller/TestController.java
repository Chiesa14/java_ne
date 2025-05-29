package com.example.exam.controller;

import com.example.exam.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final EmailService emailService;

    @GetMapping("/email")
    public String testEmail() {
        try {
            emailService.sendSalaryNotification(
                "remychiesa14@gmail.com",
                "Test User",
                3,
                2024,
                "1000000",
                "TEST001"
            );
            return "Test email sent successfully";
        } catch (Exception e) {
            return "Failed to send test email: " + e.getMessage();
        }
    }
} 