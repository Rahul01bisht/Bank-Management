package com.example.bank_management.dto;

import com.example.bank_management.model.TransactionType;
import java.time.LocalDateTime;
import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidExceptionResponse{
  private int statusCode;
  private String error;
  private String message;
  private TransactionType type;
  private LocalDateTime time;
  private Map<String , String> fieldError;
}