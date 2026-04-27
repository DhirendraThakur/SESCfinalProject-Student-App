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

## How to Run the System

To run the full system locally, follow these steps in order:

### 1. Prerequisites
Ensure you have **MongoDB** running locally on the default port `27017`.

### 2. Start the Microservices
Open three separate terminal windows/tabs and run each service:

**Terminal 1: Student Service**
```bash
cd SESC-project
./mvnw spring-boot:run
```

**Terminal 2: Library Service**
```bash
cd library-service
./mvnw spring-boot:run
```

**Terminal 3: Finance Service**
```bash
cd finance-service
./mvnw spring-boot:run
```

### 3. Accessing the UI Portals

Each service provides a simple frontend for its specific features:

*   **Main Application Portal:** [http://localhost:8080/](http://localhost:8080/)
    *   Register/Login to manage courses and check graduation eligibility.
*   **Library Portal:** [http://localhost:8081/](http://localhost:8081/)
    *   Manage books and check library account status.
*   **Finance Portal:** [http://localhost:8082/](http://localhost:8082/)
    *   View invoices and pay outstanding balances.

---

## API Overview (Key Endpoints)

### Student Service (8080)
- `POST /api/students/register`: Create student & register in all services.
- `POST /api/enrolments`: Enrol student in course & issue finance invoice.
- `GET /api/students/{id}/graduation`: Check eligibility to graduate.

### Library Service (8081)
- `POST /api/library/accounts/register`: create internal library record.
- `GET /api/library/books`: List available catalog.

### Finance Service (8082)
- `POST /accounts/`: Create finance account.
- `POST /invoices/`: Issue a new invoice (outstanding by default).
- `GET /accounts/student/{id}`: Detailed account balance and status check.
- `PUT /invoices/{id}/pay`: Settle an invoice.

---

## Project Layout

```
SESCfinalProject-Student-App/
├── SESC-project/             # Student Service (Source on Github)
├── library-service/          # Library Service (Source on Github)
└── finance-service/          # Finance Service (Local reference only)
```

> [!NOTE]
> For this version of the project, the **finance-service** is a local-only reference implementation and is NOT committed to the public repository.
