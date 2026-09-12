package com.example.bank_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LoginResponse{
  
  private String name;
  private String role;
  private LocalDateTime time;
  
}