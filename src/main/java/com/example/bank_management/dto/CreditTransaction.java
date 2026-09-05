package com.example.bank_management.dto;

import com.example.bank_management.model.TransactionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditTransaction{
  private Long senderId;
  private double amount;
  private double total;
  private TransactionType type = TransactionType.CREDIT;
}