package com.example.bank_management.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {

    @NotNull(message = "Send valid Your Id")
    private Long userId;

    @Positive(message = 
              "Amount must be greater than 0")
    private double amount;
}
