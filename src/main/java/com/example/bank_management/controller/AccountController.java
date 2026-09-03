package com.example.bank_management.controller;

import com.example.bank_management.model.BankAccount;
import com.example.bank_management.service.AccountService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


import java.util.*;
@RestController
@RequestMapping("/accounts")
public class AccountController{
  @Autowired
  private AccountService acService;

  // Create new Bank Account
  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestBody BankAccount BankAc){
    return ResponseEntity.ok(acService.addUser(BankAc));
  }

  //Find all account
  @GetMapping
  public ResponseEntity<?> findAll(){
    return ResponseEntity.ok(acService.findAll());
  }

  //add balance in account
  @PutMapping("/credit")
  public ResponseEntity<?> credit(@RequestBody BankAccount BankAc){
    Long Id = BankAc.getUserId();
    if(Id!=null && acService.findUserId(Id)){
      BankAccount bA = acService.findUser(Id);
      bA.setUserBalance(BankAc.getUserBalance()+bA.getUserBalance());
      acService.updateBalance(bA);
      return ResponseEntity.ok(bA);
    }
    return ResponseEntity.badRequest().body(Map.of("message", "UserId not matched here"));
  }

  

  //Debit balance in account
  @PutMapping("/debit")
  public ResponseEntity<?> debit(@RequestBody BankAccount BankAc){
    Long Id = BankAc.getUserId();
    if(Id!=null && acService.findUserId(Id)){
      BankAccount bA = acService.findUser(Id);
      double balance = bA.getUserBalance();
      double debitBalance = BankAc.getUserBalance();
      if(debitBalance>balance) return ResponseEntity.badRequest().body(Map.of("message", "Check your balance"));
      bA.setUserBalance(balance-debitBalance);
      acService.updateBalance(bA);
      return ResponseEntity.ok(bA);
    }
    return ResponseEntity.badRequest().body(Map.of("message", "UserId not matched here"));
  }

  // Check bank balance
  @GetMapping("/balance")
  public ResponseEntity<?> checkBalance(@RequestParam Long userId){
    if(userId == null || !acService.findUserId(userId)){
      return ResponseEntity.badRequest().body(Map.of("message","UserId is not found"));
    }
    return ResponseEntity.ok(acService.findUser(userId));
  }
}