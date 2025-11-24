# Mindfulness Platform - JDBC, Servlet & Console Implementation

A comprehensive mindfulness program built with Java using JDBC for database operations, Servlets for web interface, and a console-based application.

## Features

- ✅ **JDBC Database Integration**: Direct JDBC connections with H2 database
- ✅ **Servlet-based Web Interface**: Full web application using Java Servlets
- ✅ **Console Application**: Command-line interface for all operations
- ✅ **User Management**: Registration, login, and profile management
- ✅ **Meditation Sessions**: Create, view, edit, and delete meditation sessions
- ✅ **Exercises**: Browse mindfulness exercises by category and difficulty
- ✅ **Progress Tracking**: Monitor meditation minutes, sessions, and streaks
- ✅ **Statistics**: View comprehensive statistics and progress reports

## Project Structure

```
src/main/
├── java/com/guvi/mindfulness/
│   ├── jdbc/
│   │   ├── DatabaseConnection.java          # JDBC connection utility
│   │   └── dao/
│   │       ├── UserDAO.java                 # User data access
│   │       ├── MeditationSessionDAO.java   # Meditation session data access
│   │       ├── ExerciseDAO.java            # Exercise data access
│   │       └── ProgressDAO.java             # Progress data access
│   ├── servlet/
│   │   ├── UserServlet.java                 # User management servlet
│   │   ├── MeditationServlet.java          # Meditation session servlet
│   │   ├── ExerciseServlet.java            # Exercise servlet
│   │   ├── ProgressServlet.java            # Progress tracking servlet
│   │   └── DashboardServlet.java           # Dashboard servlet
│   └── console/
│       └── MindfulnessConsoleApp.java        # Console application
├── webapp/
│   ├── WEB-INF/
│   │   ├── web.xml                          # Servlet configuration
│   │   └── views/                           # JSP pages
│   │       ├── layout/                      # Layout templates
│   │       ├── login.jsp                    # Login page
│   │       ├── register.jsp                 # Registration page
│   │       ├── dashboard.jsp                # Dashboard
│   │       ├── meditation/                  # Meditation pages
│   │       ├── exercise/                    # Exercise pages
│   │       └── progress/                    # Progress pages
│   └── index.html                           # Welcome page
└── resources/
    └── db/
        └── schema.sql                       # Database schema

```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Web browser (for web interface)

## Running the Application

### 1. Web Application (Servlet-based)

1. **Build the project:**
   ```bash
   mvn clean install
   ```

2. **Run with Spring Boot:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the web application:**
   - Open your browser and navigate to: `http://localhost:8080`
   - You'll see the welcome page
   - Click "Register" to create an account or "Login" to sign in

4. **Web Interface Features:**
   - **Dashboard**: View statistics and recent sessions
   - **Meditation**: Create, view, edit, and delete meditation sessions
   - **Exercises**: Browse and view mindfulness exercises
   - **Progress**: Track your meditation progress and view statistics
   - **Profile**: View and manage your profile

### 2. Console Application

1. **Run the console application:**
   ```bash
   java -cp target/classes com.guvi.mindfulness.console.MindfulnessConsoleApp
   ```

   Or if using Maven:
   ```bash
   mvn exec:java -Dexec.mainClass="com.guvi.mindfulness.console.MindfulnessConsoleApp"
   ```

2. **Console Menu Options:**
   - **Main Menu** (when not logged in):
     - Login
     - Register
     - View Exercises (Guest)
     - Exit
   
   - **User Menu** (when logged in):
     - View Dashboard
     - Meditation Sessions (Create, View, Edit, Delete)
     - View Exercises
     - View Progress
     - View Statistics
     - Logout

## Database Configuration

The application uses MySQL database. The database is automatically initialized when the application starts.

### Database Connection Details:
- **JDBC URL**: `jdbc:mysql://localhost:3306/mindfulnessdb`
- **Driver**: `com.mysql.cj.jdbc.Driver`
- **Default Username**: `root`
- **Default Password**: (empty)

### MySQL Setup:
1. **Install MySQL Server** (8.0 or higher recommended)
2. **Start MySQL service**
3. **Configure connection** (optional - uses defaults if not set):
   - Set environment variables: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
   - Or update `application.yml` with your MySQL credentials

See `MYSQL_SETUP.md` for detailed setup instructions.

### Database Schema:
The database schema is automatically created when `DatabaseConnection` is first initialized. The schema includes:
- `users` - User accounts
- `roles` - User roles
- `user_roles` - User-role mapping
- `exercises` - Mindfulness exercises
- `meditation_sessions` - Meditation session records
- `progress` - Daily progress tracking

## Key Components

### JDBC Layer

1. **DatabaseConnection**: Singleton class managing database connections
   - Initializes H2 database
   - Creates tables automatically
   - Inserts sample data

