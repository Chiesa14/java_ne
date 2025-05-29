# Database ERD Schema

## Entity Relationships

```mermaid
erDiagram
    Employee ||--o{ Employment : has
    Employee ||--o{ Payslip : receives
    Employee ||--o{ Deduction : has
    Employment ||--o{ Payslip : generates
    Deduction ||--o{ Payslip : applies

    Employee {
        UUID code PK
        String email UK
        String password
        String firstName
        String lastName
        String phoneNumber
        LocalDate dateOfBirth
        String gender
        String address
        String role
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    Employment {
        UUID code PK
        UUID employee_code FK
        String department
        String position
        BigDecimal baseSalary
        Status status
        LocalDate joiningDate
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    Payslip {
        UUID code PK
        UUID employee_code FK
        UUID employment_code FK
        LocalDate month
        BigDecimal grossSalary
        BigDecimal netSalary
        Status status
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    Deduction {
        UUID code PK
        UUID employee_code FK
        String type
        BigDecimal amount
        String description
        LocalDate effectiveDate
        Status status
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }
```

## Entity Descriptions

### Employee
- Primary entity representing system users
- Contains personal and authentication information
- Has role-based access control (ADMIN, MANAGER, EMPLOYEE)
- One-to-many relationships with Employment, Payslip, and Deduction

### Employment
- Represents employment history of employees
- Tracks department, position, and salary information
- One-to-many relationship with Payslip
- Many-to-one relationship with Employee

### Payslip
- Represents monthly salary payments
- Calculates gross and net salary
- Includes all applicable deductions
- Many-to-one relationships with Employee and Employment
- One-to-many relationship with Deduction

### Deduction
- Represents salary deductions (tax, insurance, etc.)
- Can be one-time or recurring
- Many-to-one relationship with Employee
- One-to-many relationship with Payslip

## Key Features

1. **UUID Primary Keys**
   - All entities use UUID for primary keys
   - Ensures global uniqueness
   - Better for distributed systems

2. **Audit Fields**
   - All entities include createdAt and updatedAt
   - Tracks creation and modification times
   - Helps with auditing and debugging

3. **Status Tracking**
   - Employment, Payslip, and Deduction have status fields
   - Tracks active/inactive states
   - Helps with business logic implementation

4. **Foreign Key Relationships**
   - All relationships are properly defined
   - Ensures data integrity
   - Enables efficient querying

5. **Indexing**
   - Email is unique indexed
   - Foreign keys are indexed
   - Optimizes query performance