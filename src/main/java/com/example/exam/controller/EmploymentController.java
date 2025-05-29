package com.example.exam.controller;

import com.example.exam.dto.EmploymentRequest;
import com.example.exam.entity.Employment;
import com.example.exam.service.EmploymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employments")
@RequiredArgsConstructor
@Tag(name = "Employment Management", description = "APIs for managing employee employment records")
public class EmploymentController {

    private final EmploymentService employmentService;

    @GetMapping("/my")
    @Operation(summary = "Get my employments", description = "Retrieves employment records for the currently logged-in user")
    public List<Employment> getMyEmployments(@AuthenticationPrincipal UserDetails userDetails) {
        return employmentService.findByEmployeeEmail(userDetails.getUsername());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get all employments", description = "Retrieves all employment records. Requires ADMIN or MANAGER role.")
    public List<Employment> getAllEmployments() {
        return employmentService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or @employmentService.isEmployeeOfEmployment(#id, authentication.principal.username)")
    @Operation(summary = "Get employment by ID", description = "Retrieves a specific employment record. Accessible by ADMIN, MANAGER, or the employee themselves.")
    public ResponseEntity<Employment> getEmploymentById(@PathVariable UUID id) {
        return employmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Create employment", description = "Creates a new employment record. Requires ADMIN or MANAGER role.")
    public Employment createEmployment(@Valid @RequestBody EmploymentRequest request) {
        return employmentService.save(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update employment", description = "Updates an existing employment record. Requires ADMIN or MANAGER role.")
    public ResponseEntity<Employment> updateEmployment(
            @PathVariable UUID id,
            @Valid @RequestBody EmploymentRequest request) {
        return employmentService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete employment", description = "Deletes an employment record. Requires ADMIN role.")
    public ResponseEntity<Void> deleteEmployment(@PathVariable UUID id) {
        if (employmentService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 