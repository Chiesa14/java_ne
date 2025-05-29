package com.example.exam.controller;

import com.example.exam.entity.Deductions;
import com.example.exam.service.DeductionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deductions")
@Tag(name = "Deductions Management", description = "APIs for managing salary deductions and taxes")
public class DeductionsController {

    @Autowired
    private DeductionsService deductionsService;

    @GetMapping
    @Operation(summary = "Get all deductions", description = "Retrieves a list of all deductions")
    public List<Deductions> getAllDeductions() {
        return deductionsService.findAll();
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get deduction by code", description = "Retrieves a deduction by its code. Requires ADMIN or MANAGER role.")
    public ResponseEntity<Deductions> getDeductionByCode(@PathVariable UUID code) {
        return deductionsService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create deduction", description = "Creates a new deduction. Requires ADMIN role.")
    public Deductions createDeduction(@RequestBody Deductions deduction) {
        return deductionsService.save(deduction);
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update deduction", description = "Updates an existing deduction. Requires ADMIN role.")
    public ResponseEntity<Deductions> updateDeduction(@PathVariable UUID code, @RequestBody Deductions deduction) {
        return deductionsService.update(code, deduction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete deduction", description = "Deletes a deduction. Requires ADMIN role.")
    public ResponseEntity<Void> deleteDeduction(@PathVariable UUID code) {
        if (deductionsService.delete(code)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
