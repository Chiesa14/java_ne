package com.example.exam.service;

import com.example.exam.entity.Deductions;
import com.example.exam.repository.DeductionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeductionsService {

    @Autowired
    private DeductionsRepository deductionsRepository;

    public List<Deductions> findAll() {
        return deductionsRepository.findAll();
    }

    public Optional<Deductions> findByCode(UUID code) {
        return deductionsRepository.findById(code);
    }

    public Deductions save(Deductions deduction) {
        return deductionsRepository.save(deduction);
    }

    public Optional<Deductions> update(UUID code, Deductions deduction) {
        return deductionsRepository.findById(code).map(existingDeduction -> {
            existingDeduction.setDeductionName(deduction.getDeductionName());
            existingDeduction.setPercentage(deduction.getPercentage());
            return deductionsRepository.save(existingDeduction);
        });
    }

    public boolean delete(UUID code) {
        if (deductionsRepository.existsById(code)) {
            deductionsRepository.deleteById(code);
            return true;
        }
        return false;
    }
}
 