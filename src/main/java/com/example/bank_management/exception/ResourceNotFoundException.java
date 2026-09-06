package com.example.bank_management.exeption;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message){
    super(message);
  }
  
}