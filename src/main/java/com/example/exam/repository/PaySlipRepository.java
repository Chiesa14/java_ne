package com.example.exam.repository;

import com.example.exam.entity.PaySlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaySlipRepository extends JpaRepository<PaySlip, Long> {
    List<PaySlip> findByEmployeeCode(UUID employeeCode);
    List<PaySlip> findByEmployeeCodeAndMonthAndYear(UUID employeeCode, Integer month, Integer year);
    List<PaySlip> findByMonthAndYear(Integer month, Integer year);
    List<PaySlip> findByMonthAndYearAndStatus(Integer month, Integer year, PaySlip.Status status);
} 