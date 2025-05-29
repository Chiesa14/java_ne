package com.example.exam.service;

import com.example.exam.dto.EmploymentRequest;
import com.example.exam.entity.Employee;
import com.example.exam.entity.Employment;
import com.example.exam.repository.EmploymentRepository;
import com.example.exam.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmploymentService {

    private final EmploymentRepository employmentRepository;
    private final EmployeeRepository employeeRepository;

    public List<Employment> findAll() {
        return employmentRepository.findAll();
    }

    public Optional<Employment> findById(UUID id) {
        return employmentRepository.findById(id);
    }

    public List<Employment> findByEmployeeEmail(String email) {
        return employmentRepository.findByEmployeeEmail(email);
    }

    public Employment save(EmploymentRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeCode())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Employment employment = Employment.builder()
                .employee(employee)
                .department(request.getDepartment())
                .position(request.getPosition())
                .baseSalary(request.getBaseSalary())
                .status(request.getStatus())
                .joiningDate(request.getJoiningDate())
                .build();

        return employmentRepository.save(employment);
    }

    public Optional<Employment> update(UUID id, EmploymentRequest request) {
        return employmentRepository.findById(id)
                .map(existingEmployment -> {
                    existingEmployment.setPosition(request.getPosition());
                    existingEmployment.setDepartment(request.getDepartment());
                    existingEmployment.setJoiningDate(request.getJoiningDate());
                    existingEmployment.setBaseSalary(request.getBaseSalary());
                    existingEmployment.setStatus(request.getStatus());
                    return employmentRepository.save(existingEmployment);
                });
    }

    public boolean delete(UUID id) {
        return employmentRepository.findById(id)
                .map(employment -> {
                    employmentRepository.delete(employment);
                    return true;
                })
                .orElse(false);
    }

    public boolean isEmployeeOfEmployment(UUID employmentId, String employeeEmail) {
        return employmentRepository.findById(employmentId)
                .map(employment -> employment.getEmployee().getEmail().equals(employeeEmail))
                .orElse(false);
    }
} 