
# File Tree Manager — Spring Boot + React

This is a complete implementation of the take‑home test using **Java 17 + Spring Boot 3** (MySQL 8) and **React + Vite + TypeScript** (MUI).
It includes: hierarchical tree (folders/files), add/delete/move (reorder), tagging (key–value), Swagger docs, and seed data.

## Prerequisites
- Java 21
- Maven 3.9+
- Node.js 18+
- MySQL 8 running locally

## MySQL setup
```sql
CREATE DATABASE fsdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'app'@'localhost' IDENTIFIED BY 'app';
GRANT ALL PRIVILEGES ON fsdb.* TO 'app'@'localhost';
FLUSH PRIVILEGES;
```

## Run backend
```bash
cd backend
mvn spring-boot:run
# Swagger: http://localhost:8080/swagger-ui/index.html
```

## Run frontend
```bash
cd frontend
npm install
npm run dev
# App: http://localhost:5173
```

## API quick look
- `GET /api/nodes/tree`
- `POST /api/nodes` (name, type = FOLDER|FILE, parentId?, position?, tags?)
- `PUT /api/nodes/{id}`
- `DELETE /api/nodes/{id}`
- `POST /api/nodes/{id}/move` (newParentId, newPosition)

## VS Code
Use the included `.vscode` to run both front & back together:
- **Run and Debug → "Run Frontend + Backend"**

