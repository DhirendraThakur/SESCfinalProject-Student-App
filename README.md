# SESC Student Microservices System

A distributed student management system comprising three Spring Boot microservices. This system enables student registration, course enrolment, library resource management, and financial oversight with integrated graduation eligibility checks.

## Microservices Overview

The system consists of three interconnected services and a reference implementation:

| Service | Port | Database | Primary Purpose |
| :--- | :--- | :--- | :--- |
| **Student Service** (`SESC-project`) | `8080` | MongoDB (`student-db`) | Core student data, auth, courses, and enrolments. |
| **Library Service** (`library-service`) | `8081` | MongoDB (`library-db`) | Book catalog, student library accounts, and loan tracking. |
| **Finance Service** (`finance-service`) | `8082` | MongoDB (`finance-db`) | Student financial accounts, invoicing, and eligibility tracking. |

---

## Technical Stack

*   **Runtime:** Java 21
*   **Framework:** Spring Boot 3.4.1
*   **Database:** MongoDB (Local instance at `localhost:27017`)
*   **Communication:** REST (using `RestTemplate`)
*   **Frontend:** Pure HTML/JavaScript (embedded in each service)

---

## System Integration Flow

1.  **Student Registration:** When a student registers in the **Student Service (8080)**, it automatically triggers:
    *   An API call to the **Library Service (8081)** to create a library account.
    *   An API call to the **Finance Service (8082)** to create a financial account.
2.  **Course Enrolment:** When a student enrols in a course via the **Student Service**:
    *   An enrolment record is created locally.
    *   An API call is sent to the **Finance Service** to issue a £500 tuition fee invoice.
3.  **Library Interaction:** Library resource management is handled via the Library service, which can issue fines back to the Finance service (integrated logic).
4.  **Graduation Eligibility:** The Student Service provides an endpoint (`/api/students/{id}/graduation`) that queries the Finance Service to verify if there are any outstanding invoices before confirming eligibility.

---

---

## Running Tests

To run the unit tests for the microservices, navigate to the respective service directory and use Maven:

```bash
mvn test
```

### Library Service Unit Tests
We have implemented 14 unit tests in the `library-service` using **JUnit 5** and **Mockito**, covering:
*   **Book Management:** CRUD operations and inventory logic.
*   **Loan Logic:** Borrowing, returning, and automatic late fine calculations.
*   **Account Management:** Automatic account creation for new students.

---

## How to Run the System

To run the full system locally, follow these steps:

### 1. Prerequisites
Ensure you have **MongoDB** running locally on the default port `27017` and **Java 21** installed.

### 2. Start the Microservices
Open separate terminal windows for each service and run:

```bash
# Student Service (Port 8080)
cd SESC-project && mvn spring-boot:run

# Library Service (Port 8081)
cd library-service && mvn spring-boot:run

# Finance Service (Port 8082)
cd finance-service && mvn spring-boot:run
```

### 3. Accessing the UI Portals
Each service provides a frontend for its specific features:
*   **Main Portal:** [http://localhost:8080/](http://localhost:8080/)
*   **Library Portal:** [http://localhost:8081/](http://localhost:8081/)
*   **Finance Portal:** [http://localhost:8082/](http://localhost:8082/)

---

## API Overview (Key Endpoints)

### Student Service (8080)
- `POST /api/students/register`: Create student & register in all services.
- `POST /api/enrolments`: Enrol student in course & issue finance invoice.
- `GET /api/students/{id}/graduation`: Check eligibility to graduate.

### Library Service (8081)
- `POST /api/library/books`: Add books to the catalog.
- `GET /api/library/books`: List available catalog.
- `PUT /api/library/loans/return/{loanId}`: Return book (triggers late fines).

### Finance Service (8082)
- `POST /invoices/`: Issue a new invoice.
- `GET /invoices/student/{id}/outstanding`: Check unpaid balances.
- `PUT /invoices/{id}/pay`: Settle an invoice.

---

## Project Layout

```
SESCfinalProject-Student-App/
├── SESC-project/             # Student Service
├── library-service/          # Library Service
└── finance-service/          # Finance Service
```

> [!NOTE]
> All services use Spring Boot 3.x and MongoDB. The system is designed to demonstrate microservice orchestration and inter-service communication.
