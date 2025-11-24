# MySQL Setup Guide for Mindfulness Platform

This guide will help you set up MySQL database for the Mindfulness Platform application.

## Prerequisites

1. **MySQL Server** installed and running
   - MySQL 8.0 or higher recommended
   - Download from: https://dev.mysql.com/downloads/mysql/

2. **MySQL Workbench** (optional, for GUI management)
   - Download from: https://dev.mysql.com/downloads/workbench/

## Database Setup

### Option 1: Automatic Setup (Recommended)

Call `DBConnection.initializeSchema()` once (e.g., from a bootstrap servlet or a simple main method) and it executes the SQL in `src/main/resources/db/schema.sql`.

### Option 2: Manual Setup

1. **Connect to MySQL:**
   ```bash
   mysql -u root -p
   ```

2. **Create the database:**
   ```sql
   CREATE DATABASE mindfulnessdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Create a user (optional, for better security):**
   ```sql
   CREATE USER 'mindfulness_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON mindfulnessdb.* TO 'mindfulness_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

4. **Run the schema script:**
   ```bash
   mysql -u root -p mindfulnessdb < src/main/resources/db/schema.sql
   ```

## Configuration

### Environment Variables

You can configure the database connection using environment variables:

```bash
# Windows (PowerShell)
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
$env:DB_NAME="mindfulnessdb"
$env:DB_USER="root"
$env:DB_PASSWORD="your_password"

# Linux/Mac
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=mindfulnessdb
export DB_USER=root
export DB_PASSWORD=your_password
```

### Using System Properties

Add JVM arguments while running tests or deploying:

```bash
mvn clean package -DDB_USER=root -DDB_PASSWORD=your_password
```

## Default Configuration

If no environment variables are set, the application uses these defaults:

- **Host**: `localhost`
- **Port**: `3306`
- **Database**: `mindfulnessdb`
- **Username**: `root`
- **Password**: (empty)

## Connection String Parameters

The connection string includes these important parameters:

- `useSSL=false` - Disables SSL (use `true` in production with proper certificates)
- `allowPublicKeyRetrieval=true` - Allows retrieval of public key for authentication
- `serverTimezone=UTC` - Sets timezone to UTC
- `createDatabaseIfNotExist=true` - Automatically creates database if it doesn't exist

## Testing the Connection

### Using MySQL Command Line

```bash
mysql -u root -p -e "SHOW DATABASES LIKE 'mindfulnessdb';"
```

### Using the Application

1. Build and deploy the WAR to Tomcat/Jetty.
2. Watch the logs for `Mindfulness Platform` boot messages or add a `System.out.println` after calling `DBConnection.initializeSchema()` in a bootstrap listener.
3. If you see errors, check:
   - MySQL server is running
   - Credentials are correct
   - User has proper permissions

## Troubleshooting

### Error: "Access denied for user"

**Solution:**
- Check username and password
- Ensure user has privileges:
  ```sql
  GRANT ALL PRIVILEGES ON mindfulnessdb.* TO 'your_user'@'localhost';
  FLUSH PRIVILEGES;
  ```

### Error: "Unknown database 'mindfulnessdb'"

**Solution:**
- Create the database manually, or
- Ensure `createDatabaseIfNotExist=true` is in the connection URL

### Error: "Communications link failure"

**Solution:**
- Check if MySQL server is running:
  ```bash
  # Windows
  net start MySQL80
  
  # Linux
  sudo systemctl status mysql
  ```
- Verify host and port are correct
- Check firewall settings

### Error: "The server time zone value 'XYZ' is unrecognized"

**Solution:**
- Add `serverTimezone=UTC` to connection URL (already included)
- Or set MySQL timezone:
  ```sql
  SET GLOBAL time_zone = '+00:00';
  ```

## Production Recommendations

1. **Use a dedicated database user** with limited privileges
2. **Enable SSL** for secure connections
3. **Use connection pooling** (already configured in Spring Boot)
4. **Set up regular backups**
5. **Use environment variables** for sensitive credentials
6. **Monitor database performance**

## Backup and Restore

### Backup:
```bash
mysqldump -u root -p mindfulnessdb > mindfulnessdb_backup.sql
```

### Restore:
```bash
mysql -u root -p mindfulnessdb < mindfulnessdb_backup.sql
```

## Additional Resources

- MySQL Documentation: https://dev.mysql.com/doc/
- MySQL Connector/J: https://dev.mysql.com/doc/connector-j/
- Spring Boot Data Source Configuration: https://docs.spring.io/spring-boot/docs/current/reference/html/data.html

