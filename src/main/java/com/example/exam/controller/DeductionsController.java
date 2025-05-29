package com.example.exam.controller;

import com.example.exam.entity.Deductions;
import com.example.exam.service.DeductionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deductions")
public class DeductionsController {

    @Autowired
    private DeductionsService deductionsService;

    @GetMapping
    public List<Deductions> getAllDeductions() {
        return deductionsService.findAll();
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Deductions> getDeductionByCode(@PathVariable UUID code) {
        return deductionsService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Deductions createDeduction(@RequestBody Deductions deduction) {
        return deductionsService.save(deduction);
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Deductions> updateDeduction(@PathVariable UUID code, @RequestBody Deductions deduction) {
        return deductionsService.update(code, deduction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDeduction(@PathVariable UUID code) {
        if (deductionsService.delete(code)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
