🏦 Bank Management System

A RESTful Bank Management System built with Java 21, Spring Boot, Spring Data JPA, and MariaDB/MySQL.

This project provides APIs for creating bank accounts, checking balances, depositing and withdrawing money, transferring money between accounts, viewing transaction history, and handling errors using custom exceptions and a global exception handler.

---

📌 Project Overview

The Bank Management System follows a layered Spring Boot architecture:

Client / Postman / Frontend
            │
            ▼
     AccountController
            │
            ▼
       AccountService
            │
       ┌────┴────┐
       ▼         ▼
AccountRepository  TransactionService
       │               │
       ▼               ▼
    BankAccount   TransactionRepository
                       │
                       ▼
                    Database

The application separates:

- Controller → Handles HTTP requests and responses
- Service → Contains business logic
- Repository → Communicates with the database
- Model → Database entities and enums
- DTO → Request and response data structures
- Exception → Custom exceptions and centralized error handling

---

✨ Features

- ✅ Create a new bank account
- ✅ Get all bank accounts
- ✅ Find an account and check balance
- ✅ Credit/deposit money
- ✅ Debit/withdraw money
- ✅ Transfer money between two accounts
- ✅ Validate insufficient balance
- ✅ Prevent transfer to the same account
- ✅ Store transaction history
- ✅ View all transactions
- ✅ Custom exceptions
- ✅ Global exception handling
- ✅ Jakarta Bean Validation
- ✅ Structured error responses
- ✅ Transaction status using "TransactionType"

---

🛠️ Technologies Used

Technology| Purpose
Java 21| Programming language
Spring Boot 3.5.3| Backend framework
Spring Web| REST APIs
Spring Data JPA| Database operations
Hibernate| ORM
MariaDB / MySQL| Database
Jakarta Validation| Request validation
Lombok| Getters, setters and boilerplate reduction
Maven| Build and dependency management
Git & GitHub| Version control
Postman / cURL| API testing

---

📁 Project Structure

bank-management/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── bank_management/
│   │   │
│   │   │               ├── BankManagementApplication.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   └── AccountController.java
│   │   │               │
│   │   │               ├── dto/
│   │   │               │   ├── CreateAccountRequest.java
│   │   │               │   ├── CreditRequest.java
│   │   │               │   ├── DebitRequest.java
│   │   │               │   ├── TransferRequest.java
│   │   │               │   ├── CreditTransaction.java
│   │   │               │   ├── DebitTransaction.java
│   │   │               │   ├── TransferTransaction.java
│   │   │               │   ├── UserResponse.java
│   │   │               │   ├── TransferResponse.java
│   │   │               │   ├── ExceptionResponseDto.java
│   │   │               │   └── ValidExceptionResponse.java
│   │   │               │
│   │   │               ├── exception/
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── ResourceNotFoundException.java
│   │   │               │   ├── LowBalanceException.java
│   │   │               │   └── SameAccountTransferException.java
│   │   │               │
│   │   │               ├── model/
│   │   │               │   ├── BankAccount.java
│   │   │               │   ├── TransactionHistory.java
│   │   │               │   └── TransactionType.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   ├── AccountRepository.java
│   │   │               │   └── TransactionRepository.java
│   │   │               │
│   │   │               └── service/
│   │   │                   ├── AccountService.java
│   │   │                   └── TransactionService.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md

---

📂 Package Explanation

"controller/"

Contains REST controllers.

"AccountController.java"

This is the main API controller.

It exposes endpoints for:

POST   /accounts/create
GET    /accounts
PUT    /accounts/credit
PUT    /accounts/debit
GET    /accounts/balance
PUT    /accounts/transfer
GET    /accounts/history

The controller receives requests and sends them to "AccountService".

---

"service/"

Contains the application's business logic.

"AccountService.java"

Responsible for:

- Creating accounts
- Finding accounts
- Checking balance
- Credit/debit operations
- Validating account IDs
- Checking sufficient balance
- Transfer operations
- Creating transaction DTOs
- Calling "TransactionService"
- Throwing custom exceptions

"TransactionService.java"

Responsible for:

- Saving credit transactions
- Saving debit transactions
- Saving transfer transactions
- Converting transaction entities into response DTOs
- Returning transaction history

---

"repository/"

