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

    @Autowired
    private TransactionService tsService;


    // =========================
    // CREATE ACCOUNT
    // =========================
    @PostMapping("/create")
    public ResponseEntity<BankAccount> create(
            @Valid @RequestBody CreateAccountRequest data) {

        BankAccount account = acService.addUser(data);

        return ResponseEntity.ok(account);
    }


    // =========================
    // FIND ALL ACCOUNTS
    // =========================
    @GetMapping
    public ResponseEntity<List<BankAccount>> findAll() {

        return ResponseEntity.ok(acService.findAll());
    }


    // =========================
    // CREDIT MONEY
    // =========================
    @PutMapping("/credit")
    public ResponseEntity<UserResponse> credit(
            @Valid @RequestBody CreditRequest data) {

        UserResponse list =
                acService.userCredit(data);

        if (list.getType() == TransactionType.FAILED) {

            return ResponseEntity
                    .badRequest()
                    .body(list);
        }

        return ResponseEntity.ok(list);
    }


    // =========================
    // DEBIT MONEY
    // =========================
    @PutMapping("/debit")
    public ResponseEntity<UserResponse> debit(
            @Valid @RequestBody DebitRequest data) {


        UserResponse list =
                acService.userDebit(data);

        if (list.getType() == TransactionType.FAILED) {

            return ResponseEntity
                    .badRequest()
                    .body(list);
        }

        return ResponseEntity.ok(list);
    }


    // =========================
    // CHECK BALANCE
    // =========================
    @GetMapping("/balance")
    public ResponseEntity<?> checkBalance(
            @RequestParam Long userId) {

        if (userId == null ||
                !acService.findUserId(userId)) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "UserId not found"
                    ));
        }

        BankAccount account =
                acService.findUser(userId);

        return ResponseEntity.ok(account);
    }


    // =========================
    // TRANSFER MONEY
    // =========================
    @PutMapping("/transfer")
    public ResponseEntity<?> transfer(
            @Valid @RequestBody TransferRequest data) {

        String message =
                acService.transferMoney(data);

        if (message.equals("Transfer Successfully")) {

            return ResponseEntity.ok(
                    Map.of("message", message)
            );
        }

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "message", message
                ));
    }


    // =========================
    // TRANSACTION HISTORY
    // =========================
    @GetMapping("/history")
    public ResponseEntity<List<TransactionHistory>> history() {

        return ResponseEntity.ok(
                tsService.showAll()
        );
    }
}