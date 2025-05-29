package com.example.exam.service;

import com.example.exam.dto.OtpRequest;
import com.example.exam.dto.OtpVerificationRequest;
import com.example.exam.dto.PasswordResetRequest;
import com.example.exam.entity.Employee;
import com.example.exam.entity.Otp;
import com.example.exam.repository.EmployeeRepository;
import com.example.exam.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmailService emailService;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 5;

    public String generateAndSendOtp(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));

        // Generate OTP
        String otp = generateOtp();

        // Save OTP
        Otp otpEntity = new Otp();
        otpEntity.setEmployee(employee);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
        otpEntity.setUsed(false);
        otpRepository.save(otpEntity);

        // Send OTP via email
        emailService.sendOtpEmail(email, otp);

        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));

        Otp otpEntity = otpRepository.findByEmployeeAndOtp(employee, otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if (otpEntity.isUsed()) {
            throw new RuntimeException("OTP has already been used");
        }

        return true;
    }

    private String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public boolean verifyOtp(OtpVerificationRequest request) {
        return otpRepository.findByEmployee_EmailAndOtpAndUsedFalse(request.getEmail(), request.getOtp())
                .filter(token -> token.getExpiryTime().isAfter(LocalDateTime.now()))
                .map(token -> {
                    token.setUsed(true);
                    otpRepository.save(token);
                    return true;
                })
                .orElse(false);
    }

    public void resetPassword(PasswordResetRequest request) {
        boolean valid = verifyOtp(OtpVerificationRequest.builder()
                .email(request.getEmail())
                .otp(request.getOtp())
                .build());

        if (!valid) throw new RuntimeException("Invalid or expired OTP");

        // Implementation of resetPassword method
    }
}