Repository interfaces communicate with the database through Spring Data JPA.

"AccountRepository.java"

Works with:

BankAccount

"TransactionRepository.java"

Works with:

TransactionHistory

and can be extended with custom query methods such as user-specific transaction history.

---

📦 DTOs

DTO means Data Transfer Object.

DTOs are used to control what data enters and leaves the API instead of directly using entities everywhere.

---

"CreateAccountRequest"

Used while creating an account.

Example:

{
  "name": "Rahul",
  "amount": 5000
}

Fields:

Field| Type| Validation
name| String| "@NotBlank"
amount| double| "@Positive"

---

"CreditRequest"

Used for depositing money.

{
  "userId": 500001,
  "amount": 1000
}

Fields:

Field| Type| Validation
userId| Long| "@NotNull"
amount| double| "@Positive"

---

"DebitRequest"

Used for withdrawing money.

{
  "userId": 500001,
  "amount": 500
}

Fields:

Field| Type| Validation
userId| Long| "@NotNull"
amount| double| "@Positive"

---

"TransferRequest"

Used for transferring money.

{
  "senderId": 500001,
  "receiverId": 500002,
  "amount": 1000
}

Fields:

Field| Type| Validation
senderId| Long| "@NotNull"
receiverId| Long| "@NotNull"
amount| double| "@Positive"

---

🗃️ Models

"BankAccount"

Main account entity.

BankAccount
│
├── userId
├── userName
└── userBalance

Example:

{
  "userId": 500001,
  "userName": "Rahul",
  "userBalance": 5000.0
}

"userId" is the primary key.

---

"TransactionHistory"

Stores transaction information.

TransactionHistory
│
├── transactionId
├── senderId
├── receiverId
├── amount
├── type
├── total
└── time

Example:

{
  "transactionId": 1,
  "senderId": 500001,
  "receiverId": 500002,
  "amount": 1000.0,
  "type": "TRANSFER",
  "total": 4000.0,
  "time": "2026-09-08T15:30:00"
}

---

🔄 Transaction Types

The project uses the "TransactionType" enum.

public enum TransactionType {
    CREDIT,
    DEBIT,
    TRANSFER,
    FAILED
}

Meaning:

Type| Meaning
"CREDIT"| Money deposited
"DEBIT"| Money withdrawn
"TRANSFER"| Money transferred
"FAILED"| Transaction failed

---

🚨 Exception Handling

The project uses custom exceptions instead of putting every error response directly inside the controller.

"ResourceNotFoundException"

Used when an account does not exist.

Example:

500001 Not Found

HTTP status:

404 NOT_FOUND

---

"LowBalanceException"

Used when the account does not have enough balance.

Example:

Your balance is low

HTTP status:

400 BAD_REQUEST

---

"SameAccountTransferException"

Used when sender and receiver are the same account.

Example:

You are not transfer money in Your same Account

HTTP status:

400 BAD_REQUEST

---

"GlobalExceptionHandler"

"GlobalExceptionHandler" is annotated with:

@RestControllerAdvice

It centrally handles exceptions thrown by controllers/services.

This keeps "AccountController" clean.

---

🌐 API Documentation

Base URL:

http://localhost:8080/accounts

All account APIs start with:

/accounts

---

1️⃣ Create Account

Endpoint

POST /accounts/create

Request

{
  "name": "Rahul",
  "amount": 5000
}

cURL

curl -X POST http://localhost:8080/accounts/create \
  -H "Content-Type: application/json" \
  -d '{"name":"Rahul","amount":5000}'

Successful Response

{
  "userId": 500001,
  "userName": "Rahul",
  "userBalance": 5000.0
}

Status

200 OK

---

2️⃣ Get All Accounts

Endpoint

GET /accounts

cURL

curl http://localhost:8080/accounts

Response

[
  {
    "userId": 500001,
    "userName": "Rahul",
    "userBalance": 5000.0
  },
  {
    "userId": 500002,
    "userName": "Aman",
    "userBalance": 3000.0
  }
]

Status

200 OK

---

3️⃣ Check Account / Balance

Endpoint

GET /accounts/balance?userId={userId}

Example

GET /accounts/balance?userId=500001

cURL

curl "http://localhost:8080/accounts/balance?userId=500001"

Response