2. **DAO Classes**: Data Access Objects using pure JDBC
   - `UserDAO`: User CRUD operations
   - `MeditationSessionDAO`: Meditation session operations
   - `ExerciseDAO`: Exercise operations
   - `ProgressDAO`: Progress tracking operations

### Servlet Layer

1. **UserServlet** (`/user/*`):
   - GET `/user` - Login page
   - GET `/user/register` - Registration page
   - POST `/user/login` - Handle login
   - POST `/user/register` - Handle registration
   - GET `/user/logout` - Logout
   - GET `/user/profile` - User profile

2. **MeditationServlet** (`/meditation/*`):
   - GET `/meditation` - List all sessions
   - GET `/meditation/new` - New session form
   - POST `/meditation` - Create session
   - GET `/meditation/edit/{id}` - Edit session form
   - POST `/meditation/update/{id}` - Update session
   - GET `/meditation/delete/{id}` - Delete session

3. **ExerciseServlet** (`/exercise/*`):
   - GET `/exercise` - List all exercises
   - GET `/exercise/view/{id}` - View exercise details
   - GET `/exercise/category/{category}` - Filter by category
   - GET `/exercise/difficulty/{difficulty}` - Filter by difficulty

4. **ProgressServlet** (`/progress/*`):
   - GET `/progress` - View progress history
   - GET `/progress/statistics` - View statistics

5. **DashboardServlet** (`/dashboard`):
   - GET `/dashboard` - Main dashboard

### Console Application

The console application provides a text-based interface for all operations:
- Interactive menus
- User authentication
- Full CRUD operations for meditation sessions
- Exercise browsing
- Progress tracking
- Statistics viewing

## Usage Examples

### Web Interface

1. **Register a new user:**
   - Navigate to `/user/register`
   - Fill in the registration form
   - Submit to create account

2. **Create a meditation session:**
   - Login and go to `/meditation/new`
   - Fill in session details
   - Save the session

3. **View exercises:**
   - Go to `/exercise`
   - Browse exercises or filter by category/difficulty
   - Click "View" to see details

### Console Interface

1. **Register:**
   ```
   Select an option: 2
   Username: john_doe
   Email: john@example.com
   Password: password123
   First Name: John
   Last Name: Doe
   ```

2. **Create meditation session:**
   ```
   Select an option: 2
   Select an option: 2
   Title: Morning Meditation
   Description: Started my day with mindfulness
   Duration (minutes): 15
   Category: Breathing
   Difficulty Level: Beginner
   Rating (1-5, optional): 5
   Notes: Felt very relaxed
   ```

## API Endpoints (Web Interface)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Welcome page |
| GET | `/user` | Login page |
| GET | `/user/register` | Registration page |
| POST | `/user/login` | Login |
| POST | `/user/register` | Register |
| GET | `/user/logout` | Logout |
| GET | `/user/profile` | User profile |
| GET | `/dashboard` | Dashboard |
| GET | `/meditation` | List sessions |
| GET | `/meditation/new` | New session form |
| POST | `/meditation` | Create session |
| GET | `/meditation/edit/{id}` | Edit session form |
| POST | `/meditation/update/{id}` | Update session |
| GET | `/meditation/delete/{id}` | Delete session |
| GET | `/exercise` | List exercises |
| GET | `/exercise/view/{id}` | View exercise |
| GET | `/progress` | Progress history |
| GET | `/progress/statistics` | Statistics |

## Technologies Used

- **Java 17**: Programming language
- **JDBC**: Database connectivity
- **H2 Database**: In-memory database
- **Java Servlets**: Web framework
- **JSP**: View technology
- **Maven**: Build tool
- **Spring Boot**: Application framework (for running servlets)

## Notes

- The application uses MySQL database for persistent data storage
- Database and tables are automatically created on first run
- Passwords are stored in plain text (for demo purposes). Use password hashing in production
- Session management uses HttpSession for web interface
- The console application maintains state in memory
- Configure MySQL connection using environment variables or `application.yml`

## Troubleshooting

1. **Port 8080 already in use:**
   - Change the port in `application.yml`: `server.port: 8081`

2. **Database connection errors:**
   - Ensure H2 driver is in classpath
   - Check database URL in `DatabaseConnection.java`

3. **JSP not rendering:**
   - Ensure JSP dependencies are in `pom.xml`
   - Check `web.xml` configuration

## Future Enhancements

- [ ] Password encryption (BCrypt)
- [ ] Persistent database support
- [ ] Email notifications
- [ ] Mobile-responsive design improvements
- [ ] Export progress reports
- [ ] Social features (sharing, community)
- [ ] Audio guides integration
- [ ] Reminder notifications

## License

MIT License

## Contact

For support or questions, please contact the development team.

