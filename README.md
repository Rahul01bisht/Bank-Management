# 🏦 Bank Management System API

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-MariaDB%20%2F%20MySQL-blue.svg)](https://mariadb.org/)
[![Status](https://img.shields.io/badge/Status-In%20Development-yellow.svg)](#-roadmap)

A production-ready RESTful Bank Management System built with **Java** and **Spring Boot**. The service exposes clean endpoints to handle foundational banking workflows, including account initializa[...]

---

## 📑 Table of Contents

- [Tech Stack](#-tech-stack)
- [Architecture & Design](#-architecture--design)
- [API Endpoints](#-api-endpoints)
- [Database Schema](#-database-schema)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
  - [Installation & Run](#installation--run)
- [Testing](#-testing)
- [Roadmap](#-roadmap)
- [Author](#-author)

---

## 🛠 Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **Data Access:** Spring Data JPA, Hibernate
* **Database:** MariaDB / MySQL
* **Build Tool:** Apache Maven
* **Architecture:** RESTful Multi-Tier Architecture

---

## 🏛 Architecture & Design

The application follows the standard Spring Boot layered design pattern to enforce strict separation of concerns:

```text
[ Client (Postman / Web UI) ]
             │ HTTP (JSON)
             ▼
[ Controller Layer ]  ── AccountController.java (Handles HTTP requests & responses)
             │
             ▼
[ Service Layer ]     ── AccountService.java    (Business logic, validations, tx management)
             │
             ▼
[ Data Layer ]        ── AccountRepository.java (Spring Data JPA queries)
             │
             ▼
[ Database ]          ── MariaDB / MySQL
```

---

## 🔌 API Endpoints

### Base URL
```text
http://localhost:8080/accounts
```

### Endpoints Overview

| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/accounts/create` | Register a new bank account | `BankAccount` JSON |
| `GET` | `/accounts` | Retrieve all registered accounts | None |
| `GET` | `/accounts/balance` | Query balance via query parameter | None (`?userId={id}`) |
| `PUT` | `/accounts/credit` | Deposit funds into an account | Transaction JSON |
| `PUT` | `/accounts/debit` | Withdraw funds from an account | Transaction JSON |
| `PUT` | `/accounts/transfer` | Transfer funds between accounts | Transfer JSON |
| `GET` | `/accounts/history` | List transaction history | None |

---

### Request & Response Payloads

#### 1. Create Account
* **Endpoint:** `POST /accounts/create`
* **Content-Type:** `application/json`

**Request:**
```json
{
  "userName": "Rahul Bisht",
  "userBalance": 1500.00
}
```

**Response (`201 Created`):**
```json
{
  "userId": 1,
  "userName": "Rahul Bisht",
  "userBalance": 1500.00
}
```

---

#### 2. Get All Accounts
* **Endpoint:** `GET /accounts`

**Response (`200 OK`):**
```json
[
  {
    "userId": 1,
    "userName": "Rahul Bisht",
    "userBalance": 1500.00
  }
]
```

---

#### 3. Check Balance
* **Endpoint:** `GET /accounts/balance?userId=1`

**Response (`200 OK`):**
```json
{
  "userId": 1,
  "userBalance": 1500.00
}
```

---

#### 4. Credit Funds
* **Endpoint:** `PUT /accounts/credit`
* **Content-Type:** `application/json`

**Request:**
```json
{
  "userId": 1,
  "userBalance": 500.00
}
```

**Response (`200 OK`):**
```json
{
  "userId": 1,
  "userBalance": 2000.00
}
```

---

#### 5. Debit Funds
* **Endpoint:** `PUT /accounts/debit`
* **Content-Type:** `application/json`

**Request:**
```json
{
  "userId": 1,
  "userBalance": 200.00
}
```

**Response (`200 OK`):**
```json
{
  "userId": 1,
  "userBalance": 1800.00
}
```

---

#### 6. Transfer Money
* **Endpoint:** `PUT /accounts/transfer`
* **Content-Type:** `application/json`
* **Description:** Transfer money from one account to another. Returns a TransferResponse indicating success or failure. On failure, the API returns `400 Bad Request` with details.

**Request:**
```json
{
  "fromUserId": 1,
  "toUserId": 2,
  "amount": 25.0,
  "description": "Payment"
}
```

**Response (`200 OK`):**
```json
{
  "transactionId": 123,
  "fromUserId": 1,
  "toUserId": 2,
  "amount": 25.0,
  "status": "SUCCESS",
  "message": "Transfer completed"
}
```

**Sample curl:**
```bash
curl -X PUT http://localhost:8080/accounts/transfer \
  -H "Content-Type: application/json" \
  -d '{"fromUserId":1,"toUserId":2,"amount":25.0,"description":"Payment"}'
```

---

#### 7. Transaction History
* **Endpoint:** `GET /accounts/history`
* **Description:** Returns a list of transaction history entries (TransactionHistory). This endpoint provides an overview of all transactions recorded by the system.

**Response (`200 OK`):**
```json
[
  {
    "transactionId": 101,
    "userId": 1,
    "type": "CREDIT",
    "amount": 500.0,
    "timestamp": "2026-09-06T12:34:56Z",
    "description": "Salary"
  },
  {
    "transactionId": 102,
    "userId": 1,
    "type": "DEBIT",
    "amount": 50.0,
    "timestamp": "2026-09-06T13:00:00Z",
    "description": "ATM withdrawal"
  }
]
```

**Sample curl:**
```bash
curl http://localhost:8080/accounts/history
```

---

Notes:
- Request/response DTO field names in these examples are inferred from the controller methods and common naming. Adjust JSON field names if your DTOs differ.
- The API returns 400 Bad Request for operations that fail validation or business checks (e.g., insufficient funds).
