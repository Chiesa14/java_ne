package com.example.exam.controller;

import com.example.exam.entity.Employee;
import com.example.exam.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<Employee> getEmployeeByCode(@PathVariable UUID code) {
        return employeeService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @PutMapping("/{code}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable UUID code, @RequestBody Employee employee) {
        return employeeService.update(code, employee)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{code}")    
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID code) {
        if (employeeService.delete(code)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 