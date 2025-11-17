# Team Commercial Backend - Spring Boot with Hibernate

RESTful API backend built with Spring Boot, Spring Data JPA (Hibernate), and PostgreSQL.

## Features

- RESTful API endpoints for product management
- Hibernate ORM for database operations
- PostgreSQL database integration
- CORS enabled for Angular frontend
- Input validation
- Automatic timestamp management
- Transaction management

## Technology Stack

- Spring Boot 3.2.0
- Spring Data JPA (Hibernate)
- PostgreSQL Driver
- Spring Validation
- Lombok
- Spring DevTools
- Maven

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/teamcommercial/
│   │   │   ├── Application.java
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java
│   │   │   ├── entity/
│   │   │   │   └── Product.java
│   │   │   ├── repository/
│   │   │   │   └── ProductRepository.java
│   │   │   └── service/
│   │   │       └── ProductService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

## Database Configuration

1. Create a PostgreSQL database:
```sql
CREATE DATABASE teamcommercial;
```

2. Configure database connection in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/teamcommercial
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

1. Build the project:
```bash
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

## API Endpoints

### Product Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create new product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |
| GET | `/api/products/search?name={name}` | Search products by name |
| GET | `/api/products/in-stock` | Get products in stock (quantity > 0) |

## Entity Model

### Product
```java
{
  "id": Long,
  "name": String (required),
  "description": String,
  "price": BigDecimal (required),
  "quantity": Integer (required),
  "createdAt": LocalDateTime,
  "updatedAt": LocalDateTime
}
```

## Hibernate Configuration

The application uses Hibernate with the following settings:
- **DDL Auto**: `update` - Automatically updates database schema
- **Show SQL**: `true` - Logs SQL statements to console
- **Format SQL**: `true` - Formats SQL statements for readability
- **Dialect**: PostgreSQL

## Testing the API

### Create a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "quantity": 10
  }'
```

### Get All Products
```bash
curl http://localhost:8080/api/products
```

### Update a Product
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Laptop",
    "description": "High-performance gaming laptop",
    "price": 1299.99,
    "quantity": 5
  }'
```

### Delete a Product
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

## Development

### Adding New Entities

1. Create entity class in `entity/` package with `@Entity` annotation
2. Create repository interface extending `JpaRepository`
3. Create service class with business logic
4. Create REST controller with API endpoints

### Database Migrations

For production, consider using Flyway or Liquibase for database migrations instead of `ddl-auto=update`.

## Building for Production

```bash
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

For production, update `application.properties`:
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

## Troubleshooting

- **Port already in use**: Change port in `application.properties`
- **Database connection failed**: Verify PostgreSQL is running and credentials are correct
- **Hibernate schema issues**: Check `ddl-auto` setting and database permissions

