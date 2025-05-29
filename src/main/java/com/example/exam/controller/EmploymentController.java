package com.example.exam.controller;

import com.example.exam.entity.Employment;
import com.example.exam.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/employment")
public class EmploymentController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/{employeeCode}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Employment> addEmploymentDetails(@PathVariable UUID employeeCode, @RequestBody Employment employment) {
        try {
            Employment savedEmployment = employeeService.addEmploymentDetails(employeeCode, employment);
            return ResponseEntity.ok(savedEmployment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 