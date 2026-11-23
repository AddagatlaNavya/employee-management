# Employee Management System

A RESTful Employee Management System built using **Java, Spring Boot, Spring Data JPA, and PostgreSQL**. The application provides employee CRUD operations, pagination, sorting, JWT-based authentication, and role-based authorization.

## Key Features

- Employee CRUD operations.
- RESTful API design using versioned endpoints.
- Request validation using Jakarta Bean Validation.
- Pagination and sorting.
- Centralized exception handling.
- JWT-based authentication and authorization.
- Role-based access control (USER and ADMIN).
- Password encryption using BCrypt.
- PostgreSQL database integration.
- Database versioning and migrations using Flyway.
- API documentation using Swagger / OpenAPI.
- Application monitoring using Spring Boot Actuator.
- Docker and Docker Compose support.
- Environment-based configuration for sensitive properties.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 25 | Programming language |
| Spring Boot 4.1.2 | Application framework |
| Spring Web | REST API development |
| Spring Data JPA | Persistence layer |
| Hibernate | ORM |
| PostgreSQL | Relational database |
| Spring Security | Application security |
| JWT | Stateless authentication |
| BCrypt | Password hashing |
| Flyway | Database migrations |
| Maven | Build and dependency management |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies |
| Swagger / OpenAPI | API documentation |
| Spring Boot Actuator | Application monitoring |
| Docker | Containerization |
| Docker Compose | Multi-container orchestration |
| IntelliJ IDEA | Development environment |

## Application Architecture

The application follows a layered architecture to separate responsibilities and improve maintainability.

```text
Client
  |
  v
Controller Layer
  |
  v
Service Layer
  |
  v
Repository Layer
  |
  v
PostgreSQL Database
```

### Package Structure

```text
com.navya.employeemanagement
|
├── config
│   ├── OpenApiConfig
│   ├── SecurityConfig
│   ├── JwtConfig
│   └── AdminBootstrapRunner
|
├── controller
│   ├── EmployeeController
│   └── AuthController
|
├── dto
│   ├── request
│   └── response
|
├── entity
│   ├── Employee
│   └── User
|
├── exception
│   ├── EmployeeNotFoundException
│   ├── DuplicateEmployeeEmailException
│   ├── DuplicateUserEmailException
│   ├── GlobalExceptionHandler
│   └── SecurityErrorHandler
|
├── repository
│   ├── EmployeeRepository
│   └── UserRepository
|
├── service
│   ├── EmployeeService
│   ├── EmployeeServiceImpl
│   ├── AuthService
│   ├── AuthServiceImpl
│   └── JwtService
|
└── EmployeeManagementApplication
```

## REST API Endpoints

Base URL: `/api/v1`

### Employee APIs

Base endpoint: `/api/v1/employees`

