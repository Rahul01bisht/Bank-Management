package com.example.bank_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank_management.model.BankAccount;


public interface AccountRepository extends JpaRepository<BankAccount , Long>{
  
}