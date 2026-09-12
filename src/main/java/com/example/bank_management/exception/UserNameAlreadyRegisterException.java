package com.example.bank_management.exeption;

public class UserNameAlreadyRegisterException extends RuntimeException {
  
  public UserNameAlreadyRegisterException(String message){
    super(message);
  }
  
}