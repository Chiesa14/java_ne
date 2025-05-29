package com.example.exam.repository;

import com.example.exam.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByEmployee_Code(UUID employeeCode);
    List<Message> findByEmployee_CodeAndMonthAndYear(UUID employeeCode, Integer month, Integer year);
} 