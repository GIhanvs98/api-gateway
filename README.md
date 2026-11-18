# AI-Powered Enterprise Knowledge Assistant - API Gateway

## Overview
The **API Gateway** (`api-gateway`) is the central ingress proxy for the **AI-Powered Enterprise Knowledge Assistant** microservices platform. Built using **Spring Boot 3.x** and **Spring Cloud Gateway (Reactive WebFlux)**, it routes client requests to downstream microservices discovered via **Netflix Eureka**, handles CORS policy for web clients, and injects distributed correlation IDs for end-to-end tracing.

---

## Architectural Decisions & Principles

1. **Non-Blocking Reactive Engine**:
   - Built on Spring Cloud Gateway and Project Reactor (Netty), providing high throughput and low resource footprint for concurrent I/O operations.
2. **Dynamic Service Routing via Eureka**:
   - Uses `lb://<service-name>` URI schemes to dynamically balance requests across registered instances of `auth-service`, `document-service`, and `ai-service`.
3. **Distributed Request Tracing**:
   - Implements `CorrelationIdGlobalFilter` to ensure every incoming HTTP request carries a unique `X-Correlation-ID` header across all downstream service boundaries.
4. **CORS Governance**:
   - Enforces centralized Cross-Origin Resource Sharing policy for frontend web applications (e.g. Next.js / Vite).
5. **Observability**:
   - Integrates **Spring Boot Actuator** exposing health checks (`/actuator/health`) and application metadata (`/actuator/info`).

---

## Route Configuration

| Route ID | Path Pattern | Target Destination | Purpose |
|---|---|---|---|
| `auth-service-route` | `/api/v1/auth/**` | `lb://auth-service` | Authentication, JWT, and user management |
| `document-service-route` | `/api/v1/documents/**` | `lb://document-service` | Document ingestion, metadata, and chunking |
| `ai-service-route` | `/api/v1/ai/**` | `lb://ai-service` | RAG, vector search, and LLM querying |

---

## Environment Variables & `.env` Reference

Copy `.env.example` to create your local `.env`:
```bash
cp .env.example .env
```

| Variable | Description | Default Value |
|---|---|---|
| `SERVER_PORT` | HTTP port for the API Gateway | `8080` |
| `SPRING_APPLICATION_NAME` | Service application name | `api-gateway` |
| `EUREKA_HOSTNAME` | Hostname for Eureka service discovery | `localhost` |
| `EUREKA_SERVICE_URL` | Eureka Server registry endpoint | `http://localhost:8761/eureka/` |
| `AUTH_SERVICE_URI` | Target URI for Auth Service | `lb://auth-service` |
| `DOCUMENT_SERVICE_URI` | Target URI for Document Service | `lb://document-service` |
| `AI_SERVICE_URI` | Target URI for AI Service | `lb://ai-service` |
| `CORS_ALLOWED_ORIGINS` | Comma-separated allowed origins | `http://localhost:3000,http://localhost:5173` |
| `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE` | Actuator exposed endpoints | `health,info` |
| `MANAGEMENT_ENDPOINT_HEALTH_SHOW_DETAILS` | Actuator health detail level | `always` |

---

## Project Structure

```text
api-gateway/
├── .env                  # Local environment configurations (git-ignored)
├── .env.example          # Environment configuration template
├── .gitignore            # Git exclusion rules
├── pom.xml               # Maven POM with Spring Boot & Cloud dependencies
├── README.md             # Gateway documentation
└── src/
    ├── main/
    │   ├── java/
    │   │   └── lk/
    │   │       └── gihanvs/
    │   │           └── knowledge/
    │   │               └── gateway/
    │   │                   ├── ApiGatewayApplication.java
    │   │                   ├── config/
    │   │                   │   └── CorsConfig.java
    │   │                   └── filter/
    │   │                       └── CorrelationIdGlobalFilter.java
    │   └── resources/
    │       └── application.yml
    └── test/
        └── java/
            └── lk/
                └── gihanvs/
                    └── knowledge/
                        └── gateway/
                            └── ApiGatewayApplicationTests.java
```

---

## Setup & Running Instructions

### Prerequisites
- **JDK 21** installed and configured (`JAVA_HOME` pointing to Java 21)
- **Apache Maven 3.8+** installed
- **Eureka Discovery Server** running on port `8761`

### 1. Build the Gateway
From within the `api-gateway` directory:
```bash
cd /home/gihanvs/projects/final/api-gateway
mvn clean package
```

### 2. Run the Service
Run directly using the Spring Boot Maven plugin:
```bash
mvn spring-boot:run
```

Or run the packaged executable JAR:
```bash
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

### 3. Verify Health & Routes
```bash
# Actuator health check
curl http://localhost:8080/actuator/health

# Actuator info endpoint
curl http://localhost:8080/actuator/info
```
