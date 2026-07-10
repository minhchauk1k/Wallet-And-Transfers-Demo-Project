# Wallet Transfer System

A modern fintech application for managing customer wallets and processing money transfers with advanced features like distributed transactions, idempotency, and event-driven architecture.

## Overview

This project implements a complete wallet and transfer system with the following capabilities:
- Customer and wallet management
- Secure money transfers with double-entry ledger accounting
- Distributed transaction handling
- Idempotency support for reliable API calls
- Event-driven architecture using Kafka
- Redis-based caching and distributed locking

## Tech Stack

- **Framework**: Spring Boot 4.1.0
- **Language**: Java 21
- **Database**: PostgreSQL 17
- **Cache**: Redis 7
- **Message Queue**: Apache Kafka 7.7.0
- **Build Tool**: Maven
- **ORM**: Spring Data JPA with Hibernate

## Project Structure

```
src/
├── main/
│   ├── java/com/fintech/wallet/transfer/system/
│   │   ├── adapter/              # Adapter layer (controllers, repositories, etc.)
│   │   │   ├── in/               # Input adapters (REST controllers)
│   │   │   └── out/              # Output adapters (persistence, cache, events)
│   │   ├── application/          # Application/use case layer
│   │   │   ├── service/          # Business services
│   │   │   ├── port/             # Port interfaces
│   │   │   └── constant/         # Application constants
│   │   ├── domain/               # Domain layer
│   │   │   ├── model/            # Domain models
│   │   │   ├── constant/         # Domain constants
│   │   │   └── exception/        # Domain exceptions
│   │   └── config/               # Configuration classes
│   └── resources/
│       ├── application.yml       # Application configuration
│       └── db/migration/         # Flyway database migrations
└── test/                         # Test files
```

## Architecture

This project follows **Hexagonal Architecture (Ports & Adapters)**:

- **Domain Layer**: Core business logic and entities
- **Application Layer**: Use cases and services
- **Adapter Layer**: External interfaces (REST, persistence, messaging)

## Prerequisites

- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- (Optional) Java Virtual Threads support for concurrency testing (requires Java 21)

## Getting Started

### 1. Start Infrastructure (PostgreSQL, Redis, Kafka)

```bash
docker-compose up -d
```

This will start:
- **PostgreSQL** on port 5432
- **Redis** on port 6379
- **Kafka** on port 9092
- **Kafka UI** on port 8080 (for monitoring)
- **Redis Commander** on port 8081 (for monitoring)

### 2. Configure Database

Use an `.env` file and environment variables for secrets. Ensure `DB_*`, `REDIS_*`, and Kafka/Outbox related environment variables are set (see `src/main/resources/application.yml` for keys). Do NOT commit secrets to the repo.

### 3. Build and Run the Application

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Key Features

### 1. Customer Management
- Create and retrieve customers

### 2. Wallet Management
- Create wallets for customers
- Check wallet balance
- Track wallet status

### 3. Money Transfers
- Transfer money between wallets
- Idempotent transfer requests (using idempotency keys)
- Distributed locking to prevent race conditions
- Double-entry ledger for financial accuracy
- Resilience and retry support to handle optimistic locking conflicts

### 4. Transaction Tracking
- Complete transaction history
- Ledger entries for audit trails
- Outbox pattern for reliable event publishing (new OutboxEvent entity & migration)

### 5. Concurrency Testing (Developer)
- A dedicated endpoint exists to stress-test concurrency using Java virtual threads:
  - `GET /api/v1/test-concurrency-safety` — runs many virtual-thread tasks that invoke the transfer API and returns a summary of results.
  - Requires Java 21 (Project Loom virtual threads). Use only in development or test environments; do NOT run in production.

### 6. Caching & Performance
- Redis caching for frequently accessed data
- Distributed locks for concurrent transfer safety

### 7. Event Publishing
- Kafka-based event streaming for asynchronous processing
- Outbox pattern for transactional event delivery

## API Endpoints

### Customers
- `POST /customers` - Create a new customer
- `GET /customers/{id}` - Get customer details

### Wallets
- `POST /wallets` - Create a new wallet
- `GET /wallets/{id}` - Get wallet details

### Transactions
- `POST /transactions` - Transfer money between wallets
- `GET /transactions/{id}` - Get transaction details

## Configuration

### Environment Setup

Create an `.env` file in the project root with your credentials (never commit this file):

```env
DB_URL=your_database_url
DB_USERNAME=your_username
DB_PASSWORD=your_secure_password
REDIS_HOST=your_redis_host
REDIS_PORT=your_redis_port
```

Update `src/main/resources/application.yml` to use environment variables:

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
```

### Configuration Parameters

- **Redis Idempotency Lock TTL**: 5 seconds (configurable)
- **Redis Cache TTL**: 24 hours (configurable)
- **Kafka Compression**: Snappy
- **Kafka Log Retention**: 24 hours

> **⚠️ Security Note**: Never commit sensitive credentials. Use environment variables or secure secret management solutions in production.

## Development

### Running Tests
```bash
mvn test
```

### Database Migrations
Flyway migrations are located in `src/main/resources/db/migration/`
- To enable migrations, set `spring.flyway.enabled: true` in `application.yml`

## Monitoring

- **Kafka UI**: http://localhost:8080
- **Redis Commander**: http://localhost:8081

## Project Metadata

- **Group ID**: com.fintech
- **Artifact ID**: wallet.transfer.system
- **Version**: 0.0.1-SNAPSHOT

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is part of the Fintech Wallet Transfer System.
