package com.example.bank_management.service;

import com.example.bank_management.model.*;
import com.example.bank_management.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class AccountService{
  

  @Autowired
  private AccountRepository repo;
  @Autowired
  private TransactionService tsService;

  // add user
  public BankAccount addUser(BankAccount ac){
    return repo.save(ac);
  }

  // find all user
  public List<BankAccount> findAll(){
    return repo.findAll();
  }

  // search user id
  public boolean findUserId(Long userId){
    return repo.existsById(userId);
  }

  // search user data
  public BankAccount findUser(Long userId){
    return repo.findById(userId).orElse(null);
  }

  // update user balance
  public BankAccount updateBalance(BankAccount bankAc){
    return repo.save(bankAc);
  }

  // delete user Account
  public boolean deleteUserId(Long userId){
    if(repo.existsById(userId)){
      repo.deleteById(userId);
      return true;
    }
    return false;
  }

  // check balance
  public double balance(Long userId){
    BankAccount data = findUser(userId);
    return data.getUserBalance();
  }

  //credit money
  public void credit(BankAccount bankAc){
    Long userId = bankAc.getUserId();
    BankAccount data = findUser(userId);
    data.setUserBalance(data.getUserBalance() + bankAc.getUserBalance());
    repo.save(data);
  }


  //debit money
  public void debit(BankAccount bankAc){
    Long userId = bankAc.getUserId();
    BankAccount data = findUser(userId);
    data.setUserBalance(data.getUserBalance() - bankAc.getUserBalance());
  repo.save(data);
  }

// valide debit check
  public boolean valideDebit(Long userId,double debitBalance){
    return balance(userId) >= debitBalance;
  }

  
  // Check money is debit
  public boolean isDebit(BankAccount bankAc){
    Long userId = bankAc.getUserId();
    if(valideDebit(userId,bankAc.getUserBalance())){
      debit(bankAc);
      return true;
    }
    return false;
    }

  

  //tranfer money
  public String transferMoney(Long senderId, BankAccount bankAc){
    BankAccount debit = new BankAccount();
    debit.setUserId(senderId);
    TransactionHistory th = new TransactionHistory();
    
    debit.setUserBalance(bankAc.getUserBalance());
    if(isDebit(debit)){
      credit(bankAc);
      th.setType(TransactionType.TRANSFER);
      th = tsService.update(th,senderId,bankAc, balance(senderId));
      tsService.add(th);
      
      return "Transfer Successfully";
    }
    th.setType(TransactionType.FAILED);
    th = tsService.update(th,senderId,bankAc,balance(senderId));
    tsService.add(th);
    return "Transfer Failled";
  }
}