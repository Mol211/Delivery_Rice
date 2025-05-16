# Delivery_Rice
## Backend for the Final Project of Víctor Molins Martínez (FPGS DAM)

## 📚 Table of Contents


- [Description](#Description)
- [Features](#Features)
- [Technologies & Tools](#Technologies_&_Tools)
- [Installation](#Installation)
- [Testing](#testing)

## 📝 Description
Delivery_Rice is an e-commerce platform that allows users to order high-quality rice from anywhere. It's a fast alternative to traditional fast food — quick, but much better.
This backend is built with the Spring Boot framework and provides a RESTful API that allows users to interact with the platform, manage their accounts, and place orders.

## ✨ Features
- User registration and login
- Role-based access control (Client, Delivery, Admin)
- CRUD operations for user addresses
- CRUD operations for products (Admin role only)
- Multiple payment options: Bizum, Cash, Card
- Order management:
  - Place orders
  - Select and modify shipping addresses
  - Choose payment methods
  - Track order status
  - View order history

## 🛠️ Technologies & Tools
- Java 21 (JDK)
- Spring Boot
- Spring Security
- Hibernate JPA
- RESTful API
- Layered Architecture (Repository, Service, Controller)
- Design patterns used:
  - Repository Pattern
  - DTO Pattern
- Unit testing with JUnit and Mockito

## Instalation
1. Open your mySQL Server (default port: 3306)
2. The application is configured to use the default phpMyAdmin credentials:  
  **Username:** `root`
  **Password:** *(blank)*
   If your MySQL setup uses different credentials, update the `spring.datasource.username` and `spring.datasource.password` fields in `src/main/resources/application.properties`.
3. Run the application with `mvn spring-boot:run`, the first run will generate the database automatically. The application runs on the default phpMyAdmin user "root".
4. Open phpMyAdmin and Click on `Import` and select the `bd_service_deliveryRice.sql` file.


## Testing
- Run all unit tests with: `mvn clean test` 