package com.example.bank_management.dto;

import com.example.bank_management.model.TransactionType;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse{
  private Long transactionId;
  private Long senderId;
  private Long receiverId;
  private String name;
  private double amount;
  private TransactionType type;
  private LocalDateTime time;
}