# DailyNews Backend

Backend API for the DailyNews application, developed with Spring Boot and Spring Security.

## 🚀 Features

- **JWT Authentication**: Secure authentication system with JWT tokens
- **User Management**: User registration, login, and authentication status verification
- **MySQL Database**: Data persistence with JPA/Hibernate
- **Security**: Spring Security configuration with BCrypt for password encryption
- **CORS**: Configured for communication with Angular frontend

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Security**
- **Spring Data JPA**
- **MySQL 8.0**
- **JWT (JSON Web Tokens)**
- **Lombok**
- **Gradle**

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Gradle 7.0 or higher

## ⚙️ Configuration

### 1. Clone the repository
\`\`\`bash
git clone <repository-url>
cd dailyNews-BackEnd
\`\`\`

### 2. Configure the database
Create a MySQL database named \`dailyNews\`:
\`\`\`sql
CREATE DATABASE dailyNews;
\`\`\`

### 3. Configure environment variables
Edit the \`src/main/resources/application.properties\` file:

\`\`\`properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/dailyNews
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT
jwt.secret=your_secret_key
jwt.expiration=3600000
\`\`\`

### 4. Run the application
\`\`\`bash
./gradlew bootRun
\`\`\`

The application will be available at \`http://localhost:8080\`

## 📚 API Endpoints

### Authentication

#### User Registration
\`\`\`http
POST /auth/register
Content-Type: application/json

{
\"name\": \"John Doe\",
\"email\": \"john@example.com\",
\"password\": \"password123\"
}
\`\`\`

**Success Response (201):**
\`\`\`json
{
\"user\": {
\"id\": 1,
\"name\": \"John Doe\",
\"email\": \"john@example.com\"
},
\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"
}
\`\`\`

#### User Login
\`\`\`http
POST /auth/login
Content-Type: application/json

{
\"email\": \"john@example.com\",
\"password\": \"password123\"
}
\`\`\`

**Success Response (200):**
\`\`\`json
{
\"user\": {
\"id\": 1,
\"name\": \"John Doe\",
\"email\": \"john@example.com\"
},
\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"
}
\`\`\`

#### Check Authentication Status
\`\`\`http
GET /auth/check-status
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
\`\`\`

**Success Response (200):**
\`\`\`json
{
\"user\": {
\"id\": 1,
\"name\": \"John Doe\",
\"email\": \"john@example.com\"
},
\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"
}
\`\`\`

### Users

#### Get All Users
\`\`\`http
GET /
\`\`\`

**Success Response (200):**
\`\`\`json
[
{
\"id\": 1,
\"name\": \"John Doe\",
\"email\": \"john@example.com\",
\"createdAt\": \"2024-01-15T10:30:00.000Z\"
}
]
\`\`\`

## 🔒 Security

- **Passwords**: Encrypted with BCrypt
- **JWT**: Tokens with configurable expiration (default 1 hour)
- **CORS**: Configured for \`http://localhost:4200\` (Angular)
- **Public endpoints**: \`/auth/**\` and \`/\`

## 📁 Project Structure

\`\`\`
src/main/java/dailyNews/dailyNews/
├── DailyNewsApplication.java          # Spring Boot main class
├── SecurityConfig.java                # Security configuration
├── dto/                              # Data Transfer Objects
│   ├── AuthResponse.java
│   ├── UserLogInDTO.java
│   ├── UserRegisterDTO.java
│   └── UserResponseDTO.java
├── entity/                           # JPA entities
│   └── User.java
├── repositories/                     # Data repositories
│   └── UserRepository.java
├── service/                          # Business logic
│   ├── JwtService.java
│   └── userService.java
└── userController/                   # REST controllers
└── UserController.java
\`\`\`

## 🧪 Testing

Run tests:
\`\`\`bash
./gradlew test
\`\`\`

## 🚀 Deployment

### Development
\`\`\`bash
./gradlew bootRun
\`\`\`

### Production
\`\`\`bash
./gradlew build
java -jar build/libs/dailyNews-0.0.1-SNAPSHOT.jar
\`\`\`

## 📝 HTTP Status Codes

- **200**: OK - Successful operation
- **201**: Created - User created successfully
- **401**: Unauthorized - Invalid credentials or expired token
- **404**: Not Found - User not found
- **409**: Conflict - Email already registered

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (\`git checkout -b feature/AmazingFeature\`)
3. Commit your changes (\`git commit -m 'Add some AmazingFeature'\`)
4. Push to the branch (\`git push origin feature/AmazingFeature\`)
5. Open a Pull Request


## 👨‍💻 Developed by

- **James Orozco** - _Full Stack Developer_ - [Hetvall](https://github.com/Hetvall)
