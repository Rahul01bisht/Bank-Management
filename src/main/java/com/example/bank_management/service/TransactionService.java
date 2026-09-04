package com.example.bank_management.service;

import com.example.bank_management.model.*;
import com.example.bank_management.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class TransactionService{

  @Autowired
  private TransactionRepository repo;
  
  public void add(TransactionHistory th){
    repo.save(th);
  }

  public TransactionHistory update(TransactionHistory th, Long senderId,BankAccount bankAc, double total){
    Long Id = bankAc.getUserId();
    th.setSenderId(senderId);
    th.setReceiverId(Id);
    th.setAmount(bankAc.getUserBalance());
    th.setTotal(total);
    return th;
  }

  public List<TransactionHistory> showAll(){
    return repo.findAll();
  }
}