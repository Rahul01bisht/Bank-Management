🏦 Bank Management System

A simple Bank Management System REST API built using Java and Spring Boot.
This project provides basic banking operations such as creating accounts, checking balances, crediting and debiting money, and viewing all bank accounts.

🚀 Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MariaDB / MySQL
- Maven
- REST API
- Git & GitHub

📌 Current Features

1. Create Bank Account

Creates a new bank account.

Endpoint:

POST /accounts/create

2. Find All Accounts

Returns all registered bank accounts.

Endpoint:

GET /accounts

3. Check Balance

Checks the balance of a particular user.

Endpoint:

GET /accounts/balance?userId={userId}

4. Credit Money

Adds money to an existing bank account.

Endpoint:

PUT /accounts/credit

Example request:

{
  "userId": 1,
  "userBalance": 500
}

5. Debit Money

Removes money from an existing bank account.

Endpoint:

PUT /accounts/debit

📂 Project Structure

bank-management/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── bank_management/
│       │               ├── controller/
│       │               │   └── AccountController.java
│       │               │
│       │               ├── model/
│       │               │   └── BankAccount.java
│       │               │
│       │               ├── repository/
│       │               │   └── AccountRepository.java
│       │               │
│       │               └── service/
│       │                   └── AccountService.java
│       │
│       └── resources/
│           └── application.properties
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md

🔄 Basic API Flow

Client
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Database

🗄️ Database

The project uses MariaDB/MySQL with Spring Data JPA for database operations.

The "BankAccount" entity stores account information such as:

- User ID
- User Name
- Account Balance
- Other account details

🧪 API Testing

The APIs can be tested using tools such as:

- Postman
- Insomnia
- Thunder Client
- cURL

📈 Future Features

Planned improvements:

- Transaction History
- Money Transfer
- Account Update
- Account Delete
- Input Validation
- Global Exception Handling
- Authentication & Authorization
- Admin Features

👨‍💻 Project Status

Currently in development 🚧

Basic account and balance management features are implemented. More banking features will be added gradually.

---

Author

Rahul01bisht

Built with ☕ Java + Spring Boot
