package com.example.bank_management.model;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String password;
  private String role;
  private LocalDateTime time;
}