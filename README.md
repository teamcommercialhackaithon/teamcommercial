# Team Commercial - Full Stack Application

A full-stack application with Spring Boot backend and Angular frontend for managing products.

## Project Structure

```
teamcommercial/
├── backend/          # Spring Boot application
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   └── pom.xml
└── frontend/         # Angular application
    ├── src/
    │   └── app/
    ├── angular.json
    └── package.json
```

## Technologies Used

### Backend
- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - Data persistence with Hibernate
- **PostgreSQL** - Database
- **Maven** - Build tool
- **Lombok** - Reduces boilerplate code
- **Java 17** - Programming language

### Frontend
- **Angular 17** - Frontend framework
- **TypeScript** - Programming language
- **RxJS** - Reactive programming
- **HttpClient** - API communication

## Prerequisites

Before you begin, ensure you have the following installed:
- **Java 17** or higher
- **Maven 3.6+**
- **Node.js 18+** and npm
- **PostgreSQL 12+**
- **Angular CLI** (`npm install -g @angular/cli`)

## Database Setup

1. Install PostgreSQL if you haven't already
2. Create a new database:
```sql
CREATE DATABASE teamcommercial;
```

3. Update the database credentials in `backend/src/main/resources/application.properties` if needed:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/teamcommercial
spring.datasource.username=postgres
spring.datasource.password=postgres
```

The application uses Hibernate's `ddl-auto=update` setting, so tables will be created automatically when you run the backend.

## Backend Setup & Running

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The backend will start on **http://localhost:8080**

### Backend API Endpoints

#### Products API
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create a new product
- `PUT /api/products/{id}` - Update a product
- `DELETE /api/products/{id}` - Delete a product
- `GET /api/products/search?name={name}` - Search products by name
- `GET /api/products/in-stock` - Get products with quantity > 0

#### Customers API
- `GET /api/customers` - Get all customers
- `GET /api/customers/{customerId}` - Get customer by customer ID
- `GET /api/customers/email/{emailId}` - Get by email
- `POST /api/customers` - Create customer
- `PUT /api/customers/{customerId}` - Update customer
- `DELETE /api/customers/{customerId}` - Delete customer
- `GET /api/customers/search?name={name}` - Search by name
- `GET /api/customers/city/{city}` - Get by city
- `GET /api/customers/state/{state}` - Get by state

#### Notifications API
- `GET /api/notifications` - Get all notifications
- `GET /api/notifications/{id}` - Get by ID
- `GET /api/notifications/customer/{customerId}` - Get by customer ID
- `GET /api/notifications/notified/{status}` - Get by status (true/false)
- `GET /api/notifications/type/{type}` - Get by type
- `GET /api/notifications/expired` - Get expired notifications
- `POST /api/notifications` - Create notification
- `PUT /api/notifications/{id}` - Update notification
- `PATCH /api/notifications/{id}/mark-notified` - Mark as notified
- `DELETE /api/notifications/{id}` - Delete notification

#### Events API
- `GET /api/events` - Get all events (ordered by date)
- `GET /api/events/{id}` - Get event by ID
- `GET /api/events/type/{type}` - Get by type
- `GET /api/events/mac/{macAddress}` - Get by MAC address
- `GET /api/events/serial/{serial}` - Get by serial
- `GET /api/events/recent` - Get recent events
- `POST /api/events` - Create event
- `PUT /api/events/{id}` - Update event
- `DELETE /api/events/{id}` - Delete event

### Sample JSON Examples

**Product:**
```json
{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 10
}
```

**Customer:**
```json
{
  "customerName": "John Doe",
  "customerId": 1001,
  "streetNumber": "123",
  "streetName": "Main Street",
  "city": "Sydney",
  "state": "NSW",
  "postCode": "2000",
  "emailId": "john.doe@example.com",
  "mobileNumber": "+61-400-123-456",
  "communicationPreference": "Email"
}
```

**Notification:**
```json
{
  "customerId": "CUST001",
  "macAddress": "00:1A:2B:3C:4D:5E",
  "ipAddress": "192.168.1.100",
  "serial": "SER12345",
  "startDate": "2025-11-17",
  "endDate": "2025-12-17",
  "type": "Email",
  "notified": false
}
```

**Event:**
```json
{
  "type": "INFO",
  "macAddress": "00:1A:2B:3C:4D:5E",
  "serial": "SER12345",
  "message": "Device connected successfully",
  "date": "2025-11-17T10:30:00",
  "payloadBase64": "SGVsbG8gV29ybGQ="
}
```

## Frontend Setup & Running

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Run the development server:
```bash
npm start
```
or
```bash
ng serve
```

The frontend will start on **http://localhost:4200**

## Running the Full Application

1. Start PostgreSQL database
2. In one terminal, start the backend:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
3. In another terminal, start the frontend:
   ```bash
   cd frontend
   npm start
   ```
4. Open your browser and navigate to **http://localhost:4200**

## Features

### Product Management
- ✅ Create, read, update, delete products
- ✅ Product catalog with pricing and inventory
- ✅ Search products by name
- ✅ Filter products in stock

### Customer Management
- ✅ Complete customer information management
- ✅ Address tracking (street, unit, city, state, postal code)
- ✅ Contact information (email, mobile)
- ✅ Communication preferences
- ✅ Search and filter by location

### Notification System
- ✅ Customer notifications with device tracking
- ✅ MAC address and IP address logging
- ✅ Date range management (start/end dates)
- ✅ Notification status tracking (notified/unnotified)
- ✅ Filter by notification status
- ✅ Quick toggle notification status

### Event Logging
- ✅ Event tracking with timestamps
- ✅ Device information (MAC address, serial)
- ✅ Event types and messages
- ✅ BLOB payload support for binary data
- ✅ Base64 encoding for JSON transmission
- ✅ Filter events by type
- ✅ Payload viewer with modal

### UI/UX Features
- ✅ Responsive design for all screen sizes
- ✅ Modern UI with gradient backgrounds
- ✅ Real-time feedback with success/error messages
- ✅ Form validation
- ✅ Beautiful card-based layouts
- ✅ Navigation menu for easy access

## Development

### Backend Development

**Entities:**
- `com.teamcommercial.entity.Product` - Product catalog
- `com.teamcommercial.entity.Customer` - Customer information
- `com.teamcommercial.entity.CustomerNotification` - Notification system
- `com.teamcommercial.entity.Event` - Event logging with BLOB support

**Repositories:**
- `com.teamcommercial.repository.ProductRepository`
- `com.teamcommercial.repository.CustomerRepository`
- `com.teamcommercial.repository.CustomerNotificationRepository`
- `com.teamcommercial.repository.EventRepository`

**Services:**
- `com.teamcommercial.service.ProductService`
- `com.teamcommercial.service.CustomerService`
- `com.teamcommercial.service.CustomerNotificationService`
- `com.teamcommercial.service.EventService`

**Controllers:**
- `com.teamcommercial.controller.ProductController`
- `com.teamcommercial.controller.CustomerController`
- `com.teamcommercial.controller.CustomerNotificationController`
- `com.teamcommercial.controller.EventController`

**DTOs:**
- `com.teamcommercial.dto.EventDTO` - For Base64 payload encoding

### Frontend Development

**Components:**
- `src/app/components/product-list/` - Product management
- `src/app/components/customer-list/` - Customer management
- `src/app/components/notification-list/` - Notification management
- `src/app/components/event-list/` - Event logging

**Services:**
- `src/app/services/product.service.ts`
- `src/app/services/customer.service.ts`
- `src/app/services/customer-notification.service.ts`
- `src/app/services/event.service.ts`

**Models:**
- `src/app/models/product.model.ts`
- `src/app/models/customer.model.ts`
- `src/app/models/customer-notification.model.ts`
- `src/app/models/event.model.ts`

## Troubleshooting

### Backend Issues
- **Database connection error**: Verify PostgreSQL is running and credentials are correct
- **Port 8080 already in use**: Change the port in `application.properties`:
  ```properties
  server.port=8081
  ```

### Frontend Issues
- **Cannot connect to backend**: Ensure backend is running on port 8080
- **CORS errors**: The backend has CORS enabled for `http://localhost:4200`
- **Port 4200 already in use**: Angular CLI will prompt to use a different port

## Building for Production

### Backend
```bash
cd backend
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
cd frontend
ng build --configuration production
```
The production files will be in `frontend/dist/`

## License

This project is created for Team Commercial hackathon purposes.

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