| HTTP Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/v1/employees` | Create employee | ADMIN |
| GET | `/api/v1/employees/{id}` | Get employee by ID | USER, ADMIN |
| GET | `/api/v1/employees` | Get all employees | USER, ADMIN |
| PUT | `/api/v1/employees/{id}` | Update employee | ADMIN |
| DELETE | `/api/v1/employees/{id}` | Delete employee | ADMIN |

### Pagination and Sorting

The employee listing API supports:

- Page number
- Page size
- Sorting by supported fields
- Ascending and descending order

Example:

```http
GET /api/v1/employees?page=0&size=10&sortBy=name&direction=asc
```

### Authentication and Authorization

The application implements JWT-based stateless authentication.

- User registration and login.
- JWT access token generation.
- BCrypt password hashing.
- Role-based authorization.
- USER role has read access to employee APIs.
- ADMIN role has read and write access to employee APIs.
- Unauthorized and forbidden requests return structured JSON error responses.

An initial administrator account can be provisioned through environment variables.

## Database and Migrations

**Database:** PostgreSQL

The application uses Spring Data JPA and Hibernate for persistence.

### Database Tables

**employees**
- id
- name
- email
- department

**users**
- id
- email
- password
- role

### Flyway Database Migration

Flyway manages database schema versioning.

- Versioned SQL migration scripts.
- Migration history tracking.
- Schema validation through Hibernate.
- Existing database baseline support.

Hibernate schema generation is configured with `ddl-auto=validate`.

## Exception Handling

Centralized exception handling is implemented using `@RestControllerAdvice`.

The application handles:

- Resource not found – HTTP 404
- Duplicate employee email – HTTP 409
- Duplicate user email – HTTP 409
- Validation failures – HTTP 400
- Invalid requests – HTTP 400
- Invalid login credentials – HTTP 401

Spring Security authentication and authorization errors are handled separately through a custom security error handler.

## API Documentation

Swagger UI provides interactive API documentation.

URL:

```text
http://localhost:8080/swagger-ui.html
```

Features include:
- Endpoint descriptions.
- Request and response schemas.
- HTTP response documentation.
- JWT Bearer token authorization.
- Actuator endpoint documentation.

## Application Monitoring

Spring Boot Actuator is integrated for application monitoring.

Configured endpoints:

| Endpoint | Purpose |
|---|---|
| `/actuator/health` | Application health |
| `/actuator/info` | Application and build information |
| `/actuator/metrics` | Application and JVM metrics |

Health is publicly accessible, while other Actuator endpoints require authentication under the configured security rules.

## Prerequisites

Install the following:

- JDK 25
- Maven (or use the included Maven Wrapper)
- PostgreSQL
- Docker Desktop (for containerized execution)
- IntelliJ IDEA or another Java IDE

## Environment Configuration

Create a `.env` file in the project root.

Use `.env.example` as the reference.

Required variables:

```properties
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
JWT_SECRET=your_base64_encoded_secret
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=your_admin_password
```

**Security:** Never commit `.env` or actual credentials to GitHub.

Configure the environment variables in your IDE run configuration when running locally.

## Running the Application Locally

### 1. Clone the repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd employee-management
```

### 2. Configure PostgreSQL

Create a PostgreSQL database:

```sql
CREATE DATABASE employee_management;
```

### 3. Configure environment variables

Create the `.env` file using `.env.example` as a reference.

### 4. Build the application

Windows:

```bash
mvnw.cmd clean package
```

Linux/macOS:

```bash
./mvnw clean package
```

### 5. Run the application

```bash
mvnw.cmd spring-boot:run
```

Alternatively, run the main application class from IntelliJ IDEA with the required environment variables configured.

Application URL:

```text
http://localhost:8080
```

## Running with Docker Compose

The project includes a Dockerfile and Docker Compose configuration.

Build the application JAR first:

```bash
mvnw.cmd clean package -DskipTests
```

Build the Docker image:

```bash
docker build -t employee-management:1.0 .
```

Start the containers:

```bash
docker compose up -d
```

Check running containers:

```bash
docker compose ps
```

View application logs:

```bash
docker compose logs -f app
```

The Compose configuration runs:

- Spring Boot application container.
- PostgreSQL database container.
- Persistent PostgreSQL volume.

Application: `http://localhost:8080`

PostgreSQL host port: `5433`

## Testing

The project uses:

- JUnit 5 for unit testing.
- Mockito for mocking dependencies.
- Assertions for validating expected behaviour.
- Verification of repository interactions.

Run tests using:

```bash
mvnw.cmd test
```

## Future Enhancements

Potential improvements:

- Integration testing using Testcontainers.
- CI/CD pipeline using GitHub Actions.
- Centralized logging and distributed tracing.
- Prometheus and Grafana monitoring.
- Refresh token and token revocation support.
- Deployment to a cloud platform.
- Microservices decomposition.

## Author

**Navya**

Java Developer | Spring Boot | REST APIs | Hibernate | SQL | Microservices