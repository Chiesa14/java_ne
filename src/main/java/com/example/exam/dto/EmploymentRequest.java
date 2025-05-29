package com.example.exam.dto;

import com.example.exam.entity.Employment;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmploymentRequest {
    @NotNull
    private UUID employeeCode;

    @NotBlank
    @Size(max = 100)
    private String department;

    @NotBlank
    @Size(max = 100)
    private String position;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal baseSalary;

    @NotNull
    private Employment.Status status;

    @NotNull
    private LocalDate joiningDate;
} 