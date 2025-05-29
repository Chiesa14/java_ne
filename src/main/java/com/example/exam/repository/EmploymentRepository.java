package com.example.exam.repository;

import com.example.exam.entity.Employee;
import com.example.exam.entity.Employment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, UUID> {
    Optional<Employment> findByEmployeeCode(UUID employeeCode);
    Optional<Employment> findByEmployee(Employee employee);
}