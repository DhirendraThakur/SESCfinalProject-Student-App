# SESC Student App

A **Spring Boot** backend for a student-facing application: REST APIs for registration and login, plus simple static HTML pages (welcome, login, register, dashboard). Data is stored in **MongoDB**.

The runnable Maven project lives in **`SESC-project/`**.

---

## Tech stack

| Layer | Technology |
|--------|------------|
| Runtime | Java **21** |
| Framework | Spring Boot **4.x** (Web MVC, Data MongoDB) |
| Database | **MongoDB** |
| Build | **Maven** (wrapper included: `mvnw` / `mvnw.cmd`) |
| Boilerplate | **Lombok** |

---

## Prerequisites

- **JDK 21** (or compatible; the build targets Java 21 per `pom.xml`). Set `JAVA_HOME` so Maven uses the correct JDK.
- **MongoDB** running and reachable. With default settings, Spring Boot connects to `mongodb://localhost:27017` (no extra config in `application.properties` unless you add it).
- Network access for the **first** build (Maven downloads dependencies).

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
        │   ├── controllers/StudentController.java
        │   ├── service/StudentService.java
        │   ├── repositories/Student_Repositories.java
        │   └── entities/StudentEntities.java
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
./mvnw spring-boot:run
```

On Windows:

```cmd
mvnw.cmd spring-boot:run
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

Base path: **`/api/students`**

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/students/register` | Register a new student (JSON body) |
| `POST` | `/api/students/login` | Login with `email` and `password` in JSON |
| `PUT` | `/api/students/{id}` | Update student by ID |

`StudentController` uses `@CrossOrigin` for browser access from other origins during development.

MongoDB collection: **`students`** (see `@Document` on `StudentEntities`).

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

---

## License

See your course or team policy; no license is specified in this repository by default.
