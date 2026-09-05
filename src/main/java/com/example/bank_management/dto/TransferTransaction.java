package com.example.bank_management.dto;

import com.example.bank_management.model.TransactionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferTransaction{
  private Long senderId;
  private Long receiverId;
  private double amount;
  private double total;
  private TransactionType type = TransactionType.TRANSFER;
}