package com.example.bank_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bank_management.model.TransactionHistory;

import java.util.List;


public interface TransactionRepository extends JpaRepository<TransactionHistory, Long>{
  List<TransactionHistory> findBySenderIdOrReceiverId(
    Long sender_Id, Long receiver_Id
  );
}