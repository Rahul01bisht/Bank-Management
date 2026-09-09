package com.example.bank_management.service;

import com.example.bank_management.model.*;
import com.example.bank_management.dto.*;
import com.example.bank_management.exeption.ResourceNotFoundException;
import com.example.bank_management.exeption.LowBalanceException;
import com.example.bank_management.exeption.SameAccountTransferException;

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
  public List<AccountListDto> findAll(){
    List<BankAccount> datas = repo.findAll();
    List<AccountListDto> list = new ArrayList<>();
    for(BankAccount data:datas){
      list.add(mapOfAccountListDto(data));
    }
    return list;
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
  

  //getUserId  Name
  private String findUserName(Long userId)
  {
    BankAccount data = findUser(userId);
    return data.getUserName();
  }

  private String findUserIdName(Long userId){
    BankAccount data = repo
         .findById(userId)
         .orElse(null);
    if(data == null) return "null";
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
    if(!findUserId(data.getUserId())){

      list.setType(TransactionType.FAILED);
      tsService.saveCredit(list);
      throw new ResourceNotFoundException(
        data.getUserId() + " Not Found"
      );
      
    }

    credit(data);
    list.setTotal(balance(data.getUserId()));
    UserResponse response = 
      tsService.saveCredit(list);
    response.setName(
      findUserName(data.getUserId())
    );
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
  private boolean isValidBalance(DebitRequest debitReq){
    return balance(debitReq.getUserId()) >= debitReq.getAmount();
  }


  // when user do debit
  public UserResponse userDebit(DebitRequest data){
    DebitTransaction list = 
      mapOfDebitTransaction(data);

    // Validate user id 
    if(!findUserId(data.getUserId())){
      
      list.setType(TransactionType.FAILED);
      tsService.saveDebit(list);
      throw new ResourceNotFoundException(
        data.getUserId() + " Not Found"
      );
      
    }

    //Validate Account Money
    if(!isValidBalance(data)){
      
      list.setType(TransactionType.FAILED);
      list.setTotal(balance(data.getUserId()));
      tsService.saveDebit(list);
      throw new LowBalanceException("You have low balance in " + data.getUserId()+" your Account!");
    
    }

    //Debit Money
    debit(data);
    list.setTotal(balance(data.getUserId()));
    UserResponse response = 
      tsService.saveDebit(list);
    response.setName(findUserName(data.getUserId()));
    return response;
  }


  //tranfer money
  public TransferResponse transferMoney(TransferRequest data){

    TransferTransaction list = mapOfTransferTransaction(data);

    // Validate sender Id
    if (!findUserId(data.getSenderId())){
      list.setType(TransactionType.FAILED);
      tsService.saveTransfer(list);
      throw new ResourceNotFoundException("Your Sender Id "+ data.getSenderId() +" not found");
    }

    // Validate Receiver Id
    if(!findUserId(data.getReceiverId())){
      list.setType(TransactionType.FAILED);
      list.setTotal(balance(data.getSenderId()));
      tsService.saveTransfer(list);
      throw new ResourceNotFoundException("Your Receiver Id "+ data.getReceiverId() +" not found");
    }
         
    //Validate same Account Tranfer
    if(data.getReceiverId().equals(data.getSenderId()))
      {
        list.setType(TransactionType.FAILED);
        list.setTotal(balance(data.getSenderId()));
        tsService.saveTransfer(list);
        
        throw new SameAccountTransferException("You are not tranfer money in Your same Account");
    }

    //making debit and credit Request
    DebitRequest debitReq = mapOfDebitRequest(data);
    CreditRequest creditReq = mapOfCreditRequest(data);

    // Check sender balance and debit
    if (!isValidBalance(debitReq)) {

      // Insufficient balance
      list.setType(TransactionType.FAILED);
      list.setTotal(balance(data.getSenderId()));

      tsService.saveTransfer(list);

      throw new LowBalanceException("Your balance is low");
    }

    // Credit receiver
    debit(debitReq);
    credit(creditReq);

    list.setTotal(balance(data.getSenderId()));

    TransferResponse response =
      tsService.saveTransfer(list);
     response.setName(findUserName(data.getSenderId()));
    return response;
    
}


  // see Transaction history
  public List<TransferResponse> transactionHistory(){

    List<TransferResponse> data =
      tsService.transactionHistory();
    List<TransferResponse> list = new ArrayList<>();
    for(TransferResponse ts:data){
      if(ts.getSenderId() != null){
        ts.setName(findUserIdName(ts.getSenderId()));
      }else{
        ts.setName(findUserIdName(ts.getReceiverId()));
      }
      list.add(ts);
    }
    
    return list;
  }


  // see userId transaction History
  public List<TransferResponse> transactionHistoryById(Long userId){
    if(!findUserId(userId)){
      throw new ResourceNotFoundException("this user is not Exits");
    }
    List<TransferResponse> data = 
      tsService.transactionHistoryById(userId);
    
    List<TransferResponse> list = new ArrayList<>();
    for(TransferResponse ts:data){
      ts.setName(findUserIdName(userId));
      list.add(ts);
    }
    
    return list;
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


  private AccountListDto mapOfAccountListDto (BankAccount data){
    AccountListDto list = new AccountListDto(
      data.getUserName(),
      data.getUserId(),
      data.getUserBalance()
    );
    return list;
  }
  
}