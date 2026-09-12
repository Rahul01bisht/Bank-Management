package com.example.bank_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RegisterRequest{
  @NotBlank(message = "Enter your UserName")
  private String name;
  @NotBlank(message = "Enter your password")
  private String password;
  @NotBlank(message = "enter your role")
  private String role;
}