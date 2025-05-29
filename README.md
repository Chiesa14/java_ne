# Employee Management System

A Spring Boot application for managing employee records, employment details, payroll, and deductions.

## Features

- **Authentication & Authorization**
  - JWT-based authentication
  - Role-based access control (ADMIN, MANAGER, EMPLOYEE)
  - OTP-based password reset

- **Employee Management**
  - Employee registration and profile management
  - Employment history tracking
  - Department and position management

- **Payroll Management**
  - Automatic payroll generation
  - Payslip generation and approval workflow
  - Salary deductions and tax calculations

- **Deductions Management**
  - Configurable deduction types
  - Tax calculations
  - Deduction history tracking

## Tech Stack

- Java 17
- Spring Boot 3.5.0
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway for database migrations
- Swagger/OpenAPI for API documentation
- JWT for authentication
- Maven for dependency management

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd employee-management-system
   ```

2. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE employee_management;
   ```

3. Configure the application:
   - Copy `src/main/resources/application.properties.example` to `src/main/resources/application.properties`
   - Update the database connection details and other configurations

4. Build the application:
   ```bash
   mvn clean install
   ```

5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8080`

## API Documentation

Once the application is running, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

### Key API Endpoints

#### Authentication
- `POST /api/auth/register` - Register new employee
- `POST /api/auth/login` - User login
- `POST /api/auth/update-password` - Update password

#### Employee Management
- `GET /api/employees` - Get all employees (ADMIN/MANAGER)
- `GET /api/employees/{code}` - Get employee by code
- `POST /api/employees` - Create new employee (ADMIN)
- `PUT /api/employees/{code}` - Update employee (ADMIN/MANAGER)
- `DELETE /api/employees/{code}` - Delete employee (ADMIN)

#### Employment Management
- `GET /api/employments/my` - Get my employments
- `GET /api/employments` - Get all employments (ADMIN/MANAGER)
- `GET /api/employments/{id}` - Get employment by ID
- `POST /api/employments` - Create employment (ADMIN/MANAGER)
- `PUT /api/employments/{id}` - Update employment (ADMIN/MANAGER)
- `DELETE /api/employments/{id}` - Delete employment (ADMIN)

#### Payroll Management
- `GET /api/payroll/my` - Get my payslips
- `GET /api/payroll` - Get all payslips (ADMIN/MANAGER)
- `POST /api/payroll/generate` - Generate payroll (ADMIN)
- `PUT /api/payroll/{id}/approve` - Approve payslip (MANAGER)

#### Deductions Management
- `GET /api/deductions` - Get all deductions (ADMIN/MANAGER)
- `GET /api/deductions/{code}` - Get deduction by code
- `POST /api/deductions` - Create deduction (ADMIN)
- `PUT /api/deductions/{code}` - Update deduction (ADMIN)
- `DELETE /api/deductions/{code}` - Delete deduction (ADMIN)

## Database Migrations

The application uses Flyway for database migrations. Migration scripts are located in `src/main/resources/db/migration/`.

## Security

- JWT tokens are used for authentication
- Passwords are encrypted using BCrypt
- Role-based access control is implemented
- Sensitive endpoints are protected

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
