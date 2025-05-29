package com.example.exam.service;

import com.example.exam.entity.Employee;
import com.example.exam.entity.Employment;
import com.example.exam.entity.PaySlip;
import com.example.exam.repository.EmployeeRepository;
import com.example.exam.repository.EmploymentRepository;
import com.example.exam.repository.PaySlipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaySlipService {

    private final PaySlipRepository paySlipRepository;
    private final EmployeeRepository employeeRepository;
    private final EmploymentRepository employmentRepository;
    private final EmailService emailService;

    @Transactional
    public List<PaySlip> generatePayroll(int month, int year) {
        List<Employee> activeEmployees = employeeRepository.findAll().stream()
                .filter(employee -> employee.getStatus() == Employee.Status.ACTIVE)
                .collect(Collectors.toList());

        return activeEmployees.stream()
                .map(employee -> {
                    // Check if employee already has a payslip for this month/year
                    List<PaySlip> existingPaySlips = paySlipRepository.findByEmployeeCodeAndMonthAndYear(employee.getCode(), month, year);
                    if (!existingPaySlips.isEmpty()) {
                        return existingPaySlips.get(0); // Return the existing payslip
                    }

                    // Get and validate employment status
                    Employment employment = employmentRepository.findByEmployee(employee)
                            .orElse(null);

                    // Skip if no employment record or employment is not active
                    if (employment == null || employment.getStatus() != Employment.Status.ACTIVE) {
                        return null;
                    }

                    BigDecimal baseSalary = employment.getBaseSalary();
                    BigDecimal housingAmount = baseSalary.multiply(BigDecimal.valueOf(0.14));
                    BigDecimal transportAmount = baseSalary.multiply(BigDecimal.valueOf(0.14));
                    BigDecimal grossSalary = baseSalary.add(housingAmount).add(transportAmount);

                    BigDecimal employeeTaxedAmount = baseSalary.multiply(BigDecimal.valueOf(0.30));
                    BigDecimal pensionAmount = baseSalary.multiply(BigDecimal.valueOf(0.06));
                    BigDecimal medicalInsuranceAmount = baseSalary.multiply(BigDecimal.valueOf(0.05));
                    BigDecimal otherTaxedAmount = baseSalary.multiply(BigDecimal.valueOf(0.05));

                    BigDecimal netSalary = grossSalary.subtract(employeeTaxedAmount)
                            .subtract(pensionAmount)
                            .subtract(medicalInsuranceAmount)
                            .subtract(otherTaxedAmount);

                    PaySlip paySlip = PaySlip.builder()
                            .employeeCode(employee.getCode())
                            .houseAmount(housingAmount)
                            .transportAmount(transportAmount)
                            .employeeTaxedAmount(employeeTaxedAmount)
                            .pensionAmount(pensionAmount)
                            .medicalInsuranceAmount(medicalInsuranceAmount)
                            .otherTaxedAmount(otherTaxedAmount)
                            .grossSalary(grossSalary)
                            .netSalary(netSalary)
                            .month(month)
                            .year(year)
                            .status(PaySlip.Status.PENDING)
                            .build();

                    return paySlipRepository.save(paySlip);
                })
                .filter(paySlip -> paySlip != null) // Remove null entries (employees without active employment)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approvePayroll(int month, int year) {
        List<PaySlip> pendingPaySlips = paySlipRepository.findByMonthAndYearAndStatus(month, year, PaySlip.Status.PENDING);
        
        for (PaySlip paySlip : pendingPaySlips) {
            Employee employee = employeeRepository.findById(paySlip.getEmployeeCode())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            paySlip.setStatus(PaySlip.Status.PAID);
            paySlipRepository.save(paySlip);

            // Send email notification
            emailService.sendSalaryNotification(
                employee.getEmail(),
                employee.getFirstName(),
                paySlip.getMonth(),
                paySlip.getYear(),
                paySlip.getNetSalary().toString(),
                employee.getCode().toString()
            );
        }
    }

    @Transactional
    public PaySlip approvePaySlip(Long id) {
        PaySlip paySlip = paySlipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaySlip not found"));

        Employee employee = employeeRepository.findById(paySlip.getEmployeeCode())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        paySlip.setStatus(PaySlip.Status.PAID);
        PaySlip savedPaySlip = paySlipRepository.save(paySlip);

        // Send email notification
        emailService.sendSalaryNotification(
            employee.getEmail(),
            employee.getFirstName(),
            paySlip.getMonth(),
            paySlip.getYear(),
            paySlip.getNetSalary().toString(),
            employee.getCode().toString()
        );

        return savedPaySlip;
    }

    @Transactional(readOnly = true)
    public List<PaySlip> getAllPaySlips() {
        return paySlipRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PaySlip> getPaySlipsByEmployeeCode(UUID employeeCode) {
        return paySlipRepository.findByEmployeeCode(employeeCode);
    }

    @Transactional(readOnly = true)
    public List<PaySlip> getPaySlipsByMonthAndYear(int month, int year) {
        return paySlipRepository.findByMonthAndYear(month, year);
    }
} 