package com.example.exam.service;

import com.example.exam.entity.Employee;
import com.example.exam.entity.Employment;
import com.example.exam.repository.EmployeeRepository;
import com.example.exam.repository.EmploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentRepository employmentRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findByCode(UUID code) {
        return employeeRepository.findById(code);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> update(UUID code, Employee employee) {
        return employeeRepository.findById(code).map(existingEmployee -> {
            // Prevent updating email and code
            employee.setEmail(existingEmployee.getEmail());
            employee.setCode(existingEmployee.getCode());

            // Only update non-empty fields
            if (StringUtils.hasText(employee.getFirstName())) {
                existingEmployee.setFirstName(employee.getFirstName());
            }
            if (StringUtils.hasText(employee.getLastName())) {
                existingEmployee.setLastName(employee.getLastName());
            }
            if (StringUtils.hasText(employee.getPassword())) {
                existingEmployee.setPassword(employee.getPassword());
            }
            if (StringUtils.hasText(employee.getMobile())) {
                existingEmployee.setMobile(employee.getMobile());
            }
            if (employee.getDob() != null) {
                existingEmployee.setDob(employee.getDob());
            }
            if (employee.getStatus() != null) {
                existingEmployee.setStatus(employee.getStatus());
            }

            return employeeRepository.save(existingEmployee);
        });
    }

    public boolean delete(UUID code) {
        if (employeeRepository.existsById(code)) {
            employeeRepository.deleteById(code);
            return true;
        }
        return false;
    }

    public Employment addEmploymentDetails(UUID employeeCode, Employment employment) {
        Employee employee = employeeRepository.findById(employeeCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employment.setEmployee(employee);
        return employmentRepository.save(employment);
    }
} 