package com.example.bank_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class RegisterResponse{
  private String name;
  private String password;
  private String message;
  private LocalDateTime time;
}