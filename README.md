# SESC Student App

A **Spring Boot** backend for a student-facing application: REST APIs for registration, login, courses, and enrolments, plus simple static HTML pages (welcome, login, register, dashboard). Data is stored in **MongoDB**.

The runnable Maven project lives in **`SESC-project/`**.

---

## Tech stack

| Layer | Technology |
|--------|------------|
| Runtime | Java **21** |
| Framework | Spring Boot **3.x** (Web MVC, Data MongoDB) |
| Database | **MongoDB** |
| Build | **Maven** (wrapper included: `mvnw` / `mvnw.cmd`) |
| Boilerplate | **Lombok** |

---

## Prerequisites

- **JDK 21** (or compatible; the build targets Java 21 per `pom.xml`). Set `JAVA_HOME` so Maven uses the correct JDK.
- **MongoDB** running and reachable. With default settings, Spring Boot connects to `mongodb://localhost:27017` (no extra config in `application.properties` unless you add it).
- Network access for the **first** build (Maven downloads dependencies).

---

## Database Seeding

When you start the application, a **Course Seeder** automatically checks if there are any courses in the database.
If the `courses` collection is empty, it will automatically populate MongoDB with 3 sample test courses:
1. Introduction to Algorithms
2. Web Development
3. Database Management

This ensures that the application is immediately testable upon startup without requiring manual database inserts.

---

## Project layout

```
SESCfinalProject-Student-App/
├── README.md                 ← this file
└── SESC-project/
    ├── pom.xml
    ├── mvnw, mvnw.cmd        ← Maven Wrapper (no global Maven required)
    └── src/main/
        ├── java/com/example/studentapp/
        │   ├── SescProjectApplication.java
        │   ├── controllers/
        │   │   ├── StudentController.java
        │   │   ├── CourseController.java
        │   │   └── EnrolmentController.java
        │   ├── service/
        │   │   ├── StudentService.java
        │   │   ├── CourseService.java
        │   │   └── EnrolmentService.java
        │   ├── repositories/
        │   ├── entities/
        │   ├── seeder/
        │   │   └── CourseSeeder.java
        │   └── dto/
        └── resources/
            ├── application.properties
            └── static/       ← HTML UI (index, login, register, dashboard)
```

---

## Getting started

### 1. Go to the project folder

```bash
cd SESC-project
```

### 2. Use JDK 21+

Example on macOS (paths vary by install):

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
java -version
```

### 3. Make the Maven wrapper executable (if needed)

If `./mvnw` fails with “permission denied”:

```bash
chmod +x mvnw
```

### 4. Start MongoDB

Start your local MongoDB instance (or point the app at a remote URI — see **Configuration** below).

### 5. Run the application

```bash
./mvnw clean spring-boot:run
```

On Windows:

```cmd
mvnw.cmd clean spring-boot:run
```

### 6. Open the app

By default the server listens on **port 8080**.

| What | URL |
|------|-----|
| Welcome / entry | [http://localhost:8080/](http://localhost:8080/) or `/index.html` |
| Login | `/login.html` |
| Register | `/register.html` |
| Dashboard | `/dashboard.html` |

---

## Build a JAR and run it

```bash
./mvnw -DskipTests package
java -jar target/SESC-project-0.0.1-SNAPSHOT.jar
```

---

## API overview

### Students API (`/api/students`)
| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/students/register` | Register a new student (JSON body) |
| `POST` | `/api/students/login` | Login with `email` and `password` in JSON |
| `PUT` | `/api/students/{id}` | Update student by ID |
| `DELETE` | `/api/students/{id}` | Delete student by ID |

### Courses API (`/api/courses`)
| Method | Path | Description |
|--------|------|-------------|
| `GET`  | `/api/courses` | Get all available courses |
| `GET`  | `/api/courses/{id}` | Get course by ID |

### Enrolments API (`/api/enrolments`)
| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/enrolments` | Enroll a student in a course (requires `studentId`, `courseId`) |
| `GET`  | `/api/enrolments/{studentId}` | Fetch all full course details a student is enrolled in |

*Controllers use `@CrossOrigin` for browser access from other origins during development.*

---

## Configuration

Default application name is set in `SESC-project/src/main/resources/application.properties`. To customize MongoDB, add standard Spring Boot properties, for example:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/yourDatabaseName
```

Or use `spring.data.mongodb.host`, `port`, and `database` as needed.

---

## Tests

```bash
./mvnw test
```

---

## Notes for contributors

- **Security:** Login compares passwords as stored in the database; treat this as a course/demo baseline, not production-ready auth.
- **IDE:** If you use IntelliJ, align the project SDK with **JDK 21+** so it matches the Maven compiler settings in `pom.xml`.
