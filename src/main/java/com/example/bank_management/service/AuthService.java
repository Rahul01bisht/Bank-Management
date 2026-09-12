package com.example.bank_management.service;

import com.example.bank_management.model.User;
import com.example.bank_management.dto.RegisterRequest;
import com.example.bank_management.dto.LoginRequest;
import com.example.bank_management.dto.RegisterResponse;
import com.example.bank_management.dto.LoginResponse;

import com.example.bank_management.exeption.ResourceNotFoundException;
import com.example.bank_management.exeption.UserNameAlreadyRegisterException;

import com.example.bank_management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import java.util.*;


@Service
public class AuthService{
  @Autowired
  private UserRepository repo;
  @Autowired
  private PasswordEncoder passwordEncoder;


  
  public RegisterResponse register(RegisterRequest data){
    if(repo.findByName(data.getName()).isPresent())
    {
      throw new UserNameAlreadyRegisterException(
        "Username Already Exists"
      );
    }
    User user_data = mapOfUserRegisterRequest(data);
    repo.save(user_data);
    RegisterResponse list = new RegisterResponse(
      data.getName(),
      data.getPassword(),
      "Register Success",
      user_data.getTime()
    );
    return list;
  }



  public LoginResponse login(LoginRequest data){
    
    User user = repo.findByName(data.getName())
                    .orElseThrow(() ->
                      new ResourceNotFoundException(
                            "Username not found"
                          ));
    if (!passwordEncoder.matches(
        data.getPassword(),
        user.getPassword()
      )) 
    {
      throw new ResourceNotFoundException(
        "Password not matched"
      );
    }
    LoginResponse list = new LoginResponse(

      user.getName(),
      user.getRole(),
      LocalDateTime.now()
      
    );

    return list;
  }


  
  private User mapOfUserRegisterRequest(RegisterRequest data){
    User user = new User();
    user.setName(data.getName());
    String encodedPassword = 
      passwordEncoder.encode(data.getPassword());
    user.setPassword(encodedPassword);
    user.setRole(data.getRole());
    user.setTime(LocalDateTime.now());
    return user;
  }

}