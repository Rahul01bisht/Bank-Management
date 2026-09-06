# 🏦 Bank Management System API

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-MariaDB%20%2F%20MySQL-blue.svg)](https://mariadb.org/)
[![Status](https://img.shields.io/badge/Status-In%20Development-yellow.svg)](#-roadmap)

A production-ready RESTful Bank Management System built with **Java** and **Spring Boot**. The service exposes clean endpoints to handle foundational banking workflows: account creation, credit/debit, transfers and transaction history.

---

## 📑 Table of Contents

- [Tech Stack](#-tech-stack)
- [Architecture & Design](#-architecture--design)
- [API Endpoints](#-api-endpoints)
- [DTOs & Validation](#-dtos--validation)
- [Database Schema](#-database-schema)
- [Getting Started](#-getting-started)
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
| `POST` | `/accounts/create` | Create a new bank account | CreateAccountRequest JSON |
| `GET` | `/accounts` | Retrieve all accounts | None |
| `GET` | `/accounts/balance` | Query account summary / balance (query param `userId`) | None |
| `PUT` | `/accounts/credit` | Deposit (credit) funds into an account | CreditRequest JSON |
| `PUT` | `/accounts/debit` | Withdraw (debit) funds from an account | DebitRequest JSON |
| `PUT` | `/accounts/transfer` | Transfer funds between accounts | TransferRequest JSON |
| `GET` | `/accounts/history` | List transaction history | None |

---

## 🔁 DTOs & Validation

The documentation below reflects the current DTOs and model field names in the project. Validation annotations are applied in DTOs (Jakarta Validation): `@NotBlank`, `@NotNull`, `@Positive`.

#### CreateAccountRequest
- Fields:
  - `name` (String) - required (@NotBlank)
  - `amount` (double) - initial deposit, must be > 0 (@Positive)

Example request:
```json
{
  "name": "John Doe",
  "amount": 1000.0
}
```

Controller response: The controller returns the persisted BankAccount entity (HTTP 200 OK) with fields: `userId`, `userName`, `userBalance`.

Example BankAccount response:
```json
{
  "userId": 1,
  "userName": "John Doe",
  "userBalance": 1000.0
}
```

---

#### CreditRequest
- Fields:
  - `userId` (Long) - account id to credit (@NotNull)
  - `amount` (double) - amount to credit, must be > 0 (@Positive)

Example request:
```json
{
  "userId": 1,
  "amount": 500.0
}
```

Response: Returns a UserResponse DTO (HTTP 200 OK) on success, or HTTP 400 Bad Request when transaction fails.

UserResponse fields:
- `transactionId` (Long)
- `userId` (Long)
- `name` (String)
- `amount` (double)
- `type` (TransactionType) - CREDIT/DEBIT/FAILED
- `time` (LocalDateTime)

Example success response:
```json
{
  "transactionId": 101,
  "userId": 1,
  "name": "John Doe",
  "amount": 500.0,
  "type": "CREDIT",
  "time": "2026-09-06T12:34:56"
}
```

---

#### DebitRequest
- Fields:
  - `userId` (Long) - account id to debit (@NotNull)
  - `amount` (double) - amount to withdraw, must be > 0 (@Positive)

Example request:
```json
{
  "userId": 1,
  "amount": 200.0
}
```

Response: Returns a UserResponse DTO (HTTP 200 OK) on success, or HTTP 400 Bad Request when transaction fails (e.g., insufficient funds).

---

#### TransferRequest
- Fields:
  - `senderId` (Long) - account id of sender (@NotNull)
  - `receiverId` (Long) - account id of receiver (@NotNull)
  - `amount` (double) - amount to transfer, must be > 0 (@Positive)

Example request:
```json
{
  "senderId": 1,
  "receiverId": 2,
  "amount": 25.0
}
```

Response: Returns a TransferResponse DTO (HTTP 200 OK) on success, or HTTP 400 Bad Request on failure.

TransferResponse fields:
- `transactionId` (Long)
- `senderId` (Long)
- `receiverId` (Long)
- `name` (String) — account holder name (if provided by service)
- `amount` (double)
- `type` (TransactionType)
- `time` (LocalDateTime)

Example success response:
```json
{
  "transactionId": 200,
  "senderId": 1,
  "receiverId": 2,
  "name": "John Doe",
  "amount": 25.0,
  "type": "TRANSFER",
  "time": "2026-09-06T13:00:00"
}
```

---

#### TransactionHistory (model)
The `TransactionHistory` entity contains recorded transactions. Fields:
- `transactionId` (Long)
- `senderId` (Long)
- `receiverId` (Long)
- `amount` (double)
- `type` (TransactionType) — stored as String
- `total` (double) — balance / total after transaction if provided
- `time` (LocalDateTime)

Example response from `/accounts/history`:
```json
[
  {
    "transactionId": 101,
    "senderId": 1,
    "receiverId": null,
    "amount": 500.0,
    "type": "CREDIT",
    "total": 1500.0,
    "time": "2026-09-06T12:34:56"
  },
  {
    "transactionId": 102,
    "senderId": 1,
    "receiverId": null,
    "amount": 50.0,
    "type": "DEBIT",
    "total": 1450.0,
    "time": "2026-09-06T13:00:00"
  }
]
```

---

## 🗄 Database Schema

The primary persistence entity is mapped to the `bank_account` table and the fields reflect the `BankAccount` model:

```sql
CREATE TABLE bank_account (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    user_balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00
);
```

---

## 🚀 Getting Started

### Prerequisites

Ensure you have installed:
* [JDK 17 or higher](https://www.oracle.com/java/technologies/downloads/)
* [MariaDB Server](https://mariadb.org/download/) or MySQL
* [Maven 3.8+](https://maven.apache.org/) (optional, Maven Wrapper included)
* [Git](https://git-scm.com/)

---

### Configuration & Run

1. Clone the repository:
```bash
git clone https://github.com/Rahul01bisht/Bank-Management.git
cd Bank-Management
```

2. Create the database:
```sql
CREATE DATABASE bank_db;
```

3. Configure `src/main/resources/application.properties` with your DB credentials.

4. Start the app:
```bash
./mvnw clean spring-boot:run
```

Server starts on port `8080` by default.

---

## 🧪 Testing

Use curl / Postman to test endpoints. Examples:

Create account:
```bash
curl -X POST http://localhost:8080/accounts/create \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","amount":1000.0}'
```

Credit:
```bash
curl -X PUT http://localhost:8080/accounts/credit \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":500.0}'
```

Transfer:
```bash
curl -X PUT http://localhost:8080/accounts/transfer \
  -H "Content-Type: application/json" \
  -d '{"senderId":1,"receiverId":2,"amount":25.0}'
```

History:
```bash
curl http://localhost:8080/accounts/history
```

---

## 📈 Roadmap

- [ ] Atomic peer-to-peer money transfers between accounts
- [ ] Ledger-backed transaction history logs
- [ ] Bean validation (`@Valid`, `@NotNull`, `@Positive`)
- [ ] Centralized `@ControllerAdvice` global exception handling
- [ ] Soft deletion and profile update routes
- [ ] Spring Security integration with JWT authentication

---

## 👨‍💻 Author

* **Rahul Bisht** - [GitHub Profile](https://github.com/Rahul01bisht)

---

Notes:
- This README was synchronized with current DTOs and entity fields (CreateAccountRequest, CreditRequest, DebitRequest, TransferRequest, TransferResponse, UserResponse, TransactionHistory, BankAccount).
- If you'd like, I can add a GitHub Action or small script to regenerate or verify README snippets automatically when DTOs change — tell me if you want that and I’ll add it.
