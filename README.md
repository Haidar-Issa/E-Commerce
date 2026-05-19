# E-Commerce
Production‑ready E‑Commerce Backend built with Spring Boot, Redis, Stripe, Twilio, and Webhook Analytics. Includes OTP authentication, caching, rate limiting, and an admin dashboard.
----------------------------------------------------------------------------
E‑Commerce Backend — Spring Boot | Redis | Stripe | OTP | Webhook Analytics
A production‑ready backend for an e‑commerce platform built with Spring Boot, featuring Stripe payments, Webhook processing, OTP authentication, Redis caching, and a full Admin Analytics Dashboard.

This project is designed with clean architecture, scalability, and enterprise‑grade patterns.
----------------------------------------------------------------------------
🚀 Features
1-JWT Authentication + Refresh Tokens
2-OTP System (Email)
3-Redis for OTP, caching, and rate limiting
4-Stripe Payment Integration
5-Webhook Processing + Retry Logic
6-WebhookLog Analytics + Charts Endpoints
7-Admin Dashboard Backend
8-Clean Architecture (Controller → Service → Repository → DTO → Mapper)
9-Global Exception Handling + Validation
10-MySQL / PostgreSQL Support
----------------------------------------------------------------------------
🧰 Tech Stack
1-Java 21
2-Spring Boot 3
3-Spring Security
4-Redis
5-Stripe API
6-MySQL / PostgreSQL
----------------------------------------------------------------------------
🏗 Architecture Overview
src/main/java/com/example/project/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/
├── config/
├── security/
└── exception/

-This structure ensures:
1-High scalability
2-Clear separation of concerns
3-Easy testing
4-Maintainability
----------------------------------------------------------------------------
🔌 Modules
Authentication
1-Login
2-Register
3-Refresh Token
4-OTP (Email + SMS)
5-Rate Limiting
6-Failed Attempt Blocking

Payments
1-Stripe PaymentIntent
2-Stripe Webhooks
3-Signature Verification
4-Retry Logic

Webhook Analytics
1-Count
2-Daily
3-Hourly
4-Top Errors
5-Top Retries
6-Chart‑Ready Endpoints

Admin Dashboard
1-WebhookLog Table
2-Filtering
3-Pagination
4-Export
5-Manual Retry
----------------------------------------------------------------------------
⚙️ How to Run the Project

1-Clone the repository
    git clone https://github.com/HiadarIssa/E-Commerce.git
    cd E-Commerce

2-Configure environment variables
  create: 
  application-dev.properties
  
  add:
  STRIPE_SECRET_KEY=
  TWILIO_ACCOUNT_SID=
  TWILIO_AUTH_TOKEN=
  TWILIO_PHONE_NUMBER=
  SPRING_DATASOURCE_URL=
  SPRING_DATASOURCE_USERNAME=
  SPRING_DATASOURCE_PASSWORD=
  REDIS_HOST=localhost
  REDIS_PORT=6379
  JWT_SECRET=
  
3-Start Redis:
docker run -p 6379:6379 redis

4-Run the backend:
mvn spring-boot:run
----------------------------------------------------------------------------
🧪 Testing
1-Unit Tests (Mockito)
2-Integration Tests (MockMvc)
3-Postman Collection included
----------------------------------------------------------------------------
🔐 Security
1-JWT Access + Refresh
2-Redis Rate Limiting
3-OTP brute‑force protection
4-Webhook signature verification
5-Admin‑only endpoints
----------------------------------------------------------------------------
