package com.example.bank_management.service;


import com.example.bank_management.repository.TransactionRepository;
import com.example.bank_management.model.TransactionHistory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.bank_management.dto.*;

import java.util.*;

@Service
public class TransactionService{

  @Autowired
  private TransactionRepository repo;
  
  private void add(TransactionHistory th){
    repo.save(th);
  }

  public UserResponse saveCredit(CreditTransaction data){
    TransactionHistory th = new TransactionHistory();
    th.setSenderId(data.getSenderId());
    th.setAmount(data.getAmount());
    th.setTotal(data.getTotal());
    th.setType(data.getType());
    add(th);
    UserResponse list = mapOfUserResponse(th);
    return list;
  }

  public UserResponse saveDebit(DebitTransaction data){
    TransactionHistory th = new TransactionHistory();
    th.setReceiverId(data.getReceiverId());
    th.setAmount(data.getAmount());
    th.setTotal(data.getTotal());
    th.setType(data.getType());
    add(th);
    UserResponse list = mapOfUserResponse(th);
    return list;
  }

  public void saveTransfer(TransferTransaction data){
    TransactionHistory th = new TransactionHistory();
    th.setSenderId(data.getSenderId());
    th.setReceiverId(data.getReceiverId());
    th.setAmount(data.getAmount());
    th.setTotal(data.getTotal());
    th.setType(data.getType());
    add(th);
  }
  
  public List<TransactionHistory> showAll(){
    return repo.findAll();
  }


  
  private UserResponse mapOfUserResponse(
    TransactionHistory data
  ){
    UserResponse list = new UserResponse();
    list.setTransactionId(data.getTransactionId());
    list.setType(data.getType());
    if(data.getSenderId() == null) {
        list.setUserId(data.getReceiverId());
    } else {
        list.setUserId(data.getSenderId());
    }
    list.setAmount(data.getAmount());
    list.setTime(data.getTime());
    return list;
  }
}