{
  "userId": 500001,
  "userName": "Rahul",
  "userBalance": 5000.0
}

If Account Does Not Exist

The service throws:

ResourceNotFoundException

The global exception handler returns a structured error response.

---

4️⃣ Credit / Deposit Money

Credit means account me money add karna.

Endpoint

PUT /accounts/credit

Request

{
  "userId": 500001,
  "amount": 1000
}

cURL

curl -X PUT http://localhost:8080/accounts/credit \
  -H "Content-Type: application/json" \
  -d '{"userId":500001,"amount":1000}'

Suppose old balance:

5000

After credit:

6000

Response

{
  "transactionId": 1,
  "userId": 500001,
  "name": "Rahul",
  "amount": 1000.0,
  "type": "CREDIT",
  "time": "2026-09-08T15:30:00"
}

Status

200 OK

---

5️⃣ Debit / Withdraw Money

Debit means account se money withdraw karna.

Endpoint

PUT /accounts/debit

Request

{
  "userId": 500001,
  "amount": 500
}

cURL

curl -X PUT http://localhost:8080/accounts/debit \
  -H "Content-Type: application/json" \
  -d '{"userId":500001,"amount":500}'

If balance is:

6000

After debit:

5500

Successful Response

{
  "transactionId": 2,
  "userId": 500001,
  "name": "Rahul",
  "amount": 500.0,
  "type": "DEBIT",
  "time": "2026-09-08T15:35:00"
}

Insufficient Balance

If the user tries to withdraw more than the available balance, the service throws:

LowBalanceException

Example:

{
  "status": 400,
  "error": "Bad Request",
  "message": "You have low balance in 500001 your Account!",
  "type": "FAILED"
}

---

6️⃣ Transfer Money

Transfers money from one account to another.

Endpoint

PUT /accounts/transfer

Request

{
  "senderId": 500001,
  "receiverId": 500002,
  "amount": 1000
}

cURL

curl -X PUT http://localhost:8080/accounts/transfer \
  -H "Content-Type: application/json" \
  -d '{"senderId":500001,"receiverId":500002,"amount":1000}'

Transfer Flow

Sender Account
      │
      │  Debit 1000
      ▼
Sender Balance
      │
      │
      ▼
Receiver Account
      │
      │  Credit 1000
      ▼
Receiver Balance

Successful Response

{
  "transactionId": 3,
  "senderId": 500001,
  "receiverId": 500002,
  "name": "Rahul",
  "amount": 1000.0,
  "type": "TRANSFER",
  "time": "2026-09-08T15:40:00"
}

---

Transfer Validations

Sender account does not exist

Throws:

ResourceNotFoundException

Receiver account does not exist

Throws:

ResourceNotFoundException

Sender and receiver are the same

Throws:

SameAccountTransferException

Sender has insufficient balance

Throws:

LowBalanceException

---

7️⃣ Transaction History

Returns all saved transactions.

Endpoint

GET /accounts/history

cURL

curl http://localhost:8080/accounts/history

Example Response

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

Status

200 OK

---

❌ Validation Errors

Request DTOs use Jakarta Bean Validation.

For example:

@NotNull
private Long userId;

@Positive
private double amount;

If an invalid request is sent:

{
  "userId": null,
  "amount": -500
}

"MethodArgumentNotValidException" is generated.

"GlobalExceptionHandler" converts it into a structured response.

Example:

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

---

📋 HTTP Status Codes

Status| Meaning| Example
"200 OK"| Request successful| Credit/Transfer
"400 BAD_REQUEST"| Invalid request/business error| Low balance
"404 NOT_FOUND"| Account/resource not found| Invalid userId
"500 INTERNAL_SERVER_ERROR"| Unexpected server error| Unknown runtime error

---

🔁 Complete Example Flow

A typical banking operation can be tested in this order.

Step 1 — Create Sender

POST /accounts/create

{
  "name": "Rahul",
  "amount": 5000
}

Suppose:

userId = 500001

---

Step 2 — Create Receiver

POST /accounts/create

{
  "name": "Aman",
  "amount": 2000
}

Suppose:

userId = 500002

---

Step 3 — Check Sender Balance

GET /accounts/balance?userId=500001

Balance:

5000

---

Step 4 — Credit Sender

PUT /accounts/credit

