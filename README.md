# 🏦 BankingApp

**BankingApp** is a full-featured **banking system backend** built with **Spring Boot**, offering core banking operations such as account management, loans, cards, transactions, with external integrations for AI, email, exchange rates, and cloud storage.  

---

## 🚀 System Architecture

BankingApp is built in a **layered, RESTful service architecture**:

- **Backend** → Spring Boot (RESTful APIs, business logic)  
- **Database** → MySQL using Spring Data JPA / Hibernate  
- **Security** → Spring Security + JWT for authentication & role-based access control  
- **Scheduler** → background tasks for periodic jobs (loan payments, exchange rate updates, etc.)  
- **External Integrations** → AI Chatbot, Currency Exchange API, Email SMTP, Cloud Storage  

---

## 🛠️ Core Technologies

| Component             | Technology / Version            | Purpose |
|-----------------------|----------------------------------|---------|
| Framework             | Spring Boot 3.3.5               | RESTful backend services |
| Database & ORM        | MySQL + Spring Data JPA / Hibernate | Persistence, relational data modelling |
| Security              | Spring Security + JWT           | Authentication, authorization, RBAC |
| Scheduling / Tasks    | Spring `@Scheduled`, cron jobs  | Periodic operations (exchange rate updates, loan payments) |
| API Documentation     | OpenAPI / Swagger               | REST endpoint docs |
| External Services     | AI Chatbot, Exchange Rate API, Cloudinary, Gmail SMTP | Chat support, real-time rates, image storage, email |
| Build & Language      | Java 17, Maven                  | Build & dependency management |

---

## 🧩 Key Features

### 🏦 Core Banking Operations
- **Account Management** → Create, update, and close customer accounts with support for multiple currencies.  
- **Fund Transfers & Currency Conversion** → Transfer between accounts, with real-time exchange rates.  
- **Loan Management** → Apply for loans, track status, automated interest & payment scheduling.  
- **Card Management** → Issue & manage credit/debit cards.  
- **Transaction Tracking** → Secure logging, auditing, and retrieval of transaction history.  

### 🔐 User Management & Security
- Authentication via **JWT tokens**.  

### 🌐 External Integrations
- **AI Chatbot** → Customer support.  
- **Exchange Rate API** → Real-time currency rates.  
- **Email Notifications** → Gmail SMTP.  
- **Cloudinary** → Image & document storage.  

---

## 📦 Installation & Setup

### 1. Clone the repository
```bash
git clone https://github.com/Minh20Duc04/BankingApp.git
cd BankingApp
```

### 2. Configure & Build
Then create database: 
```bash
- Create a MySQL database (e.g. `banking_app`).
- Update `application.yml` with credentials & secrets:
  - Database connection
  - JWT secret key
  - API keys (Exchange rate, AI chatbot)
  - Cloudinary config
  - Gmail SMTP credentials
```
# Build with Maven
```bash
mvn clean install
```
## 📚 API Documentation
Once the backend is running, the API documentation is available via Swagger UI:
```bash
http://localhost:2004/swagger-ui.html
```
This provides detailed information about all REST endpoints, request/response schemas, and authentication requirements.

## 🤝 Contributing
We welcome contributions to improve ElderSync!

1. Fork the repository
2. Create a feature branch:
   git checkout -b feature/your-feature
3. Commit your changes:
   git commit -m "Add new feature: your-feature"
4. Push the branch:
   git push origin feature/your-feature
5. Open a Pull Request

---

## 📄 License
This project is licensed under the MIT License.
See the LICENSE file for details.

