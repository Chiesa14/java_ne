package com.example.exam.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${institution.name}")
    private String institutionName;

    public void sendOtpEmail(String to, String otp) {
        log.info("Sending OTP email to: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + ". It will expire in 5 minutes.");
        try {
            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", to, e);
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public void sendSalaryNotification(
            String to,
            String firstName,
            int month,
            int year,
            String amount,
            String employeeCode) {
        log.info("Preparing salary notification for employee: {} ({})", firstName, employeeCode);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Salary Payment Notification");
            
            String content = String.format(
                "Dear %s,\n\n" +
                "Your salary for %d/%d from %s amounting to %s has been credited to your account %s successfully.\n\n" +
                "Thank you for your service.\n\n" +
                "Best regards,\n%s",
                firstName,
                month,
                year,
                institutionName,
                amount,
                employeeCode,
                institutionName
            );
            
            message.setText(content);
            log.info("Sending salary notification to: {}", to);
            mailSender.send(message);
            log.info("Salary notification sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send salary notification to: {}", to, e);
            throw new RuntimeException("Failed to send salary notification", e);
        }
    }
}
