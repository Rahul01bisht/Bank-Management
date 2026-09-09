package com.example.bank_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountListDto{
  private String name;
  private Long accountId;
  private double balance;
}