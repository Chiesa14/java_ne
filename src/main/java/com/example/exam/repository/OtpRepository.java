package com.example.exam.repository;

import com.example.exam.entity.Employee;
import com.example.exam.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID> {
    Optional<Otp> findByEmployeeAndOtp(Employee employee, String otp);
    void deleteByEmployee(Employee employee);
    Optional<Otp> findByEmployee_EmailAndOtpAndUsedFalse(String email, String otp);
} 