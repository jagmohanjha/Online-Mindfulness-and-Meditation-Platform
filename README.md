# Mindfulness & Meditation Platform (GUVI project)

Backend mini-project that demonstrates Core Java, JDBC, Servlets, and database design for a mindfulness learning platform. The submission matches the Review-1 rubric (problem understanding, OOP usage, JDBC CRUD, and servlet integration).

---

## 1. Problem Understanding & Solution Design

| Item | Details |
|------|---------|
| **Problem Statement** | Busy learners struggle to stay consistent with meditation. They need a lightweight backend that stores courses, tracks guided sessions, and generates insights without a heavy UI. |
| **Solution Approach** | Build a layered Java app. Servlets handle HTTP input, services enforce mindfulness rules, DAOs interact with MySQL through JDBC and PreparedStatement, and models capture the domain language (User, Course, Session). |
| **Target Users** | Learners needing guided sessions, mindfulness coaches configuring courses, and reviewers who want to see clean Java patterns. |
| **Key Features** | User registration, scheduling sessions, reflection updates, course catalogue, session history retrieval, and a bootstrap schema for MySQL. |

### High-level Flow / Architecture

```
Browser / Postman
        │
        ▼
Servlets (RegisterServlet, MindfulnessSessionServlet)
        │
        ▼
Services (UserService, MindfulnessSessionService)
        │
        ▼
DAO Layer (UserDAO, MindfulnessSessionDAO)
        │
        ▼
MySQL (via DBConnection + PreparedStatement)
```

### Folder Structure (simplified)

```
mindfulness/
├── pom.xml
├── README.md
├── MYSQL_SETUP.md
├── src/main/java/com/guvi/mindfulness
│   ├── dao/
│   ├── exception/
│   ├── jdbc/
│   ├── model/
│   ├── service/
│   └── servlet/
├── src/main/resources/db/schema.sql
└── src/main/webapp/WEB-INF/web.xml
```

---

## 2. Core Java Concepts 

| Concept | Where it is Implemented |
|---------|-------------------------|
| **Classes & Objects** | `User`, `MindfulnessCourse`, `MindfulnessSession`, `UserDAO`, `UserService`, etc. |
| **Encapsulation** | Every model has private fields with getters/setters ensuring controlled access. |
| **Inheritance** | `MindfulnessCourse` and `MindfulnessSession` both extend `MindfulnessActivity`. |
| **Polymorphism** | Both subclasses implement the `MindfulnessPractice` interface. Services operate on the interface when generating summaries. |
| **Packages** | `model`, `dao`, `service`, `servlet`, `exception`, `jdbc` show clean separation. |
| **Interfaces** | `MindfulnessPractice` demonstrates abstraction of course/session behaviour. |
| **Exception Handling** | `ValidationException` and `DataAccessException` guard the service layer. |
| **Collections** | `User` keeps a `List<MindfulnessSession>` to track completed sessions. |

---

## 3. Database Integration (JDBC) 

- `DBConnection.java`: singleton helper that exposes `getConnection()` and `initializeSchema()` while hiding the JDBC URL and credentials.
- DAOs only use `PreparedStatement` to avoid SQL injection.
- CRUD examples:
  - **Insert** – `UserDAO.insert()` and `MindfulnessSessionDAO.insert()`
  - **Retrieve** – `UserDAO.findAll()`, `MindfulnessSessionDAO.findByUser()`
  - **Update** – `UserDAO.update()`, `MindfulnessSessionDAO.updateReflection()`
  - **Delete** – `UserDAO.delete()` and `MindfulnessSessionDAO.delete()`
- SQL schema lives in `src/main/resources/db/schema.sql` and can be executed from MySQL Workbench or via `DBConnection.initializeSchema()` during bootstrap.

---

## 4. Servlets & Web Integration 

- Servlet classes: `RegisterServlet` and `MindfulnessSessionServlet` (both annotated + declared in `web.xml`).
- Each servlet calls the service layer to keep controllers thin.
- Basic HTTP methods implemented:
  - `RegisterServlet#doPost` – creates a learner profile.
  - `MindfulnessSessionServlet#doPost` – schedules a session.
  - `MindfulnessSessionServlet#doGet` – fetches session history for a user.
- `src/main/webapp/WEB-INF/web.xml` wires URL patterns for review purposes even though annotations are present.

---

## 5. Environment Setup & Execution

1. **Prerequisites**: Java 17, Maven 3.8+, MySQL 8.x, Tomcat 10/Jetty 12 (any Jakarta EE 10 compatible container).
2. **Clone + Build**
   ```bash
   git clone <repo>
   cd mindfulness
   mvn clean package
   ```
3. **Database**
   ```bash
   mysql -u root -p < src/main/resources/db/schema.sql
   ```
   or set `DB_HOST`, `DB_USER`, `DB_PASSWORD` and let `DBConnection.initializeSchema()` run once.
4. **Deploy**
   - Copy `target/mindfulness-platform.war` into Tomcat’s `webapps`.
   - Hit `http://localhost:8080/mindfulness-platform/index.jsp` to verify deployment.
5. **Test Endpoints**
   ```bash
   curl -X POST http://localhost:8080/mindfulness-platform/api/register \
        -d "fullName=Divya&email=divya@example.com&password=secret1&focusArea=Stress"

   curl -X POST http://localhost:8080/mindfulness-platform/api/sessions \
        -d "userId=1&title=Morning Calm&description=Breathing practice&category=Breath" \
        -d "difficulty=Beginner&scheduledAt=2025-11-24T07:30:00&durationMinutes=10&reflectionNotes=Felt calm"

   curl "http://localhost:8080/mindfulness-platform/api/sessions?userId=1"
   ```

