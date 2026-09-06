# 🏦 Bank Management System (Advanced Docs)

Comprehensive, developer-focused documentation for the Bank-Management Spring Boot service.

This README documents the current API surface, DTO shapes, example requests/responses, validation rules, error behavior, and recommended automation for keeping docs in sync with code.

---

## Quick Links
- Source: https://github.com/Rahul01bisht/Bank-Management
- Base API URL (default): `http://localhost:8080/accounts`

---

## Table of Contents
- [Overview](#overview)
- [Usage & Quick Start](#usage--quick-start)
- [Endpoints (detailed)](#endpoints-detailed)
- [DTOs & Models](#dtos--models)
- [Validation & Errors](#validation--errors)
- [Transaction semantics & status codes](#transaction-semantics--status-codes)
- [Testing examples (cURL)](#testing-examples-curl)
- [OpenAPI / Automated docs (recommended)](#openapi--automated-docs-recommended)
- [Developer notes & contribution checklist](#developer-notes--contribution-checklist)
- [Changelog / Release notes](#changelog--release-notes)

---

## Overview
This project is a simple bank management REST API built with Java 17 and Spring Boot. It supports:
- Creating bank accounts
- Crediting (deposit) and debiting (withdraw) operations
- Peer-to-peer transfers between accounts
- Transaction history listing

The implementation follows a layered design (controller -> service -> repository -> DB). DTOs use Jakarta Bean Validation annotations to enforce request constraints.

---

## Usage & Quick Start
1. Clone repo
```bash
git clone https://github.com/Rahul01bisht/Bank-Management.git
cd Bank-Management
```
2. Configure your DB in `src/main/resources/application.properties` (MariaDB/MySQL)
3. Start the app
```bash
./mvnw clean spring-boot:run
```
4. API base: `http://localhost:8080/accounts`

---

## Endpoints (detailed)
All endpoints are under the `/accounts` path.

### 1) Create account
- Method: POST
- URL: `/accounts/create`
- Request DTO: CreateAccountRequest
  - name (String) — @NotBlank
  - amount (double) — @Positive (initial deposit)
- Response: HTTP 200 OK (returns persisted BankAccount entity)

Example request
```json
{
  "name": "John Doe",
  "amount": 1000.0
}
```
Example response
```json
{
  "userId": 1,
  "userName": "John Doe",
  "userBalance": 1000.0
}
```

Notes: Controller currently responds with the BankAccount entity. If you want 201 Created semantics, change the controller to return ResponseEntity.created(...).

---

### 2) Get all accounts
- Method: GET
- URL: `/accounts`
- Response: HTTP 200 OK — list of BankAccount

Example response
```json
[
  {
    "userId": 1,
    "userName": "John Doe",
    "userBalance": 1000.0
  }
]
```

---

### 3) Check balance / account summary
- Method: GET
- URL: `/accounts/balance?userId={id}`
- Response: HTTP 200 OK — account object or summary

Example request
GET `/accounts/balance?userId=1`

Example response
```json
{
  "userId": 1,
  "userName": "John Doe",
  "userBalance": 1000.0
}
```

---

### 4) Credit (deposit)
- Method: PUT
- URL: `/accounts/credit`
- Request DTO: CreditRequest
  - userId (Long) — @NotNull
  - amount (double) — @Positive
- Response: HTTP 200 OK — UserResponse on success; HTTP 400 Bad Request on failure

Example request
```json
{
  "userId": 1,
  "amount": 500.0
}
```
Example success response
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

### 5) Debit (withdraw)
- Method: PUT
- URL: `/accounts/debit`
- Request DTO: DebitRequest
  - userId (Long) — @NotNull
  - amount (double) — @Positive
- Response: HTTP 200 OK — UserResponse on success; HTTP 400 Bad Request on failure

Example request
```json
{
  "userId": 1,
  "amount": 200.0
}
```
Example failure (insufficient funds) — HTTP 400
```json
{
  "transactionId": null,
  "userId": 1,
  "name": "John Doe",
  "amount": 200.0,
  "type": "FAILED",
  "time": "2026-09-06T13:00:00",
  "message": "Insufficient funds"
}
```

---

### 6) Transfer
- Method: PUT
- URL: `/accounts/transfer`
- Request DTO: TransferRequest
  - senderId (Long) — @NotNull
  - receiverId (Long) — @NotNull
  - amount (double) — @Positive
- Response: HTTP 200 OK — TransferResponse on success; HTTP 400 Bad Request on failure

Example request
```json
{
  "senderId": 1,
  "receiverId": 2,
  "amount": 25.0
}
```
Example success response
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
Example failure response (bad request / failed transfer)
```json
{
  "transactionId": null,
  "senderId": 1,
  "receiverId": 2,
  "name": "John Doe",
  "amount": 25.0,
  "type": "FAILED",
  "time": "2026-09-06T13:02:00",
  "message": "Receiver not found"
}
```

---

### 7) Transaction history
- Method: GET
- URL: `/accounts/history`
- Response: HTTP 200 OK — list of TransactionHistory

Example response
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

## DTOs & Models (current code)
This section mirrors the DTO and model classes found in `src/main/java/com/example/bank_management`.

- CreateAccountRequest
  - name: String (@NotBlank)
  - amount: double (@Positive)

- CreditRequest
  - userId: Long (@NotNull)
  - amount: double (@Positive)

- DebitRequest
  - userId: Long (@NotNull)
  - amount: double (@Positive)

- TransferRequest
  - senderId: Long (@NotNull)
  - receiverId: Long (@NotNull)
  - amount: double (@Positive)

- UserResponse
  - transactionId: Long
  - userId: Long
  - name: String
  - amount: double
  - type: TransactionType
  - time: LocalDateTime

- TransferResponse
  - transactionId: Long
  - senderId: Long
  - receiverId: Long
  - name: String
  - amount: double
  - type: TransactionType
  - time: LocalDateTime

- BankAccount (entity)
  - userId: Long (PK)
  - userName: String
  - userBalance: double

- TransactionHistory (entity)
  - transactionId: Long (PK)
  - senderId: Long
  - receiverId: Long
  - amount: double
  - type: TransactionType (Enum stored as String)
  - total: double
  - time: LocalDateTime (set at @PrePersist)

---

## Validation & Errors
- DTOs use Jakarta Validation (annotated with `@NotBlank`, `@NotNull`, `@Positive`).
- Validation failures will produce HTTP 400 responses (Spring Boot's default BindingResult/MethodArgumentNotValid handling). Consider adding a `@ControllerAdvice` to unify error JSON shapes.
- Business errors (insufficient funds, missing accounts) are modeled in the service layer and returned from controllers as DTOs with `type == TransactionType.FAILED` and a 400 status.

Suggested unified error JSON (ControllerAdvice-friendly)
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Insufficient funds",
  "path": "/accounts/debit"
}
```

---

## Transaction semantics & status codes
- Successful operations: 200 OK (controller currently returns 200 for create as well)
- Validation or business failures: 400 Bad Request
- Consider adding: 201 Created for create, 404 Not Found for missing accounts, 500 for unexpected server errors

---

## Testing examples (cURL)
Create account
```bash
curl -X POST http://localhost:8080/accounts/create \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","amount":1000.0}'
```

Credit
```bash
curl -X PUT http://localhost:8080/accounts/credit \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":500.0}'
```

Debit
```bash
curl -X PUT http://localhost:8080/accounts/debit \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":200.0}'
```

Transfer
```bash
curl -X PUT http://localhost:8080/accounts/transfer \
  -H "Content-Type: application/json" \
  -d '{"senderId":1,"receiverId":2,"amount":25.0}'
```

History
```bash
curl http://localhost:8080/accounts/history
```

---

## OpenAPI / Automated docs (recommended)
To keep documentation in sync with DTOs and controllers, I strongly recommend exposing an OpenAPI document from the running application and using a generator in CI to produce human-friendly docs.

1) Add springdoc to `pom.xml` dependencies
```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.1.0</version>
</dependency>
```
This exposes `/v3/api-docs` and Swagger UI at `/swagger-ui.html`.

2) Add a GitHub Actions workflow to generate docs and commit them (example below).

Example workflow `.github/workflows/generate-api-docs.yml` (high-level)
```yaml
name: Generate API docs
on:
  push:
    paths:
      - 'src/**'
      - 'pom.xml'
jobs:
  generate-docs:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'
      - run: mvn -DskipTests package -q
      - run: nohup java -jar target/*.jar & sleep 8
      - run: |
          for i in {1..30}; do
            if curl -sSf http://localhost:8080/v3/api-docs -o openapi.json; then exit 0; fi
            sleep 2
          done
          exit 1
      - run: |
          docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate \
            -i /local/openapi.json -g markdown -o /local/docs
      - run: |
          git config user.name "github-actions[bot]"
          git config user.email "actions@github.com"
          git add docs README.md || true
          git commit -m "chore(docs): update API docs" || echo "No changes"
          git push
```

Notes:
- The workflow above runs the application in CI to fetch live OpenAPI JSON and then generates Markdown docs to `docs/`. You can adapt it to replace README sections or produce a separate `docs/API.md`.
- If starting the app in CI proves flaky, consider using maven plugins or a compile-time generator that introspects annotations (less common with Spring but possible).

---

## Developer notes & contribution checklist
When you change DTOs/controllers/services, update the docs as follows:
1. If you have automated docs enabled: ensure build passes and the workflow can run.
2. If not automated: update README sections under `DTOs & Models` and the endpoint payloads.
3. Add or update integration tests (recommended) that exercise request/response shapes and error cases.
4. Add `@Operation` and `@Schema` annotations (springdoc) to controllers and DTOs to improve generated docs.

Suggested PR checklist (add to `.github/PULL_REQUEST_TEMPLATE.md` if desired):
- [ ] Code compiles and unit tests pass
- [ ] DTO renames/fields documented in README or OpenAPI
- [ ] Added/updated API examples (curl/Postman)
- [ ] If behavior changes: added migration notes in CHANGELOG

---

## Changelog / Release notes
Keep a `CHANGELOG.md` for major behavioral changes (schema, DTO renames, response shape changes). For small teams, reference PRs in changelog entries.

---

## Contact / Author
* **Rahul Bisht** — https://github.com/Rahul01bisht

---

If you want, I can now:
- Add the springdoc dependency to `pom.xml` and create the GitHub Actions workflow to auto-generate docs.
- Add a `ControllerAdvice` to normalize validation and business error JSON shapes.
- Generate a separate `docs/API.md` and link it from README (recommended if docs are large).

Tell me which of the above follow-ups you want me to implement and I'll commit them.