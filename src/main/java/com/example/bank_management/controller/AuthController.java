package com.example.bank_management.controller;

import com.example.bank_management.dto.RegisterRequest;
import com.example.bank_management.dto.LoginRequest;
import com.example.bank_management.dto.RegisterResponse;
import com.example.bank_management.dto.LoginResponse;

import com.example.bank_management.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import org.springframework.security.web.csrf.CsrfToken;

import java.util.*;


@RequestMapping("/auth")
@RestController
public class AuthController{

  @Autowired
  private AuthService service;
  
  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(
    @Valid @RequestBody RegisterRequest data
  ){
    return ResponseEntity.ok(service.register(data));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
    @Valid @RequestBody LoginRequest data
  ){
    return ResponseEntity.ok(service.login(data));
  }
}

