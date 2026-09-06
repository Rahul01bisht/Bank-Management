package com.example.bank_management.service;

import com.example.bank_management.model.*;
import com.example.bank_management.dto.*;
import com.example.bank_management.exeption.ResourceNotFoundException;
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
  public BankAccount addUser(CreateAccountRequest cAR){
    BankAccount bA = mapOfCreateAccount(cAR);
    return repo.save(bA);
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
    BankAccount data = repo
         .findById(userId)
         .orElseThrow(() ->
          new ResourceNotFoundException(
            userId +" is Not Found"
          ));
    return data;
  }

  //getUserId  name
  private String findUserName(Long userId)
  {
    BankAccount data = findUser(userId);
    return data.getUserName();
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
  private void credit(CreditRequest creditReq){
    Long userId = creditReq.getUserId();
    BankAccount data = findUser(userId);
    data.setUserBalance(data.getUserBalance() + creditReq.getAmount());
    repo.save(data);
  }

  
    // when user do credit
  public UserResponse userCredit(CreditRequest data){
    CreditTransaction list = mapOfCreditTransaction(data);
    if(findUserId(data.getUserId())){
      credit(data);
      list.setTotal(balance(data.getUserId()));
      UserResponse response = tsService.saveCredit(list);
      response.setName(findUserName(data.getUserId()));
      return response;
    }
    list.setType(TransactionType.FAILED);
    
    UserResponse response = tsService.saveCredit(list);
    response.setName("Not found");
    return response;
  }



  //debit money
  private void debit(DebitRequest debitReq){
    Long userId = debitReq.getUserId();
    BankAccount data = findUser(userId);
    data.setUserBalance(data.getUserBalance() - debitReq.getAmount());
  repo.save(data);
  }

// valide debit check
  public boolean isValidDebit(DebitRequest debitReq){
    return balance(debitReq.getUserId()) >= debitReq.getAmount();
  }

  
  // Check money is debit
  public boolean checkDebit(DebitRequest debitReq){
    if(isValidDebit(debitReq)){
      debit(debitReq);
      return true;
    }
    return false;
    }


  // when user do debit
  public UserResponse userDebit(DebitRequest data){
    DebitTransaction list = mapOfDebitTransaction(data);
    
    if(!findUserId(data.getUserId())){
      list.setType(TransactionType.FAILED);
    
    UserResponse response = tsService.saveDebit(list);
    response.setName("Not found");
    return response;
    }
    
    if(checkDebit(data)){
      list.setTotal(balance(data.getUserId()));
      UserResponse response = tsService.saveDebit(list);
      response.setName(findUserName(data.getUserId()));
      return response;
    }
    list.setType(TransactionType.FAILED);
    list.setTotal(balance(data.getUserId()));
    UserResponse response = tsService.saveDebit(list);
    response.setName(findUserName(data.getUserId()));
    return response;
  }

  


  //tranfer money
  public TransferResponse transferMoney(TransferRequest data){

    TransferTransaction list = mapOfTransferTransaction(data);

    // Validate accounts
    if (!findUserId(data.getSenderId())
            || !findUserId(data.getReceiverId())
            || data.getReceiverId().equals(data.getSenderId())) {

        list.setType(TransactionType.FAILED);

        if (findUserId(data.getSenderId())) {
            list.setTotal(balance(data.getSenderId()));
        }

        TransferResponse response = tsService.saveTransfer(list);
      if (findUserId(data.getSenderId())) {
         response.setName(findUserName(data.getSenderId()));
      }else{
         response.setName("Not found");
      }
      
        return response;
    }

    DebitRequest debitReq = mapOfDebitRequest(data);
    CreditRequest creditReq = mapOfCreditRequest(data);

    // Check sender balance and debit
    if (checkDebit(debitReq)) {

        // Credit receiver
        credit(creditReq);

        list.setType(TransactionType.TRANSFER);
        list.setTotal(balance(data.getSenderId()));

        TransferResponse response = tsService.saveTransfer(list);
      response.setName(findUserName(data.getSenderId()));
        return response;
    }

    // Insufficient balance
    list.setType(TransactionType.FAILED);
    list.setTotal(balance(data.getSenderId()));

    TransferResponse response = tsService.saveTransfer(list);
    response.setName(findUserName(data.getSenderId()));

    return response;
}


  private BankAccount mapOfCreateAccount(CreateAccountRequest cAR){
    BankAccount bA = new BankAccount();
    bA.setUserName(cAR.getName());
    bA.setUserBalance(cAR.getAmount());
    return bA;
  }

  private CreditRequest mapOfCreditRequest(TransferRequest data){
    CreditRequest creditReq = new CreditRequest();
    creditReq.setUserId(data.getReceiverId());
  creditReq.setAmount(data.getAmount());
    return creditReq;
  }

  private DebitRequest mapOfDebitRequest(TransferRequest data){
    DebitRequest debitReq = new DebitRequest();
    debitReq.setUserId(data.getSenderId());
  debitReq.setAmount(data.getAmount());
    return debitReq;
  }

  private CreditTransaction mapOfCreditTransaction (CreditRequest data){
    CreditTransaction list = new CreditTransaction();
    list.setAmount(data.getAmount());
    list.setSenderId(data.getUserId());
    return list;
  }

    private DebitTransaction mapOfDebitTransaction (DebitRequest data){
    DebitTransaction list = new DebitTransaction();
    list.setAmount(data.getAmount());
    list.setReceiverId(data.getUserId());
    return list;
    }

    private TransferTransaction mapOfTransferTransaction (TransferRequest data){
    TransferTransaction list = new TransferTransaction();
    list.setAmount(data.getAmount());
    list.setSenderId(data.getSenderId());
    list.setReceiverId(data.getReceiverId());
    return list;
    }

  
}