# Java Profile

A RESTful API for user profile management built with Spring Boot.

## Overview

CRUD de perfis de usuário com autenticação JWT, upload de imagens via S3, observabilidade com Prometheus/Grafana e documentação via OpenAPI.

**Entity: User**

| Field           | Type            |
|-----------------|-----------------|
| name            | String          |
| email           | String          |
| phone           | String          |
| profile_picture | String (S3 URL) |

## Tech Stack

| Category | Technology |
|---|---|
| Framework | Spring Boot 4.0.5 |
| Language | Java 24 |
| Database | PostgreSQL + Spring Data JPA |
| Cache | Redis |
| Security | Spring Security + JWT |
| Storage | AWS S3 |
| Observability | Prometheus + Grafana + Actuator |
| Documentation | OpenAPI (Springdoc) |
| Containerization | Docker |
| CI/CD | GitHub Actions |
| Testing | JUnit + Mockito + Testcontainers |

## Getting Started

### Prerequisites

- Java 24
- Docker & Docker Compose
- Maven

### Running locally

```bash
# Start infrastructure (PostgreSQL, Redis, Prometheus, Grafana)
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

### Environment Variables

| Variable | Required | Default | Description |
|---|---|---|---|
| `DB_HOST` | No | `localhost` | PostgreSQL host |
| `DB_PORT` | No | `5432` | PostgreSQL port |
| `DB_NAME` | No | `profile` | Database name |
| `DB_USERNAME` | No | `profile` | Database user |
| `DB_PASSWORD` | No | `profile` | Database password |
| `REDIS_HOST` | No | `localhost` | Redis host |
| `REDIS_PORT` | No | `6379` | Redis port |
| `JWT_SECRET` | No | *(dev default)* | JWT signing secret |
| `JWT_EXPIRATION_MS` | No | `86400000` | Token expiration (ms) |
| `AWS_S3_BUCKET` | **Yes** | — | S3 bucket name |
| `AWS_REGION` | **Yes** | — | AWS region |

### API Docs

After startup, access the Swagger UI at:
```
http://localhost:8080/java-profile/swagger-ui.html
```

### Metrics

Prometheus metrics exposed at `/actuator/prometheus`.
Grafana available at `http://localhost:3000`.

## Running Tests

```bash
./mvnw test
```

> Testcontainers requires Docker to be running for integration tests.
