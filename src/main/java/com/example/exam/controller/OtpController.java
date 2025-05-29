package com.example.exam.controller;

import com.example.exam.dto.OtpRequest;
import com.example.exam.dto.OtpVerificationRequest;
import com.example.exam.dto.PasswordResetRequest;
import com.example.exam.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateOtp(@RequestBody OtpRequest request) {
        try {
            String otp = otpService.generateAndSendOtp(request.getEmail());
            return ResponseEntity.ok("OTP has been sent to your email");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        try {
            boolean isValid = otpService.validateOtp(request.getEmail(), request.getOtp());
            if (isValid) {
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                return ResponseEntity.badRequest().body("Invalid OTP");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
        otpService.resetPassword(request);
            return ResponseEntity.ok("Password has been reset successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
