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

  public TransferResponse saveTransfer(TransferTransaction data){
    TransactionHistory th = new TransactionHistory();
    th.setSenderId(data.getSenderId());
    th.setReceiverId(data.getReceiverId());
    th.setAmount(data.getAmount());
    th.setTotal(data.getTotal());
    th.setType(data.getType());
    add(th);
    TransferResponse list = mapOfTransferResponse(th);
    return list;
  }
  
  public List<TransferResponse> transactionHistory(){
    List<TransactionHistory> dataArray =
      repo.findAll();
    List<TransferResponse> list =
      new ArrayList<>();
    for(TransactionHistory data : dataArray){
      list.add(mapOfTransferResponse(data));
    }

    return list;
  }
  
  public List<TransferResponse> transactionHistoryById(Long userId){
    
    List<TransactionHistory> dataArray =
      repo.findBySenderIdOrReceiverId(userId, userId);
    List<TransferResponse> list =
      new ArrayList<>();
    for(TransactionHistory data : dataArray){
      list.add(mapOfTransferResponse(data));
    }

    return list;
    
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

  private TransferResponse mapOfTransferResponse(
    TransactionHistory data
  ){
    TransferResponse list = new TransferResponse();
    list.setTransactionId(data.getTransactionId());
    list.setType(data.getType());
    list.setReceiverId(data.getReceiverId());
    list.setSenderId(data.getSenderId());
    list.setAmount(data.getAmount());
    list.setTime(data.getTime());
    return list;
  }
}