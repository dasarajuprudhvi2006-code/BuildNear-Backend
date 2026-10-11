# BuildNear - Backend API

Production-ready Spring Boot 3.3.4 REST API for **BuildNear** (Local Construction & Interior Services Marketplace).

## Tech Stack
- **Language & Runtime:** Java 21 (Eclipse Temurin)
- **Framework:** Spring Boot 3.3.4
- **Security:** Spring Security + Stateless JWT
- **Persistence:** Spring Data JPA + Hibernate + MySQL
- **Build Tool:** Apache Maven

---

## Architecture & Endpoints

### Public Endpoints
- `GET /api/health` — Cloud health check endpoint (returns service status & timestamp)
- `POST /api/auth/register` — Customer & Professional registration
- `POST /api/auth/login` — JWT Authentication
- `GET /api/professionals` — Search professionals (keyword, radius, category, city filters)
- `GET /api/professionals/{id}` — Professional detailed profile & portfolio
- `GET /api/packages` — Standardized construction & interior service packages
- `GET /api/reviews` — Customer reviews and ratings
- `POST /api/estimations/calculate` — Construction/Interior cost estimator

### Authenticated Endpoints
- `GET /api/users/me` — Current profile
- `POST /api/requests` — Customer service request submissions
- `GET /api/quotations` — Professional quotation management
- `POST /api/bookings` — Booking management and direct appointments
- `GET /api/admin/*` — Admin KPI overview & verification (Role: `ROLE_ADMIN`)

---

## Cloud Deployment (Render)

1. Create a **Web Service** on [Render.com](https://render.com).
2. Connect your GitHub repository: `dasarajuprudhvi2006-code/BuildNear-Backend`.
3. Choose **Docker** environment (or **Native Java** with Maven build).
4. Configure Environment Variables:
   - `PORT`: `8080`
   - `SPRING_DATASOURCE_URL`: `jdbc:mysql://<db-host>:<port>/<db-name>?useSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC`
   - `SPRING_DATASOURCE_USERNAME`: `<db-user>`
   - `SPRING_DATASOURCE_PASSWORD`: `<db-password>`
   - `APP_JWT_SECRET`: `<64-character-hex-secret>`
   - `APP_JWT_EXPIRATION_MS`: `86400000`
5. Verification:
   - Request `GET /api/health` to confirm the service is healthy.

---

## Local Development

```bash
# Run with Maven
mvn spring-boot:run
```
API runs on `http://localhost:8080`.
