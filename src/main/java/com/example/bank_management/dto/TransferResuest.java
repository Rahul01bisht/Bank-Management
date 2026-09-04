package com.example.bank_management.dto;


@Getter
@Setter
@NoArgsConstruction
@AllArgsConstruction
public class TransferRequest {
    private Long senderId;
    private Long receiverId;
    private double amount;
}