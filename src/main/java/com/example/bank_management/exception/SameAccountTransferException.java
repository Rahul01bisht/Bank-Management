package com.example.bank_management.exeption;

public class SameAccountTransferException extends RuntimeException {

  public SameAccountTransferException(String message){
    super(message);
  }
  
}