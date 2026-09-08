package com.example.bank_management.controller;

import com.example.bank_management.dto.*;
import com.example.bank_management.model.*;
import com.example.bank_management.service.AccountService;
import com.example.bank_management.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  @Autowired
  private AccountService acService;


  
  // CREATE NEW ACCOUNT
  @PostMapping("/create")
  public ResponseEntity<BankAccount> create(
          @Valid @RequestBody CreateAccountRequest data) {

    BankAccount account = acService.addUser(data);
    return ResponseEntity.ok(account);
    }


  
  // FIND ALL ACCOUNTS
  @GetMapping
  public ResponseEntity<List<BankAccount>> findAll() {
    
    return ResponseEntity.ok(acService.findAll());
    }


    
  // CREDIT MONEY
  @PutMapping("/credit")
  public ResponseEntity<UserResponse> credit(
        @Valid @RequestBody CreditRequest data) {

    UserResponse list =
            acService.userCredit(data);

    return ResponseEntity.ok(list);
  }



  // DEBIT MONEY
  @PutMapping("/debit")
  public ResponseEntity<UserResponse> debit(
        @Valid @RequestBody DebitRequest data) {

    UserResponse list =
          acService.userDebit(data);
    
    return ResponseEntity.ok(list);
  }


  
  // CHECK BALANCE
  @GetMapping("/balance")
  public ResponseEntity<?> checkBalance(
        @RequestParam Long userId) {
            
      return ResponseEntity.ok(acService.findUser(userId));
  }



  // TRANSFER MONEY
  @PutMapping("/transfer")
  public ResponseEntity<TransferResponse> transfer(
      @Valid @RequestBody TransferRequest data) {

    TransferResponse list =
            acService.transferMoney(data);

    return ResponseEntity.ok(list);

    }



  // TRANSACTION HISTORY
  @GetMapping("/transaction")
  public ResponseEntity<List<TransferResponse>> history() {
    return ResponseEntity.ok(
                acService.transactionHistory()
        );

  }


    //see transaction history by id
    @GetMapping("/transaction/{userId}")
  public ResponseEntity<List<TransferResponse>> historyByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(
                acService.transactionHistoryById(userId)
        );
    
  }
}