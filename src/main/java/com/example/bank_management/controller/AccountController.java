package com.example.bank_management.controller;

import com.example.bank_management.model.*;
import com.example.bank_management.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


import java.util.*;
@RestController
@RequestMapping("/accounts")
public class AccountController{
  
  @Autowired
  private AccountService acService;
  @Autowired
  private TransactionService tsService;

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
      TransactionHistory th = new TransactionHistory();
      th.setType(TransactionType.CREDIT);
      acService.credit(BankAc);
      th=tsService.update(th, null, BankAc, acService.balance(Id));
      tsService.add(th);
      return ResponseEntity.ok(acService.findUser(Id));
    }
    return ResponseEntity.badRequest().body(Map.of("message", "UserId not matched here"));
  }

  

  //Debit balance in account
  @PutMapping("/debit")
  public ResponseEntity<?> debit(@RequestBody BankAccount BankAc){
    Long Id = BankAc.getUserId();
    if(Id!=null && acService.findUserId(Id)){
      
      TransactionHistory th = new TransactionHistory();

      //if transaction is passed
      if(acService.isDebit(BankAc)){
        th.setType(TransactionType.DEBIT);
        th = tsService.update(th, null, BankAc, acService.balance(Id));
        tsService.add(th);
        return ResponseEntity.ok(acService.findUser(Id));
      }
      
      //if transaction is failed
      th.setType(TransactionType.FAILED);
      th = tsService.update(th, null, BankAc, acService.balance(Id));
      tsService.add(th);
      return ResponseEntity.badRequest().body(Map.of("message", "Check your balance"));
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

  //tranfer money

  @PutMapping("/transfer")
  public ResponseEntity<?> transfer(@RequestParam Long senderId, @RequestBody BankAccount BankAc){
    return ResponseEntity.ok(Map.of("message", acService.transferMoney(senderId, BankAc)));
  }

@GetMapping("/history")
public ResponseEntity<?> history() {
    return ResponseEntity.ok(tsService.showAll());
}
}