package com.example.exam.controller;

import com.example.exam.entity.PaySlip;
import com.example.exam.service.PaySlipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payslips")
@RequiredArgsConstructor
public class PaySlipController {

    private final PaySlipService paySlipService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<PaySlip>> generatePayroll(
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(paySlipService.generatePayroll(month, year));
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approvePayroll(
            @RequestParam int month,
            @RequestParam int year) {
        paySlipService.approvePayroll(month, year);
        return ResponseEntity.ok("Payroll approved successfully");
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approvePaySlip(@PathVariable Long id) {
        try {
            paySlipService.approvePaySlip(id);
            return ResponseEntity.ok("PaySlip approved successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }http://localhost:8080/api/payslips/employee/2388ad82-9ee5-4e63-8318-91914068ece4
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<PaySlip>> getAllPaySlips(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        if (month != null && year != null) {
            return ResponseEntity.ok(paySlipService.getPaySlipsByMonthAndYear(month, year));
        }
        return ResponseEntity.ok(paySlipService.getAllPaySlips());
    }

    @GetMapping("/employee/{employeeCode}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#employeeCode)")
    public ResponseEntity<List<PaySlip>> getPaySlipsByEmployeeCode(
            @PathVariable UUID employeeCode) {
        return ResponseEntity.ok(paySlipService.getPaySlipsByEmployeeCode(employeeCode));
    }
} 