package com.example.bank_management.exeption;

public class LowBalanceException extends RuntimeException {

  public LowBalanceException(String message){
    super(message);
  }
  
}