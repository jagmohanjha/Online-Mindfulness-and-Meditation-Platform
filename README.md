# Mindfulness & Meditation Platform (GUVI Review-1)

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

## Contact


For questions ping `support@guvi.com`.
