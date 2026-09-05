# 🏦 Bank Management System API

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-MariaDB%20%2F%20MySQL-blue.svg)](https://mariadb.org/)
[![Status](https://img.shields.io/badge/Status-In%20Development-yellow.svg)](#-roadmap)

A production-ready RESTful Bank Management System built with **Java** and **Spring Boot**. The service exposes clean endpoints to handle foundational banking workflows, including account initialization, real-time balance queries, and atomic credit/debit transactions.

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

## 🗄 Database Schema

The primary persistence entity is mapped to the `bank_account` table:

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

### Configuration

1. Clone the repository:
   ```bash
   git clone https://github.com/Rahul01bisht/bank-management.git
   cd bank-management
   ```

2. Create the database in MariaDB/MySQL:
   ```sql
   CREATE DATABASE bank_db;
   ```

3. Update your database credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mariadb://localhost:3306/bank_db
   spring.datasource.username=YOUR_DB_USERNAME
   spring.datasource.password=YOUR_DB_PASSWORD
   spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```

---

### Installation & Run

* **Linux / macOS:**
  ```bash
  ./mvnw clean spring-boot:run
  ```

* **Windows:**
  ```cmd
  mvnw.cmd clean spring-boot:run
  ```

The server will start on port `8080` by default.

---

## 🧪 Testing

You can verify the endpoints using cURL or import them directly into Postman / Thunder Client.

**Quick cURL Balance Verification:**
```bash
curl -X GET "http://localhost:8080/accounts/balance?userId=1"
```

---

## 📈 Roadmap

- [ ] Atomic peer-to-peer money transfers between accounts
- [ ] Ledger-backed transaction history logs
- [ ] Bean validation (`@Valid`, `@NotNull`, `@PositiveOrZero`)
- [ ] Centralized `@ControllerAdvice` global exception handling
- [ ] Soft deletion and profile update routes
- [ ] Spring Security integration with JWT authentication

---

## 👨‍💻 Author

* **Rahul Bisht** - [GitHub Profile](https://github.com/Rahul01bisht)
* 
