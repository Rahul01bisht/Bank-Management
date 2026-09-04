package com.example.bank_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank_management.model.TransactionHistory;


public interface TransactionRepository extends JpaRepository<TransactionHistory, Long>{
  
}