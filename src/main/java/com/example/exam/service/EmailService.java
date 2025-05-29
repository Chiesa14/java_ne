package com.example.exam.service;

import org.springframework.mail.SimpleMailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + ". It will expire in 5 minutes.");
        mailSender.send(message);
    }

    public void sendSalaryNotification(String to, String firstName, int month, int year, String institution, 
                                     String amount, String employeeId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Salary Payment Notification");
        
        String emailContent = String.format(
            "Dear %s, your salary for %d/%d from %s amounting to %s has been credited to your account %s successfully.",
            firstName, month, year, institution, amount, employeeId
        );
        
        helper.setText(emailContent);
        mailSender.send(message);
    }
}
