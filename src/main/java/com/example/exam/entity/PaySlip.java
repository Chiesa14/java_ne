package com.example.exam.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pay_slip")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaySlip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code")
    private UUID employeeCode;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "house_amount")
    private BigDecimal houseAmount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "transport_amount")
    private BigDecimal transportAmount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "employee_taxed_amount")
    private BigDecimal employeeTaxedAmount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "pension_amount")
    private BigDecimal pensionAmount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "medical_insurance_amount")
    private BigDecimal medicalInsuranceAmount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "other_taxed_amount")
    private BigDecimal otherTaxedAmount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "gross_salary")
    private BigDecimal grossSalary;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "net_salary")
    private BigDecimal netSalary;

    @NotNull
    @Min(1)
    @Max(12)
    @Column(name = "month")
    private Integer month;

    @NotNull
    @Min(1900)
    @Column(name = "year")
    private Integer year;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status")
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Status {
        PAID,
        PENDING
    }
} 