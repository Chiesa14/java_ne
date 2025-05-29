package com.example.exam.controller;

import com.example.exam.entity.Employee;
import com.example.exam.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees. Requires ADMIN or MANAGER role.")
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or @securityService.isCurrentUser(#code)")
    @Operation(summary = "Get employee by code", description = "Retrieves an employee by their code. Accessible by ADMIN, MANAGER, or the employee themselves.")
    public ResponseEntity<Employee> getEmployeeByCode(@PathVariable UUID code) {
        return employeeService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create new employee", description = "Creates a new employee. Requires MANAGER role.")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('MANAGER') or @securityService.isCurrentUser(#code)")
    @Operation(summary = "Update employee", description = "Updates an existing employee. Accessible by MANAGER or the employee themselves.")
    public ResponseEntity<Employee> updateEmployee(@PathVariable UUID code, @RequestBody Employee employee) {
        return employeeService.update(code, employee)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{code}")    
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete employee", description = "Deletes an employee. Requires ADMIN role.")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID code) {
        if (employeeService.delete(code)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 