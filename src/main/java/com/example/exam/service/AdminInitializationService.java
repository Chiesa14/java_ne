package com.example.exam.service;

import com.example.exam.entity.Employee;
import com.example.exam.entity.Employment;
import com.example.exam.entity.Role;
import com.example.exam.repository.EmployeeRepository;
import com.example.exam.repository.EmploymentRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializationService implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final EmploymentRepository employmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.firstName}")
    private String adminFirstName;

    @Value("${admin.lastName}")
    private String adminLastName;

    @Value("${admin.baseSalary}")
    private String adminBaseSalary;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            log.info("Checking for admin user...");
            
            // Use native query to check if admin exists
            Long count = (Long) entityManager.createNativeQuery(
                    "SELECT COUNT(*) FROM employee WHERE email = :email")
                    .setParameter("email", adminEmail)
                    .getSingleResult();

            if (count > 0) {
                log.info("Admin user already exists");
                return;
            }

            log.info("Creating admin user...");

            // Create admin employee
            Employee admin = Employee.builder()
                    .code(UUID.randomUUID())
                    .firstName(adminFirstName)
                    .lastName(adminLastName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .mobile("+250788888888")
                    .dob(LocalDate.of(2000, 1, 1))
                    .roles(Set.of(Role.ADMIN))
                    .status(Employee.Status.ACTIVE)
                    .build();

            // Use native query to insert admin
            entityManager.createNativeQuery(
                    "INSERT INTO employee (code, first_name, last_name, email, password, mobile, dob, status, created_at, updated_at) " +
                    "VALUES (:code, :firstName, :lastName, :email, :password, :mobile, :dob, :status, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
                    .setParameter("code", admin.getCode())
                    .setParameter("firstName", admin.getFirstName())
                    .setParameter("lastName", admin.getLastName())
                    .setParameter("email", admin.getEmail())
                    .setParameter("password", admin.getPassword())
                    .setParameter("mobile", admin.getMobile())
                    .setParameter("dob", admin.getDob())
                    .setParameter("status", admin.getStatus().name())
                    .executeUpdate();

            // Insert admin role
            entityManager.createNativeQuery(
                    "INSERT INTO employee_roles (employee_code, role) VALUES (:code, :role)")
                    .setParameter("code", admin.getCode())
                    .setParameter("role", Role.ADMIN.name())
                    .executeUpdate();

            // Create admin employment
            UUID employmentCode = UUID.randomUUID();
            entityManager.createNativeQuery(
                    "INSERT INTO employment (code, employee_code, department, position, base_salary, status, joining_date, created_at, updated_at) " +
                    "VALUES (:code, :employeeCode, :department, :position, :baseSalary, :status, :joiningDate, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
                    .setParameter("code", employmentCode)
                    .setParameter("employeeCode", admin.getCode())
                    .setParameter("department", "Administration")
                    .setParameter("position", "System Administrator")
                    .setParameter("baseSalary", new BigDecimal(adminBaseSalary))
                    .setParameter("status", Employment.Status.ACTIVE.name())
                    .setParameter("joiningDate", LocalDate.now())
                    .executeUpdate();

            log.info("Admin user created successfully");
        } catch (Exception e) {
            log.error("Failed to initialize admin user: {}", e.getMessage(), e);
        }
    }
} 