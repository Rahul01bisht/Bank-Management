# 🏦 Bank Management System REST API

[![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-MariaDB%20%2F%20MySQL-003545.svg?style=flat-square&logo=mariadb)](https://mariadb.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active%20Development-yellow.svg?style=flat-square)](#-roadmap)

A clean, robust, multi-tier RESTful banking backend engineered with **Java 21**, **Spring Boot 3.5.3**, **Spring Data JPA**, and **MariaDB / MySQL**. It provides end-to-end banking capabilities—including account creation, atomic deposits, withdrawals, peer-to-peer fund transfers, full transaction auditing, and centralized validation error handling.

---

## 📑 Table of Contents

- [Key Features](#-key-features)
- [System Architecture](#-system-architecture)
- [Tech Stack](#-tech-stack)
- [Project Directory Structure](#-project-directory-structure)
- [Database & Data Models](#-database--data-models)
- [API Reference & Documentation](#-api-reference--documentation)
  - [1. Create Account](#1-create-account)
  - [2. List All Accounts](#2-list-all-accounts)
  - [3. Check Account Balance](#3-check-account-balance)
  - [4. Deposit Funds (Credit)](#4-deposit-funds-credit)
  - [5. Withdraw Funds (Debit)](#5-withdraw-funds-debit)
  - [6. Transfer Funds (P2P)](#6-transfer-funds-p2p)
  - [7. Transaction Audit History](#7-transaction-audit-history)
- [Validation & Error Handling](#-validation--error-handling)
- [End-to-End Walkthrough Flow](#-end-to-end-walkthrough-flow)
- [Getting Started](#-getting-started)
- [Roadmap](#-roadmap)
- [Author & Acknowledgments](#-author--acknowledgments)

---

## ✨ Key Features

- **Account Lifecycle Management:** Register user accounts with baseline validation and auto-allocated balances.
- **Atomic Financial Operations:** Deposit, withdraw, and perform peer-to-peer balance reallocations.
- **Rigorous Business Validations:**
  - Balance safety checks before debit/transfer (`LowBalanceException`).
  - Guards against transferring money to the exact same account (`SameAccountTransferException`).
  - Explicit missing resource handling (`ResourceNotFoundException`).
- **Comprehensive Audit Trail:** Automatically logs every transaction (`CREDIT`, `DEBIT`, `TRANSFER`) with timestamps and running totals.
- **Strict Data Contracts (DTO Pattern):** Separates database entities from inbound request/outbound response payloads.
- **Centralized Exception Interception:** Utilizes `@RestControllerAdvice` to convert runtime exceptions and Jakarta bean validation errors into predictable, client-friendly JSON.

---

## 🏛 System Architecture

The project enforces clean domain separation across layers:

```text
Client (Browser / Postman / Frontend)
                 │ HTTP (JSON)
                 ▼
     [ AccountController ] ────────── Validates request schemas via @Valid
                 │
                 ▼
       [ AccountService ]  ────────── Executes core business rules, checks balances
                 │
       ┌─────────┴─────────┐
       ▼                   ▼
[ AccountRepository ]   [ TransactionService ]
       │                   │
       ▼                   ▼
  (BankAccount)     [ TransactionRepository ]
                           │
                           ▼
                 (TransactionHistory)
                           │
                           ▼
             [ MariaDB / MySQL Database ]
```

### Complete Fund Transfer Processing Flow

```text
POST /accounts/transfer
        │
        ▼
AccountController.transfer()
        │
        ▼
AccountService.transfer()
   ├── 1. Validate sender existence
   ├── 2. Validate receiver existence
   ├── 3. Ensure sender != receiver
   ├── 4. Check available funds
   ├── 5. Deduct amount from sender
   └── 6. Credit amount to receiver
        │
        ▼
TransactionService.recordTransfer()
        │
        ▼
Save entry in TransactionHistory table
        │
        ▼
Return structured TransferResponse (200 OK)
```

---

## 🛠 Tech Stack

| Component | Technology | Version / Specification |
| :--- | :--- | :--- |
| **Language** | Java | 21 (LTS) |
| **Framework** | Spring Boot | 3.5.3 |
| **Web Layer** | Spring Web | MVC / REST Controller |
| **Persistence** | Spring Data JPA / Hibernate | Object-Relational Mapping (ORM) |
| **Database** | MariaDB / MySQL | Relational RDBMS |
| **Validation** | Jakarta Bean Validation | Schema validation (`@NotNull`, `@Positive`, etc.) |
| **Utilities** | Lombok | `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` |
| **Build & Tooling** | Apache Maven | Wrapper included (`mvnw` / `mvnw.cmd`) |

---

## 📁 Project Directory Structure

```text
bank-management/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/bank_management/
│   │   │   ├── BankManagementApplication.java       # Application entry point
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   └── AccountController.java           # Public REST endpoints
│   │   │   │
│   │   │   ├── dto/                                 # Data contracts
│   │   │   │   ├── CreateAccountRequest.java
│   │   │   │   ├── CreditRequest.java
│   │   │   │   ├── DebitRequest.java
│   │   │   │   ├── TransferRequest.java
│   │   │   │   ├── CreditTransaction.java
│   │   │   │   ├── DebitTransaction.java
│   │   │   │   ├── TransferTransaction.java
│   │   │   │   ├── UserResponse.java
│   │   │   │   ├── TransferResponse.java
│   │   │   │   ├── ExceptionResponseDto.java
│   │   │   │   └── ValidExceptionResponse.java
│   │   │   │
│   │   │   ├── exception/                           # Custom exceptions & handlers
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── LowBalanceException.java
│   │   │   │   └── SameAccountTransferException.java
│   │   │   │
│   │   │   ├── model/                               # JPA entities & enumerations
│   │   │   │   ├── BankAccount.java
│   │   │   │   ├── TransactionHistory.java
│   │   │   │   └── TransactionType.java
│   │   │   │
│   │   │   ├── repository/                          # Spring Data JPA interfaces
│   │   │   │   ├── AccountRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   │
│   │   │   └── service/                             # Business domain logic
│   │   │       ├── AccountService.java
│   │   │       └── TransactionService.java
│   │   │
│   │   └── resources/
│   │       └── application.properties               # DB connection & JPA configs
│   │
│   └── test/                                        # Unit & integration tests
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

---

## 🗃 Database & Data Models

### 1. `BankAccount` Entity
Stores customer identification and active monetary balances.

```sql
CREATE TABLE bank_account (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    user_balance DOUBLE PRECISION NOT NULL DEFAULT 0.0
);
```

### 2. `TransactionHistory` Entity
An append-only record tracking financial shifts across the platform.

```sql
CREATE TABLE transaction_history (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT,
    receiver_id BIGINT,
    amount DOUBLE PRECISION NOT NULL,
    type VARCHAR(20) NOT NULL,     -- CREDIT, DEBIT, TRANSFER, FAILED
    total DOUBLE PRECISION NOT NULL,    -- Running balance after transaction
    time DATETIME NOT NULL
);
```

### 3. `TransactionType` Enum
- `CREDIT`: Inward deposit to a specific account.
- `DEBIT`: Outward withdrawal from an account.
- `TRANSFER`: Funds reallocated between two unique accounts.
- `FAILED`: Aborted or non-executable operation.

---

## 🔌 API Reference & Documentation

**Base Context URL:** `http://localhost:8080/accounts`

### Endpoints Overview

| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/accounts/create` | Register a new bank account | `CreateAccountRequest` |
| `GET` | `/accounts` | Retrieve all registered accounts | *None* |
| `GET` | `/accounts/balance?userId={id}` | Check balance for a specific account | *None* |
| `PUT` | `/accounts/credit` | Deposit money into an account | `CreditRequest` |
| `PUT` | `/accounts/debit` | Withdraw money from an account | `DebitRequest` |
| `PUT` | `/accounts/transfer` | Transfer funds between two accounts | `TransferRequest` |
| `GET` | `/accounts/history` | View ledger of all logged transactions | *None* |

---

### Detailed Endpoint Specifications

#### 1. Create Account
* **URL:** `POST /accounts/create`
* **Headers:** `Content-Type: application/json`

**Request Body:**
```json
{
  "name": "Rahul Bisht",
  "amount": 5000.0
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/accounts/create   -H "Content-Type: application/json"   -d '{"name":"Rahul Bisht","amount":5000.0}'
```

**Response (`200 OK` / `201 Created`):**
```json
{
  "userId": 500001,
  "userName": "Rahul Bisht",
  "userBalance": 5000.0
}
```

---

#### 2. List All Accounts
* **URL:** `GET /accounts`

**cURL Command:**
```bash
curl -X GET http://localhost:8080/accounts
```

**Response (`200 OK`):**
```json
[
  {
    "userId": 500001,
    "userName": "Rahul Bisht",
    "userBalance": 5000.0
  },
  {
    "userId": 500002,
    "userName": "Aman Sharma",
    "userBalance": 3000.0
  }
]
```

---

#### 3. Check Account Balance
* **URL:** `GET /accounts/balance?userId=500001`

**cURL Command:**
```bash
curl -X GET "http://localhost:8080/accounts/balance?userId=500001"
```

**Response (`200 OK`):**
```json
{
  "userId": 500001,
  "userName": "Rahul Bisht",
  "userBalance": 5000.0
}
```

---

#### 4. Deposit Funds (Credit)
* **URL:** `PUT /accounts/credit`
* **Headers:** `Content-Type: application/json`

**Request Body:**
```json
{
  "userId": 500001,
  "amount": 1000.0
}
```

**cURL Command:**
```bash
curl -X PUT http://localhost:8080/accounts/credit   -H "Content-Type: application/json"   -d '{"userId":500001,"amount":1000.0}'
```

**Response (`200 OK`):**
```json
{
  "transactionId": 1,
  "userId": 500001,
  "name": "Rahul Bisht",
  "amount": 1000.0,
  "type": "CREDIT",
  "time": "2026-09-08T15:30:00"
}
```

---

#### 5. Withdraw Funds (Debit)
* **URL:** `PUT /accounts/debit`
* **Headers:** `Content-Type: application/json`

**Request Body:**
```json
{
  "userId": 500001,
  "amount": 500.0
}
```

**cURL Command:**
```bash
curl -X PUT http://localhost:8080/accounts/debit   -H "Content-Type: application/json"   -d '{"userId":500001,"amount":500.0}'
```

**Response (`200 OK`):**
```json
{
  "transactionId": 2,
  "userId": 500001,
  "name": "Rahul Bisht",
  "amount": 500.0,
  "type": "DEBIT",
  "time": "2026-09-08T15:35:00"
}
```

---

#### 6. Transfer Funds (P2P)
* **URL:** `PUT /accounts/transfer`
* **Headers:** `Content-Type: application/json`

**Request Body:**
```json
{
  "senderId": 500001,
  "receiverId": 500002,
  "amount": 1000.0
}
```

**cURL Command:**
```bash
curl -X PUT http://localhost:8080/accounts/transfer   -H "Content-Type: application/json"   -d '{"senderId":500001,"receiverId":500002,"amount":1000.0}'
```

**Response (`200 OK`):**
```json
{
  "transactionId": 3,
  "senderId": 500001,
  "receiverId": 500002,
  "name": "Rahul Bisht",
  "amount": 1000.0,
  "type": "TRANSFER",
  "time": "2026-09-08T15:40:00"
}
```

---

#### 7. Transaction Audit History
* **URL:** `GET /accounts/history`

**cURL Command:**
```bash
curl -X GET http://localhost:8080/accounts/history
```

**Response (`200 OK`):**
```json
[
  {
    "transactionId": 1,
    "senderId": 500001,
    "receiverId": null,
    "amount": 1000.0,
    "type": "CREDIT",
    "total": 6000.0,
    "time": "2026-09-08T15:30:00"
  },
  {
    "transactionId": 2,
    "senderId": null,
    "receiverId": 500001,
    "amount": 500.0,
    "type": "DEBIT",
    "total": 5500.0,
    "time": "2026-09-08T15:35:00"
  },
  {
    "transactionId": 3,
    "senderId": 500001,
    "receiverId": 500002,
    "amount": 1000.0,
    "type": "TRANSFER",
    "total": 4500.0,
    "time": "2026-09-08T15:40:00"
  }
]
```

---

## 🛡 Validation & Error Handling

The application leverages **Jakarta Bean Validation** coupled with a centralized **`@RestControllerAdvice`** (`GlobalExceptionHandler`) to ensure errors are returned predictably.

### Standard Error Payloads

#### 1. Low Balance Error (`400 BAD REQUEST`)
Triggered when withdrawing or transferring more funds than an account holds.
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "You have low balance in account 500001!",
  "type": "FAILED"
}
```

#### 2. Account Not Found (`404 NOT FOUND`)
Triggered when an identifier fails database lookup.
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Account with ID 999999 was not found.",
  "type": "FAILED"
}
```

#### 3. Same Account Transfer (`400 BAD REQUEST`)
Triggered when `senderId == receiverId`.
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Cannot transfer money to the same account!",
  "type": "FAILED"
}
```

#### 4. Schema Validation Failure (`400 BAD REQUEST`)
Triggered by `@Valid` when incoming parameters fail format requirements.
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation Failed",
  "type": "FAILED",
  "time": "2026-09-08T15:50:00",
  "fieldError": {
    "userId": "must not be null",
    "amount": "must be greater than 0"
  }
}
```

---

## 🔁 End-to-End Walkthrough Flow

Follow this sequence to test the entire lifecycle:

1. **Create First Account:**  
   `POST /accounts/create` with `{"name": "Rahul", "amount": 5000.0}`  
   *(Assigned `userId = 500001`)*

2. **Create Second Account:**  
   `POST /accounts/create` with `{"name": "Aman", "amount": 2000.0}`  
   *(Assigned `userId = 500002`)*

3. **Verify Balance:**  
   `GET /accounts/balance?userId=500001` → Balance is `5000.0`.

4. **Deposit (Credit):**  
   `PUT /accounts/credit` with `{"userId": 500001, "amount": 1000.0}`  
   → Balance becomes `6000.0`.

5. **Withdraw (Debit):**  
   `PUT /accounts/debit` with `{"userId": 500001, "amount": 500.0}`  
   → Balance becomes `5500.0`.

6. **Transfer Funds:**  
   `PUT /accounts/transfer` with `{"senderId": 500001, "receiverId": 500002, "amount": 1000.0}`  
   → Sender balance becomes `4500.0`, Receiver balance becomes `3000.0`.

7. **Review Audit Trail:**  
   `GET /accounts/history` → Verify all 3 operations are chronologically preserved.

---

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK):** Version 21 or newer
* **Database:** MariaDB Server or MySQL Server running on port `3306`
* **Git:** Version control client

### 1. Clone the Repository
```bash
git clone https://github.com/Rahul01bisht/Bank-Management.git
cd Bank-Management
```

### 2. Configure MariaDB / MySQL
Log into your database shell and instantiate the database:
```sql
CREATE DATABASE bank_management;
```

Update your connection credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/bank_management
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

# JPA / Hibernate Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 3. Build & Run Application

* **Linux / macOS:**
  ```bash
  ./mvnw clean spring-boot:run
  ```

* **Windows:**
  ```cmd
  mvnw.cmd clean spring-boot:run
  ```

The server will launch on port `8080`. You can start sending requests to `http://localhost:8080/accounts`.

---

## 📈 Roadmap

- [ ] Add `@Transactional` safety to ensure atomic rollbacks during transfer failures
- [ ] User-filtered transaction queries (`GET /accounts/history/{userId}`)
- [ ] Pagination & dynamic sorting on ledger tables
- [ ] Authentication & Authorization with Spring Security + JWT
- [ ] Interactive API documentation via Swagger UI / OpenAPI 3
- [ ] Modern UI Dashboard built with React & Tailwind CSS
- [ ] Containerization with Docker & Docker Compose

---

## 👨‍💻 Author & Acknowledgments

* **Rahul Bisht** - [GitHub Profile](https://github.com/Rahul01bisht)

Built with ☕ **Java 21** and 🍃 **Spring Boot 3.5.3**.
