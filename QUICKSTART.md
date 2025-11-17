# Quick Start Guide

Get the Team Commercial application up and running in 5 minutes!

## 🚀 Quick Setup (Recommended)

### 1. Start PostgreSQL with Docker
```bash
docker-compose up -d
```

This will start a PostgreSQL database on port 5432 with:
- Database: `teamcommercial`
- Username: `postgres`
- Password: `postgres`

### 2. Start the Backend
Open a new terminal:
```bash
cd backend
mvn spring-boot:run
```

Wait for the message: "Started Application in X seconds"

### 3. Start the Frontend
Open another terminal:
```bash
cd frontend
npm install
npm start
```

### 4. Open Your Browser
Navigate to: **http://localhost:4200**

## 🎉 That's It!

You should now see the Product Management application. Try:
- Creating a new product
- Editing existing products
- Deleting products

## 🐛 Troubleshooting

### PostgreSQL Issues
If you don't have Docker, install PostgreSQL manually and create the database:
```sql
CREATE DATABASE teamcommercial;
```

### Backend Won't Start
- Check if Java 17+ is installed: `java -version`
- Check if port 8080 is available
- Verify PostgreSQL is running: `docker ps`

### Frontend Won't Start
- Check if Node.js is installed: `node --version`
- Try clearing and reinstalling: `rm -rf node_modules && npm install`
- Check if port 4200 is available

## 📝 Default API Endpoints

Backend runs on: **http://localhost:8080**
- GET http://localhost:8080/api/products - List all products
- POST http://localhost:8080/api/products - Create product

Frontend runs on: **http://localhost:4200**

## 🛑 Stopping the Application

1. Stop frontend: `Ctrl + C` in frontend terminal
2. Stop backend: `Ctrl + C` in backend terminal
3. Stop database: `docker-compose down`

## 📚 Need More Details?

Check out the comprehensive README.md in the root directory!

