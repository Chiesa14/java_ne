package com.example.exam.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final JdbcTemplate jdbcTemplate;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        log.info("Initializing EmailNotificationService...");
    }

    public void handleNotification(String payload) {
        try {
            log.info("Received notification payload: {}", payload);
            // Parse the JSON payload
            JsonNode jsonNode = objectMapper.readTree(payload);
            
            String to = jsonNode.get("to").asText();
            String firstName = jsonNode.get("firstName").asText();
            int month = jsonNode.get("month").asInt();
            int year = jsonNode.get("year").asInt();
            String amount = jsonNode.get("amount").asText();
            String employeeCode = jsonNode.get("employeeCode").asText();

            log.info("Sending salary notification to: {}", to);
            emailService.sendSalaryNotification(to, firstName, month, year, amount, employeeCode);
            log.info("Salary notification sent successfully");
        } catch (Exception e) {
            log.error("Error handling email notification", e);
        }
    }
} 