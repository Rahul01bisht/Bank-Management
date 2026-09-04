package com.example.bank_management.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TransactionHistory{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long transactionId;
  private Long senderId;
  private Long receiverId;
  private double amount;
  @Enumerated(EnumType.STRING)
  private TransactionType type;
  private double total;
  private LocalDateTime time;

  @PrePersist
  public void setTime() {
    time = LocalDateTime.now();
  }
  
}
