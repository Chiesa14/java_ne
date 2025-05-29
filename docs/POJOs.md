# List of POJOs (Plain Old Java Objects)

## Domain Models

### Employee
```java
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### Employment
```java
@Entity
@Table(name = "employments")
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;
    
    @ManyToOne
    @JoinColumn(name = "employee_code", nullable = false)
    private Employee employee;
    
    @Column(nullable = false)
    private String department;
    
    @Column(nullable = false)
    private String position;
    
    @Column(nullable = false)
    private BigDecimal baseSalary;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    private LocalDate joiningDate;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### Payslip
```java
@Entity
@Table(name = "payslips")
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;
    
    @ManyToOne
    @JoinColumn(name = "employee_code", nullable = false)
    private Employee employee;
    
    @ManyToOne
    @JoinColumn(name = "employment_code", nullable = false)
    private Employment employment;
    
    @Column(nullable = false)
    private LocalDate month;
    
    @Column(nullable = false)
    private BigDecimal grossSalary;
    
    @Column(nullable = false)
    private BigDecimal netSalary;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### Deduction
```java
@Entity
@Table(name = "deductions")
public class Deduction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;
    
    @ManyToOne
    @JoinColumn(name = "employee_code", nullable = false)
    private Employee employee;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    private String description;
    
    @Column(nullable = false)
    private LocalDate effectiveDate;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

## DTOs (Data Transfer Objects)

### EmployeeDTO
```java
public class EmployeeDTO {
    private UUID code;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private Role role;
}
```

### EmploymentDTO
```java
public class EmploymentDTO {
    private UUID code;
    private UUID employeeCode;
    private String department;
    private String position;
    private BigDecimal baseSalary;
    private Status status;
    private LocalDate joiningDate;
}
```

### PayslipDTO
```java
public class PayslipDTO {
    private UUID code;
    private UUID employeeCode;
    private UUID employmentCode;
    private LocalDate month;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    private Status status;
    private List<DeductionDTO> deductions;
}
```

### DeductionDTO
```java
public class DeductionDTO {
    private UUID code;
    private UUID employeeCode;
    private String type;
    private BigDecimal amount;
    private String description;
    private LocalDate effectiveDate;
    private Status status;
}
```

## Enums

### Role
```java
public enum Role {
    ADMIN,
    MANAGER,
    EMPLOYEE
}
```

### Status
```java
public enum Status {
    ACTIVE,
    INACTIVE,
    PENDING,
    APPROVED,
    REJECTED
}
```

## Request/Response Objects

### LoginRequest
```java
public class LoginRequest {
    private String email;
    private String password;
}
```

### LoginResponse
```java
public class LoginResponse {
    private String token;
    private String refreshToken;
    private UserDTO user;
}
```

### PasswordResetRequest
```java
public class PasswordResetRequest {
    private String email;
}
```

### PasswordChangeRequest
```java
public class PasswordChangeRequest {
    private String currentPassword;
    private String newPassword;
}
```

## Common Classes

### BaseEntity
```java
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### ApiResponse
```java
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
}
```

### PaginatedResponse
```java
public class PaginatedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
} 