package com.example.bank_management.service;

import com.example.bank_management.model.BankAccount;
import com.example.bank_management.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class AccountService{
  

  @Autowired
  private AccountRepository repo;

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
  public boolean deleteUserAc(Long userId){
    if(repo.existsById(userId)){
      repo.deleteById(userId);
      return true;
    }
    return false;
  }
}