package com.example.exam.repository;

import com.example.exam.entity.Deductions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface DeductionsRepository extends JpaRepository<Deductions, UUID> {
}
 