---

## 6. Database Tables

- `users`: learner profile (name, email, focus area)
- `mindfulness_courses`: curated courses to recommend
- `mindfulness_sessions`: tracks each scheduled/completed session with duration + reflections

Indexes and FK constraints are defined inside `schema.sql`.

---

## 7. Future Enhancements

- Add JSP/React front-end (optional for Review-1).
- Gamification metrics (streaks, badges).
- Email reminders via JavaMail.
- Reporting dashboard for mentors.

---

### 🎆 **Core Development Team**

| Avatar | Contributor | Role | GitHub Profile |
|--------|-------------|------|----------------|
| 👨‍💻 | **Divyankar** | JDBC Integration, Servlets, Database Tables | [![GitHub](https://img.shields.io/badge/GitHub-Divyankar-blue?style=flat&logo=github)](https://github.com/Divyankar7) |
| ⚙️ | **Jagmohan Jha** |Project Overview & Core Java Concepts | [![GitHub](https://img.shields.io/badge/GitHub-jagmohan-jha-blue?style=flat&logo=github)](https://github.com/jagmohanjha) |
| 💻 | **Atul Chaudhary** |Environment Setup,Testing,Future Enhancements | [![GitHub](https://img.shields.io/badge/GitHub-Atul--Chaudhary-blue?style=flat&logo=github)](https://github.com/labsilk85-art) |

## Project Evaluation & Implementation Guidelines

This document summarizes the expectations and evaluation criteria for the Mindfulness & Meditation Platform project. It is intended for students implementing or extending the project and for reviewers assessing it.

---

## 1. Core Feature Implementation

- **Complete core functionalities**:
  - User registration and basic profile management.
  - Creating, scheduling, and viewing mindfulness sessions.
  - Managing or listing mindfulness courses/activities as per your assignment scope.
- **Follow the layered architecture** already used in the project:
  - Servlets handle HTTP requests and responses.
  - Services contain business logic and validation.
  - DAOs handle JDBC and database CRUD.

---

## 2. Error Handling & Robustness

- **Use proper error handling** to prevent crashes:
  - Validate inputs before using them.
  - Catch and wrap low-level exceptions (SQL, connection failures) in custom exceptions such as `DataAccessException`.
  - Use `ValidationException` (or similar) for invalid user or session data.
- **Fail gracefully**:
  - Do not expose raw stack traces to users.
  - Return clear, user-friendly error messages or status codes from servlets.

---

## 3. Integration of Components

- **Ensure smooth interaction** between:
  - `Servlet` → `Service` → `DAO` → `DBConnection` → MySQL.
- **Keep responsibilities separate**:
  - Servlets should not contain SQL or heavy business logic.
  - Services should not directly manage JDBC connections.
  - DAOs should focus on `PreparedStatement` use and result mapping.

---

## 4. Event Handling & Processing

- In this backend project, “events” are mainly **HTTP requests**:
  - Each meaningful action (register, create session, list sessions) should map to a clean URL and HTTP method (`GET`/`POST`).
  - Avoid unnecessary repeated database calls inside a single request.
- If you extend the project with front-end code (JSP/JavaScript/React):
  - Use **efficient event listeners and delegation**.
  - Avoid attaching many duplicate listeners to the same elements.
  - Minimize heavy operations inside frequently triggered events.

---

## 5. Data Validation

- **Server-side validation (mandatory)**:
  - Required fields: names, emails, passwords, session titles, and scheduled date/time.
  - Constraints:
    - Valid email format.
    - Reasonable password length and basic strength rules (as per your rubric).
    - Valid date/time formats for sessions.
    - Allowed difficulty levels or categories.
- **Client-side validation (recommended where UI is present)**:
  - Use HTML5 form validation or small JavaScript checks to block obviously invalid data before submission.

---

## 6. Code Quality & Innovation

- **Code Quality**:
  - Use meaningful class, method, and variable names.
  - Keep classes cohesive; avoid “God classes” that do everything.
  - Follow consistent formatting and indentation.
  - Add JavaDoc or comments where the intent is not obvious.
- **Innovation** (optional but encouraged):
  - Add small but useful features (e.g., streak counters, average session length, recommended next session).
  - Improve validation rules or introduce new insights/summary endpoints.
  - Extend the model in a way that still respects the existing architecture.

---

## 7. Project Documentation

- Maintain and update:
  - **`README.md`** – high-level description, architecture, setup steps, and main features.
  - **`MYSQL_SETUP.md`** – database creation and configuration steps.
  - Any additional docs for APIs or usage notes, if you add them.
- Use clear headings, bullet points, and examples where helpful.

---

## 8. Submission & GitHub Requirements

- **Organized files**:
  - Preserve the structured folder hierarchy (`model`, `dao`, `service`, `servlet`, `exception`, `jdbc`, `resources`, `webapp`, etc.).
  - Do not mix Java files with configuration or SQL files in the same folder.
- **GitHub repository**:
  - Push the complete project to GitHub.
  - Ensure the repository has:
    - A clear `README.md` describing project structure and features.
    - All required source files, configuration, and SQL scripts.
- **Test & validate before submission**:
  - Run through the main flows (register user, create session, list sessions, etc.).
  - Fix any runtime errors, obvious bugs, or broken SQL.
  - Verify database connectivity and schema setup in your environment.
- **Submit on time**:
  - Share the GitHub repository link with your mentor/reviewer before the deadline specified in your course or assignment.

---

By following these guidelines, students demonstrate strong skills in **data validation, error handling, component integration, code quality, innovation, and documentation**, which are the main focus areas for this project’s evaluation.

for any enquiry
##Contact;
gmail: jagmohank978@gmail.com

