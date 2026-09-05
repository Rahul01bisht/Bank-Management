package com.example.bank_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    @NotNull(message = "Send valid Sender Id")
    private Long senderId;
    @NotNull(message = "Send valid Receiver Id")
    private Long receiverId;
    @Positive(message = "Amount must be greater than 0")
    private double amount;
}