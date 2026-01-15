# Techsloyd Inventory Management System - Setup & Installation Guide

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.12-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12%2B-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9%2B-red.svg)](https://maven.apache.org/)

## 📋 Table of Contents
1. [System Requirements](#system-requirements)
2. [Pre-Installation Checklist](#pre-installation-checklist)
3. [Step-by-Step Installation](#step-by-step-installation)
4. [Database Setup](#database-setup)
5. [Project Configuration](#project-configuration)
6. [Building the Project](#building-the-project)
7. [Running the Application](#running-the-application)
8. [Verification & Testing](#verification--testing)
9. [IDE Setup (Optional)](#ide-setup-optional)
10. [Troubleshooting](#troubleshooting)
11. [Next Steps](#next-steps)

---

## 🖥 System Requirements

### Minimum Requirements
| Component | Minimum | Recommended |
|-----------|---------|-------------|
| **OS** | Windows 7, macOS 10.12, Ubuntu 16.04 | Windows 10/11, macOS 12+, Ubuntu 20.04+ |
| **RAM** | 4 GB | 8 GB or more |
| **Storage** | 2 GB | 5 GB or more |
| **Java JDK** | 17+ | 21 (LTS) |
| **PostgreSQL** | 12+ | 15+ |
| **Maven** | 3.9+ | 3.9.11+ |

### Supported Operating Systems
- ✅ Windows 10/11 (64-bit)
- ✅ macOS 10.12+ (Intel/Apple Silicon)
- ✅ Linux (Ubuntu, Debian, CentOS, RHEL)

---

## ✅ Pre-Installation Checklist

Before starting the installation, verify the following:

### Checklist Items
- [ ] System meets minimum requirements
- [ ] At least 2 GB free disk space available
- [ ] Administrator/sudo access on the system
- [ ] Internet connection for downloading dependencies
- [ ] Git installed (for cloning repository)
- [ ] Port 8080 is available (Spring Boot default)
- [ ] Port 5432 is available (PostgreSQL default)

### Pre-requisite Software to Install

1. **Java JDK 17+**
2. **PostgreSQL 12+**
3. **Git** (optional, for cloning)
4. **Maven 3.9+** (or use bundled wrapper)

---

## 🚀 Step-by-Step Installation

### Phase 1: Install Java JDK

#### Windows Installation

**Step 1.1: Download Java JDK**
1. Go to [Oracle JDK Downloads](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
2. Select JDK 17 (or later LTS version)
3. Choose Windows x64 installer
4. Accept license and download

**Step 1.2: Install Java**
1. Run the downloaded `.exe` file
2. Click "Next" through the wizard
3. Accept default installation path (e.g., `C:\Program Files\Java\jdk-17`)
4. Complete installation

**Step 1.3: Verify Installation**
```bash
java -version
javac -version
```

Expected output:
```
java version "17.0.x" LTS
Java(TM) SE Runtime Environment (build 17.0.x+...)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.x+...)
```

#### macOS Installation

Using Homebrew (recommended):
```bash
# Install Homebrew if not installed
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java
brew install openjdk@17

# Create symlink (if required)
sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

Verify:
```bash
java -version
```

#### Linux Installation (Ubuntu/Debian)

```bash
# Update package manager
sudo apt-get update

# Install OpenJDK 17
sudo apt-get install openjdk-17-jdk

# Verify installation
java -version
```

---

### Phase 2: Install PostgreSQL

#### Windows Installation

**Step 2.1: Download PostgreSQL**
1. Go to [PostgreSQL Downloads](https://www.postgresql.org/download/windows/)
2. Download PostgreSQL 15+ (64-bit)
3. Run installer

**Step 2.2: Configure Installation**
- Installation directory: `C:\Program Files\PostgreSQL\15`
- Port: `5432` (default)
- Superuser password: `postgres` (change this in production!)
- Create a strong password and note it

**Step 2.3: Verify Installation**
```bash
# Open Command Prompt
psql -U postgres -W

# Enter password when prompted
# If connection successful, you'll see:
# postgres=#
```

#### macOS Installation

Using Homebrew:
```bash
# Install PostgreSQL
brew install postgresql@15

# Start PostgreSQL service
brew services start postgresql@15

# Verify
psql --version
```

#### Linux Installation (Ubuntu/Debian)

```bash
# Update package manager
sudo apt-get update

# Install PostgreSQL
sudo apt-get install postgresql postgresql-contrib

# Start PostgreSQL service
sudo systemctl start postgresql

# Verify
sudo -u postgres psql --version
```

---

### Phase 3: Clone the Repository

#### Using Git (Recommended)

```bash
# Navigate to desired directory
cd C:\Projects          # Windows
cd ~/Projects           # macOS/Linux

# Clone repository
git clone https://github.com/yourusername/Techsloyd.git

# Navigate to project
cd Techsloyd
```

#### Manual Download

1. Go to repository URL
2. Click "Code" → "Download ZIP"
3. Extract ZIP to desired location
4. Open terminal in extracted folder

---

### Phase 4: Database Setup

#### Option A: Using pgAdmin GUI (Easiest)

**Step 4A.1: Open pgAdmin**
- Windows: Search for "pgAdmin" in Start menu
- macOS: Applications → pgAdmin 4
- Linux: Browser → http://localhost/pgadmin4

**Step 4A.2: Create Database**
1. Right-click "Databases"
2. Select "Create" → "Database"
3. Enter name: `api`
4. Click "Save"

**Step 4A.3: Run Schema Script**
1. Right-click database `api`
2. Select "Query Tool"
3. Open file: `Techsloyd/Schema.sql`
4. Copy and paste entire content
5. Click "Execute Query"

**Step 4A.4: Verify Tables**
1. Expand `api` database
2. Expand "Schemas" → "public" → "Tables"
3. Should see 7 tables:
   - barcodes
   - categories
   - product_variants
   - products
   - variant_combinations
   - variant_option_values
   - variant_options

#### Option B: Using Command Line (Advanced)

**Windows:**
```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE api;

# Connect to database
\c api

# View file content and execute
\i 'C:/Techsloyd/Schema.sql'

# Verify tables
\dt

# Exit
\q
```

**macOS/Linux:**
```bash
# Create database
createdb -U postgres api

# Run schema file
psql -U postgres -d api -f ~/path/to/Schema.sql

# Connect and verify
psql -U postgres -d api -c "\dt"
```

**Expected Output:**
```
                List of relations
 Schema |         Name          | Type  |  Owner
--------+-----------------------+-------+----------
 public | barcodes              | table | postgres
 public | categories            | table | postgres
 public | product_variants      | table | postgres
 public | products              | table | postgres
 public | variant_combinations  | table | postgres
 public | variant_option_values | table | postgres
 public | variant_options       | table | postgres
(7 rows)
```

---

## ⚙️ Project Configuration

### Step 5: Update Database Credentials

**File Location:**
```
Techsloyd/backend-api/src/main/resources/application.properties
```

**Current Configuration:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/api
spring.datasource.username=postgres
spring.datasource.password=Anand@2004#
```

**Update for Your System:**
1. Open `application.properties` in text editor
2. Find the properties section
3. Update password to your PostgreSQL password:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/api
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD_HERE
```

**Optional: Change Database Connection Details**
```properties
# If PostgreSQL is on different host/port
spring.datasource.url=jdbc:postgresql://192.168.1.100:5432/api

# If using different database name
spring.datasource.url=jdbc:postgresql://localhost:5432/my_database_name
```

### Step 6: Review JPA Configuration

Verify these settings (usually correct by default):

```properties
# Don't auto-create/drop tables - we have Schema.sql
spring.jpa.hibernate.ddl-auto=validate

# Show SQL for debugging (optional)
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# PostgreSQL dialect
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

⚠️ **IMPORTANT:** Keep `ddl-auto=validate` - it ensures the schema matches our entities.

---

## 🔨 Building the Project

### Step 7: Build with Maven

Navigate to project directory:
```bash
# Windows
cd C:\Users\YourName\Desktop\Techsloyd
cd backend-api

# macOS/Linux
cd ~/Desktop/Techsloyd
cd backend-api
```

### Build Command

**Windows (Using Wrapper):**
```bash
mvnw.cmd clean install
```

**macOS/Linux (Using Wrapper):**
```bash
chmod +x mvnw           # Make executable (first time only)
./mvnw clean install
```

**Alternative (If Maven installed globally):**
```bash
mvn clean install
```

### What Each Command Does:
- `clean`: Removes previous build artifacts
- `install`: Compiles code, runs tests, creates JAR

### Build Output
```
[INFO] Scanning for projects...
[INFO] 
[INFO] -------< com.inventory:backend-api >--------
[INFO] Building backend-api 0.0.1-SNAPSHOT
[INFO] --------
[INFO] 
[INFO] --- maven-clean-plugin:3.x.x:clean (default-clean) @ backend-api ---
[INFO] 
[INFO] --- maven-compiler-plugin:3.x.x:compile (default-compile) @ backend-api ---
[INFO] Building jar: .../target/backend-api-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] BUILD SUCCESS
[INFO] 
[INFO] Total time: 23.456 s
[INFO] Finished at: 2026-01-16T10:30:00+05:30
[INFO] Finished at: 2026-01-16T10:30:00Z
```

### If Build Fails:

**Error: "Tests failed"**
```bash
# Skip tests (if they're failing for reasons unrelated to your setup)
mvnw.cmd clean install -DskipTests
```

**Error: "Could not find or load main class"**
```bash
# Clean cache and rebuild
mvnw.cmd clean compile
```

---

## 🚀 Running the Application

### Step 8: Start the Application

#### Option A: Using Maven Wrapper (Recommended)

**Windows:**
```bash
cd backend-api
mvnw.cmd spring-boot:run
```

**macOS/Linux:**
```bash
cd backend-api
./mvnw spring-boot:run
```

#### Option B: Running JAR Directly

```bash
# Navigate to backend-api folder
cd backend-api

# Run the JAR file
java -jar target/backend-api-0.0.1-SNAPSHOT.jar
```

#### Option C: Using IDE (See IDE Setup section below)

### Expected Startup Output

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::               (v3.4.12)

2026-01-16 10:30:00.123 INFO 12345 --- [main] c.i.b.BackendApiApplication : Starting BackendApiApplication v0.0.1-SNAPSHOT with PID 12345
2026-01-16 10:30:02.456 INFO 12345 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat initialized with port(s): 8080 (http)
2026-01-16 10:30:03.789 INFO 12345 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8080 (http) with context path ''
2026-01-16 10:30:04.012 INFO 12345 --- [main] c.i.b.BackendApiApplication : Started BackendApiApplication in 3.456 seconds
```

**✅ Application is now running on:** `http://localhost:8080`

To **stop the application:**
- Press `Ctrl + C` in the terminal

---

## ✔️ Verification & Testing

### Step 9: Test API Endpoints

#### Using cURL (Command Line)

**Test 1: Get Category Tree**
```bash
curl http://localhost:8080/api/categories/tree
```

Expected response (should be empty initially):
```json
[]
```

**Test 2: Get All Products**
```bash
curl http://localhost:8080/api/products?page=0&size=10
```

Expected response:
```json
{
  "content": [],
  "pageable": {...},
  "totalElements": 0,
  "size": 10,
  "number": 0
}
```

#### Using Postman (GUI - Recommended)

**Step 9A.1: Download Postman**
- Go to [Postman Download](https://www.postman.com/downloads/)
- Install for your OS

**Step 9A.2: Create Collection**
1. Open Postman
2. Click "New" → "Collection"
3. Name: "Techsloyd API"

**Step 9A.3: Add Requests**
1. Click "Add Request"
2. Name: "Get Categories"
3. Method: `GET`
4. URL: `http://localhost:8080/api/categories/tree`
5. Click "Send"

**Test Endpoints:**

| Name | Method | URL | Body |
|------|--------|-----|------|
| Get Categories | GET | http://localhost:8080/api/categories | - |
| Create Category | POST | http://localhost:8080/api/categories | See below |
| Get Products | GET | http://localhost:8080/api/products | - |
| Create Product | POST | http://localhost:8080/api/products | See below |

**Sample Request Bodies:**

Create Category:
```json
{
  "name": "Electronics",
  "slug": "electronics",
  "description": "Electronic devices",
  "isActive": true,
  "position": 1
}
```

Create Product:
```json
{
  "category": {
    "id": "category-id-from-previous-request"
  },
  "name": "Wireless Mouse",
  "sku": "MOUSE-001",
  "price": 29.99,
  "costPrice": 15.00,
  "stockLevel": 50,
  "status": "active"
}
```

#### Using VS Code REST Client Extension

**Step 9B.1: Install Extension**
1. Open VS Code
2. Click Extensions icon
3. Search "REST Client"
4. Install by humao.rest-client

**Step 9B.2: Create Test File**

Create `test-api.http`:
```http
### Get all categories
GET http://localhost:8080/api/categories

### Get category tree
GET http://localhost:8080/api/categories/tree

### Get all products
GET http://localhost:8080/api/products?page=0&size=10

### Create category
POST http://localhost:8080/api/categories
Content-Type: application/json

{
  "name": "Electronics",
  "slug": "electronics",
  "description": "Electronic devices",
  "isActive": true
}

### Create product
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "category": {
    "id": "your-category-id"
  },
  "name": "Wireless Mouse",
  "sku": "MOUSE-001",
  "price": 29.99,
  "costPrice": 15.00,
  "stockLevel": 50,
  "status": "active"
}
```

**Click "Send Request" above each request to test**

---

## 🖥 IDE Setup (Optional)

### IntelliJ IDEA Setup

**Step 10A.1: Open Project**
1. Launch IntelliJ IDEA
2. File → Open
3. Navigate to `Techsloyd` folder
4. Click "Open"

**Step 10A.2: Configure JDK**
1. Wait for Maven import to complete
2. File → Project Structure
3. Project SDK: Select JDK 17+
4. Click "Apply" → "OK"

**Step 10A.3: Configure Database Connection** (Optional)
1. View → Tool Windows → Database
2. Click "+" → "Data Source"
3. Select PostgreSQL
4. Configure:
   - Host: `localhost`
   - Port: `5432`
   - Database: `api`
   - User: `postgres`
   - Password: your PostgreSQL password
5. Click "Test Connection"

**Step 10A.4: Run Application**
1. Open `src/main/java/com/inventory/backend_api/BackendApiApplication.java`
2. Right-click → "Run 'BackendApiApplication'"
3. Or press `Shift + F10` (Windows) / `Ctrl + R` (Mac)

### VS Code Setup

**Step 10B.1: Install Extensions**
1. Extension Pack for Java (Microsoft)
2. Spring Boot Extension Pack (VMware)
3. Database (mongodb-js, cweijan.dbvisualizer)

**Step 10B.2: Open Project**
1. File → Open Folder
2. Select `Techsloyd`

**Step 10B.3: Debug/Run**
1. Press `F5` to start debugging
2. Or use Maven in Terminal

### Eclipse IDE Setup

**Step 10C.1: Open Project**
1. File → Import → Existing Maven Projects
2. Root Directory: `Techsloyd`
3. Click "Finish"

**Step 10C.2: Configure JDK**
1. Right-click project → Properties
2. Java Build Path → JRE System Library
3. Select JDK 17+
4. Click "Apply and Close"

**Step 10C.3: Run Application**
1. Right-click project → Run As → Spring Boot App
2. Or: Right-click BackendApiApplication.java → Run As → Java Application

---

## 🐛 Troubleshooting

### Problem 1: "PostgreSQL Connection Refused"

**Error Message:**
```
java.sql.SQLException: Connection to localhost:5432 refused
```

**Solutions:**
1. Verify PostgreSQL is running:
   ```bash
   # Windows - Check Services
   services.msc
   # Find "PostgreSQL" and ensure it's started
   
   # macOS
   brew services list | grep postgresql
   
   # Linux
   sudo systemctl status postgresql
   ```

2. Start PostgreSQL:
   ```bash
   # Windows - In Services, start PostgreSQL
   
   # macOS
   brew services start postgresql@15
   
   # Linux
   sudo systemctl start postgresql
   ```

3. Verify connection manually:
   ```bash
   psql -h localhost -U postgres -d api
   # If prompted for password, enter your PostgreSQL password
   ```

### Problem 2: "Database 'api' Does Not Exist"

**Error Message:**
```
PSQLException: ERROR: database "api" does not exist
```

**Solution:**
Follow [Database Setup section](#phase-4-database-setup) again:
1. Create database named `api`
2. Run `Schema.sql` script
3. Verify tables exist

### Problem 3: "Port 8080 Already in Use"

**Error Message:**
```
The Tomcat connector configured to listen on port 8080 failed to start
```

**Solutions:**

**Option A: Find and kill process using port 8080**

Windows:
```bash
netstat -ano | findstr :8080
taskkill /PID <process-id> /F
```

macOS/Linux:
```bash
lsof -i :8080
kill -9 <process-id>
```

**Option B: Change Spring Boot port**

Edit `application.properties`:
```properties
server.port=8090
```

Then access at: `http://localhost:8090`

### Problem 4: "Wrong Password Supplied for User 'postgres'"

**Error Message:**
```
PSQLException: FATAL: password authentication failed for user "postgres"
```

**Solution:**
1. Verify PostgreSQL password in `application.properties` matches your installation
2. Reset PostgreSQL password:

**Windows:**
1. Stop PostgreSQL service
2. Open Command Prompt as Administrator
3. Navigate to PostgreSQL bin directory:
   ```bash
   cd "C:\Program Files\PostgreSQL\15\bin"
   ```
4. Reset password:
   ```bash
   psql -U postgres -h localhost
   # Enter any password (or leave blank)
   ```

**macOS:**
```bash
# Reset password for postgres user
sudo -u postgres psql
# In psql prompt:
ALTER USER postgres WITH PASSWORD 'new_password';
\q
```

### Problem 5: "Tests Failed - Build Failure"

**Error Message:**
```
[ERROR] BUILD FAILURE
[ERROR] Tests run: X, Failures: Y, Errors: Z
```

**Solution:**
Skip tests during initial setup:
```bash
mvnw.cmd clean install -DskipTests
```

Tests can be run later once everything is configured:
```bash
mvnw.cmd test
```

### Problem 6: "Cannot Find Symbol" (Lombok Issue)

**Error Message:**
```
java: cannot find symbol
symbol: method getName()
```

**Solution:**
Enable annotation processing:

**IntelliJ IDEA:**
1. File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors
2. Check "Enable annotation processing"
3. Rebuild project

**VS Code:**
1. Install "Lombok Annotations Support" extension
2. Reload VS Code

**Eclipse:**
1. Lombok should auto-install
2. If not: Right-click project → Maven → Update Project

### Problem 7: "Cannot Connect to Database During Build"

**Error Message:**
```
org.springframework.boot.autoconfigure.jdbc.DataSourceProperties$DataSourceBeanCreationException
```

**Solution:**
1. Verify `application.properties` database settings are correct
2. Build without running the app:
   ```bash
   mvnw.cmd clean compile
   ```
3. Start PostgreSQL before running:
   ```bash
   # Ensure PostgreSQL is running first
   # Then run the app
   mvnw.cmd spring-boot:run
   ```

### Problem 8: "No Default Constructor Found"

**Error Message:**
```
No default constructor for entity
```

**Solution:**
Ensure all JPA entities have no-arg constructors (Lombok `@Data` provides this automatically).

---

## ✅ Verification Checklist

After completing all steps, verify:

- [ ] Java 17+ is installed: `java -version`
- [ ] PostgreSQL is running and accessible
- [ ] Database `api` exists with 7 tables
- [ ] `application.properties` has correct credentials
- [ ] Project built successfully: `mvnw clean install`
- [ ] Application starts without errors
- [ ] API responds at `http://localhost:8080/api/categories/tree`
- [ ] Can create categories via API
- [ ] Can create products via API
- [ ] Database shows data persistence

---

## 🎯 Next Steps

### After Successful Setup

1. **Explore the API:**
   - Use Postman/cURL to test all endpoints
   - Try creating categories and products
   - Test search functionality

2. **Read Documentation:**
   - Review `PROJECT.md` for detailed API documentation
   - Understand database schema in `Schema.sql`
   - Study entity relationships

3. **Develop Features:**
   - Add custom business logic
   - Extend controllers with new endpoints
   - Implement additional validation

4. **Deploy to Production:**
   - Set `ddl-auto=validate`
   - Use environment variables for credentials
   - Deploy JAR to production server
   - Set up CI/CD pipeline

5. **Collaborate:**
   - Push code to Git repository
   - Create feature branches
   - Submit pull requests

---

## 📚 Additional Resources

### Official Documentation
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/3.4.12/reference/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Maven Documentation](https://maven.apache.org/guides/)

### Tools & IDEs
- [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/)
- [VS Code](https://code.visualstudio.com/)
- [Postman](https://www.postman.com/downloads/)
- [pgAdmin](https://www.pgadmin.org/download/)
- [DBeaver](https://dbeaver.io/)

### Online Tutorials
- [Spring Boot Tutorial](https://spring.io/guides/gs/spring-boot/)
- [REST API Best Practices](https://restfulapi.net/)
- [PostgreSQL Tutorial](https://www.postgresql.org/docs/current/tutorial.html)

### Community Support
- [Spring Community Forums](https://spring.io/community)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/spring-boot)
- [PostgreSQL Mailing Lists](https://www.postgresql.org/list/)

---

## 🎓 Learning Path for Beginners

### Week 1: Setup & Basics
- [ ] Complete this Setup guide
- [ ] Understand project structure
- [ ] Learn about REST APIs
- [ ] Test basic CRUD operations

### Week 2: Database & Data
- [ ] Study SQL basics
- [ ] Understand entity relationships
- [ ] Learn JPA/Hibernate
- [ ] Explore Spring Data

### Week 3: API & Business Logic
- [ ] Deep dive into controllers
- [ ] Understand request/response cycle
- [ ] Study validation logic
- [ ] Learn error handling

### Week 4: Advanced
- [ ] Implement custom features
- [ ] Add authentication/authorization
- [ ] Write unit tests
- [ ] Deploy application

---

## 🔐 Security Notes

### Default Credentials ⚠️
The provided `application.properties` contains default database credentials:
```properties
spring.datasource.password=Anand@2004#
```

**IMPORTANT FOR PRODUCTION:**
1. Change PostgreSQL default password
2. Use environment variables for secrets:
   ```properties
   spring.datasource.password=${DB_PASSWORD}
   ```
3. Store credentials in secure vault
4. Never commit real credentials to Git
5. Use `.env` files (and add to `.gitignore`)

### Port Security
- Default port 8080 is accessible to anyone on your network
- In production, use firewall rules
- Consider reverse proxy (nginx, Apache)
- Enable HTTPS/SSL

### Database Security
- Restrict PostgreSQL access to localhost in production
- Use strong passwords
- Regular backups
- Monitor for suspicious activity

---

## 📞 Getting Help

If you encounter issues:

1. **Check Logs:**
   - Look at console output for error messages
   - Check application startup logs

2. **Search Online:**
   - Search error message on Google
   - Look for solutions on Stack Overflow
   - Check Spring Boot documentation

3. **Review This Guide:**
   - Check [Troubleshooting section](#troubleshooting)
   - Verify [Pre-Installation Checklist](#pre-installation-checklist)

4. **Enable Debug Mode:**
   ```properties
   # Add to application.properties
   logging.level.root=DEBUG
   logging.level.com.inventory=DEBUG
   ```

5. **Community Help:**
   - Spring Boot Community Forums
   - Stack Overflow [spring-boot] tag
   - GitHub Issues (if using GitHub)

---

## 📝 Summary

Congratulations! You should now have:

✅ Java 17+ installed
✅ PostgreSQL running with the `api` database
✅ Techsloyd project cloned and configured
✅ Application built and ready to run
✅ API endpoints accessible at `http://localhost:8080`
✅ Ability to create and manage inventory data

**Your inventory management system is now ready to use!**

---

**Last Updated:** January 16, 2026
**Version:** 1.0
**Status:** Complete & Ready for Deployment

---

## 📋 Quick Reference Commands

### Essential Commands

```bash
# Build project
mvnw.cmd clean install          # Windows
./mvnw clean install             # macOS/Linux

# Run application
mvnw.cmd spring-boot:run         # Windows
./mvnw spring-boot:run           # macOS/Linux

# Run from JAR
java -jar target/backend-api-0.0.1-SNAPSHOT.jar

# Connect to PostgreSQL
psql -U postgres -d api

# Test API
curl http://localhost:8080/api/categories/tree
```

### Common Maven Commands

```bash
# Compile only
mvnw.cmd compile

# Skip tests
mvnw.cmd clean install -DskipTests

# Run specific test
mvnw.cmd test -Dtest=TestClassName

# Show dependency tree
mvnw.cmd dependency:tree

# Update all dependencies
mvnw.cmd versions:update-properties
```

### PostgreSQL Commands

```bash
# Create database
createdb api

# Drop database
dropdb api

# Backup database
pg_dump api > backup.sql

# Restore database
psql api < backup.sql

# Connect with specific user
psql -U postgres -h localhost -d api
```

---

**Good luck with your inventory management system! Happy coding! 🚀**
