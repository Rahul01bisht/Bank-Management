package com.example.bank_management.security;

import java.util.List;

public class RoleAuthorityMapping {

    public static List<String> getAuthorities(String role) {

        return switch (role.toUpperCase()) {

            case "ADMIN" -> List.of(
                "CREDIT",
                "DEBIT",
                "TRANSFER",
                "BALANCE",
                "TRANSACTION"
            );

            case "MANAGER" -> List.of(
                "CREDIT",
                "DEBIT",
                "TRANSFER",
                "BALANCE",
                "TRANSACTION"
            );

            case "EMPLOYEE" -> List.of(
                "BALANCE",
                "TRANSACTION"
            );

            default -> List.of();
        };
    }
}