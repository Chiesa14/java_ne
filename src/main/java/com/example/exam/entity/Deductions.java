package com.example.exam.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = "deductions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deductions {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "code", updatable = false, nullable = false, unique = true)
    private UUID code;

    @NotBlank
    @Size(max = 100)
    private String deductionName;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "100.0", inclusive = true)
    private Double percentage;
} 