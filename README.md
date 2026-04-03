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
| Language | Java 26 |
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

- Java 26
- Docker & Docker Compose
- Maven

### Running locally

```bash
# Start infrastructure (PostgreSQL, Redis, Prometheus, Grafana)
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

### API Docs

After startup, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

### Metrics

Prometheus metrics exposed at `/actuator/prometheus`.
Grafana available at `http://localhost:3000`.

## Running Tests

```bash
./mvnw test
```

> Testcontainers requires Docker to be running for integration tests.
