package com.example.bank_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest{
  @NotBlank(message = "Fill your Name")
  private String name;
  
  @Positive(message = "Amount must be greater than 0")
  private double amount;
}