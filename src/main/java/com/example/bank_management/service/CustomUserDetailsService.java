package com.example.bank_management.service;

import com.example.bank_management.model.User;
import com.example.bank_management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.bank_management.security.RoleAuthorityMapping;

import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

  @Autowired
  private UserRepository repo;
  
  @Override
  public UserDetails loadUserByUsername(String username){
    User user = repo.findByName(username)
      .orElseThrow(() ->
                  new UsernameNotFoundException(
          "Username not found"
      ));
 
    return org.springframework.security.core.userdetails.User
      .withUsername(user.getName())
      .password(user.getPassword())
      .roles(user.getRole())
      .authorities(RoleAuthorityMapping
                   .getAuthorities(user.getRole())
                   .toArray(new String[0])
                  )
      .build();
  }
}