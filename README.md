# Pontrix Backend

A **Spring Boot 3** RESTful API backend for the Pontrix Reward Points Platform — a loyalty rewards system that allows companies to manage users, staff, rewards, and point redemptions.

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Core language |
| Spring Boot | 3.2.5 | Application framework |
| Spring Security | — | Authentication & authorization |
| Spring Data JPA | — | Database ORM |
| PostgreSQL | — | Relational database |
| JWT (JJWT) | 0.11.5 | Token-based authentication |
| MapStruct | 1.5.5 | DTO ↔ Entity mapping |
| Lombok | 1.18.30 | Boilerplate reduction |
| AWS SDK (S3) | 2.20.26 | DigitalOcean Spaces file storage |
| Maven | — | Build tool |
| Docker | — | Containerization |

---

## Project Structure

```
src/main/java/com/phara/pontrix_backend/
├── PontrixBackendApplication.java   # Entry point
├── audit/                           # Auditing (createdAt, updatedAt)
├── config/                          # App configuration (CORS, Beans)
├── domain/                          # JPA entities
│   ├── Admin.java
│   ├── Company.java
│   ├── Order.java
│   ├── Redeem.java
│   ├── Reward.java
│   ├── Staff.java
│   └── User.java
├── enumeration/                     # Enums (roles, statuses)
├── exception/                       # Global exception handling
├── features/                        # Feature modules (controllers, services, DTOs)
│   ├── admin/
│   ├── auth/
│   ├── companies/
│   ├── orders/
│   ├── redemptions/
│   ├── rewards/
│   ├── staff/
│   └── user/
├── mapper/                          # MapStruct mappers
├── security/                        # JWT filter, UserDetailsService
├── service/                         # Shared service interfaces
└── utils/                           # Utility classes
```

---

## Features

- **Authentication** — JWT-based login for Admin, Staff, and User roles
- **Role-Based Access Control** — Separate permissions for Admin, Staff, and User
- **Company Management** — CRUD operations for companies (Admin only)
- **User Management** — User registration, profile, and point balance management
- **Staff Management** — Staff accounts tied to companies
- **Rewards Management** — Create and manage redeemable rewards
- **Redemptions** — Users request redemptions; staff approve or reject
- **Orders** — Order tracking and history
- **File Uploads** — Image uploads via DigitalOcean Spaces (S3-compatible)

---

## Getting Started

### Prerequisites

- **Java 21** — [Download](https://adoptium.net/)
- **Maven 3.8+** — [Download](https://maven.apache.org/)
- **PostgreSQL** — Local or remote instance

### 1. Clone the Repository

```bash
git clone <repository-url>
cd pontrix-backend
```

### 2. Configure the Database

Edit `src/main/resources/application.properties`:

```properties
# Local PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/pointrix_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.jdbc.time_zone=Asia/Bangkok
```

### 3. Configure DigitalOcean Spaces (File Storage)

```properties
do.spaces.key=YOUR_ACCESS_KEY
do.spaces.secret=YOUR_SECRET_KEY
do.spaces.endpoint=https://sgp1.digitaloceanspaces.com
do.spaces.region=sgp1
do.spaces.bucket=your-bucket-name
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The server starts on **http://localhost:8080**.

---

## Build

```bash
./mvnw clean package -DskipTests
java -jar target/pontrix-backend-0.0.1-SNAPSHOT.jar
```

---

## Docker

### Build the Image

```bash
docker build -t pontrix-backend .
```

### Run the Container

```bash
docker run -p 8080:8080 pontrix-backend
```

> **Note:** Update `application.properties` with your production database credentials before building the Docker image, or inject them as environment variables at runtime.

---

## API Overview

All endpoints are prefixed with `/api/v1`. Authentication is required for most routes via `Authorization: Bearer <token>`.

| Module | Base Path | Roles |
|---|---|---|
| Auth | `/api/v1/auth` | Public |
| Admin | `/api/v1/admin` | ADMIN |
| Companies | `/api/v1/companies` | ADMIN |
| Staff | `/api/v1/staff` | ADMIN, STAFF |
| Users | `/api/v1/users` | ADMIN, USER |
| Rewards | `/api/v1/rewards` | ADMIN, STAFF, USER |
| Redemptions | `/api/v1/redemptions` | STAFF, USER |
| Orders | `/api/v1/orders` | STAFF, USER |

### Authentication Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/auth/login` | Login and receive JWT token |
| POST | `/api/v1/auth/register` | Register a new user |

---

## Environment Variables (Production)

For production deployment, use environment variables instead of hardcoding credentials:

| Variable | Description |
|---|---|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection string |
| `SPRING_DATASOURCE_USERNAME` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `DO_SPACES_KEY` | DigitalOcean Spaces access key |
| `DO_SPACES_SECRET` | DigitalOcean Spaces secret key |
| `DO_SPACES_BUCKET` | Spaces bucket name |
| `DO_SPACES_REGION` | Spaces region (e.g. `sgp1`) |
| `DO_SPACES_ENDPOINT` | Spaces endpoint URL |

---

## Deployment (DigitalOcean App Platform)

1. Push the repository to GitHub.
2. In DigitalOcean App Platform, create a new app and connect your GitHub repo.
3. Set the **Run Command** to:
   ```
   java -jar target/pontrix-backend-0.0.1-SNAPSHOT.jar
   ```
   Or use the provided `Dockerfile` for container-based deployment.
4. Add all required environment variables in the App Platform settings.
5. Set the HTTP port to **8080**.

---

## Related Projects

Frontend Repository: https://github.com/sopharaa/Reward-Frontend-Aditi
