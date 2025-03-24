```markdown
# Online Bookstore 🛒📚

A modern online bookstore application built with Spring Boot, providing secure user authentication, efficient book management, and seamless shopping cart functionality. Data is automatically initialized from a CSV file on startup.


## ✨ Features

- **🔐 Secure Authentication**
  - JWT-based registration/login system
  - Role-based access control
- **📖 Book Management**
  - Paginated book listings (20 items/page by default)
  - Advanced search with title, author, genre, and year filters
- **🛒 Shopping Cart**
  - Add multiple quantities of books
  - Clear cart functionality
  - Checkout Functionality  
- **📂 Data Initialization**
  - Automatic CSV data loading (`src/main/resources/books.csv`)
  - Sample dataset included

## 🛠️ Tech Stack

| Component           | Technology                          |
|---------------------|-------------------------------------|
| Backend Framework   | Spring Boot 3.2                     |
| Security            | Spring Security + JWT               |
| Database            | PostgreSQL                          |
| Data Parsing        | OpenCSV                             |
| API Documentation   | Swagger (Coming Soon)               |
| Build Tool          | Maven                               |

## 🚀 Getting Started

### Prerequisites

- JDK 21
- Maven 3.6+
- PostgreSQL (for production)

### Installation

1. **Clone Repository**
   ```bash
   git clone https://github.com/okeyximo/online-bookstore.git
   cd online-bookstore
   ```

2. **Build Application**
   ```bash
   mvn clean install
   ```

3. **Run Application**
   ```bash
   mvn spring-boot:run
   ```
   Application will be available at: `http://localhost:8080`

## ⚙️ Configuration

Configure your environment via `application.properties`:

```properties
# JWT Configuration
jwt.secret=your-256bit-secure-key-here
jwt.expirationTime=86400000 # 24h

# Database Configuration (Production)
spring.datasource.url=jdbc:postgresql://localhost:5432/bookstore
spring.datasource.username=bookstore_admin
spring.datasource.password=secure_password
```

## 📚 API Usage

### Authentication

**Register User**
```http
POST /users/register
Content-Type: application/json

{
  "username": "new_user",
  "password": "Str0ngP@ss!",
  "email": "user@bookstore.com"
}
```

**Login**
```http
POST /users/login
Content-Type: application/json

{
  "username": "new_user",
  "password": "Str0ngP@ss!"
}
```

### Book Operations

**Get Paginated Books**
```http
GET /books?page=0&size=20
```

**Advanced Search**
```http
GET /books/search?title=Great&author=Fitzgerald&genre=Fiction&year=1925
```

### Cart Management

**Add to Cart**
```http
POST /cart/add
Authorization: Bearer your.jwt.token
Content-Type: application/json

{
  "bookId": 42,
  "quantity": 3
}
```

**Clear Cart**
```http
DELETE /cart/clear
Authorization: Bearer your.jwt.token
```

## 📂 Data Initialization

The application automatically loads data from `src/main/resources/books.csv` on first startup:

```csv
title,author,isbn,genre,year,price,quantity
"The Great Gatsby","F. Scott Fitzgerald",9780743273565,Fiction,1925,12.99,50
"1984","George Orwell",9780451524935,Dystopian,1949,10.99,75
```

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

Please ensure tests are updated/added for new features.

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