{
  "userId": 500001,
  "amount": 1000
}

Balance:

6000

---

Step 5 — Debit Sender

PUT /accounts/debit

{
  "userId": 500001,
  "amount": 500
}

Balance:

5500

---

Step 6 — Transfer

PUT /accounts/transfer

{
  "senderId": 500001,
  "receiverId": 500002,
  "amount": 1000
}

New balances:

Sender   = 4500
Receiver = 3000

---

Step 7 — View History

GET /accounts/history

This returns stored transaction records.

---

⚙️ Database Configuration

Configure your database inside:

src/main/resources/application.properties

Example:

spring.datasource.url=jdbc:mariadb://localhost:3306/bank_management
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

Replace the database username and password with your local configuration.

---

▶️ Running the Project

1. Clone the repository

git clone https://github.com/Rahul01bisht/Bank-Management.git

2. Enter the project

cd Bank-Management

3. Configure the database

Edit:

src/main/resources/application.properties

4. Start Spring Boot

Using Maven Wrapper:

./mvnw spring-boot:run

Or:

mvn spring-boot:run

The application will normally start on:

http://localhost:8080

API base:

http://localhost:8080/accounts

---

🧪 Testing With cURL

Create

curl -X POST http://localhost:8080/accounts/create \
  -H "Content-Type: application/json" \
  -d '{"name":"Rahul","amount":5000}'

Get all

curl http://localhost:8080/accounts

Balance

curl "http://localhost:8080/accounts/balance?userId=500001"

Credit

curl -X PUT http://localhost:8080/accounts/credit \
  -H "Content-Type: application/json" \
  -d '{"userId":500001,"amount":1000}'

Debit

curl -X PUT http://localhost:8080/accounts/debit \
  -H "Content-Type: application/json" \
  -d '{"userId":500001,"amount":500}'

Transfer

curl -X PUT http://localhost:8080/accounts/transfer \
  -H "Content-Type: application/json" \
  -d '{"senderId":500001,"receiverId":500002,"amount":1000}'

History

curl http://localhost:8080/accounts/history

---

🧠 Architecture Explanation

The application follows:

Controller
    ↓
Service
    ↓
Repository
    ↓
Database

For example, when a user transfers money:

PUT /accounts/transfer
          │
          ▼
   AccountController
          │
          ▼
     AccountService
          │
          ├── Check sender
          ├── Check receiver
          ├── Check same account
          ├── Check balance
          ├── Debit sender
          ├── Credit receiver
          │
          ▼
   TransactionService
          │
          ▼
 TransactionRepository
          │
          ▼
       Database

This separation makes the application easier to maintain and extend.

---

🛡️ Error Handling Architecture

Exception occurs
      │
      ▼
Service throws exception
      │
      ▼
GlobalExceptionHandler
      │
      ├── ResourceNotFoundException
      ├── LowBalanceException
      ├── SameAccountTransferException
      ├── MethodArgumentNotValidException
      ├── RuntimeException
      └── Exception
      │
      ▼
Structured JSON Response

This prevents business-error handling from becoming repetitive inside every controller method.

---

📌 Important Design Notes

DTOs

Request DTOs prevent the controller from directly accepting database entities for operations such as credit, debit, and transfer.

Services

Business rules are kept inside "AccountService", rather than inside the controller.

Transactions

Transaction data is handled separately through "TransactionService".

Exceptions

Business errors are represented using custom exceptions and handled centrally by "GlobalExceptionHandler".

Validation

"@Valid" triggers Jakarta Bean Validation before the service receives invalid request data.

---

🚀 Future Improvements

The current project can be extended with:

- [ ] User-specific transaction history
- [ ] Pagination and sorting
- [ ] Account search
- [ ] Account update
- [ ] Spring Security
- [ ] JWT authentication
- [ ] BCrypt password hashing
- [ ] Role-based authorization
- [ ] "@Transactional" transfer processing
- [ ] Swagger / OpenAPI documentation
- [ ] Unit tests
- [ ] Integration tests
- [ ] React frontend
- [ ] Docker deployment
- [ ] Cloud deployment

---

👨‍💻 Author

Rahul Bisht

GitHub:

https://github.com/Rahul01bisht/Bank-Management

---

📄 License

This project is created for learning and development purposes.
