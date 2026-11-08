# Actividad 4 — App fullstack segura y probada

Aplicación fullstack con autenticación JWT, pruebas automatizadas (backend + frontend), base de datos PostgreSQL con migraciones Flyway, y despliegue completo con Docker Compose.

## Stack

- **Backend:** Spring Boot 3.3.5 (Java 21), Spring Security, JWT (jjwt), JPA, Flyway
- **Base de datos:** PostgreSQL 16 (contenedor)
- **Frontend:** React 18 + Vite + Vitest + Testing Library
- **Orquestación:** Docker Compose (un solo comando)

## Estructura del proyecto

```
actividad 4/
├── api/                # Backend Spring Boot
├── front/              # Frontend React + Vite
├── bd/                 # Migraciones Flyway (V1__init.sql)
├── docker-compose.yml  # Orquestación: postgres + api + front
└── README.md
```

## Cómo correrlo con Docker

Requisito: tener Docker Desktop corriendo.

```bash
docker compose up --build
```

Servicios disponibles:

- Frontend: http://localhost:5173
- Backend:  http://localhost:8080
- Postgres: localhost:5433 (host) → 5432 (contenedor)

Credenciales de prueba: `admin` / `admin123`.

Para detener y limpiar volúmenes:

```bash
docker compose down -v
```

## Cómo correrlo sin Docker (desarrollo)

**1) Base de datos:**

```bash
docker compose up -d postgres
```

**2) Backend** (desde `api/`):

```bash
./mvnw spring-boot:run
```

**3) Frontend** (desde `front/`):

```bash
npm install
npm run dev
```

## Pruebas

**Backend** (desde `api/`, requiere Postgres arriba):

```bash
./mvnw test
```

Cubre login exitoso y login con credenciales inválidas (caso de error).

**Frontend** (desde `front/`):

```bash
npm test
```

Cubre login exitoso (mockeando `fetch`) y login con error (401).

## Endpoints

- `POST /api/auth/login` — body `{"username":"admin","password":"admin123"}` → `{"token":"..."}`
- `GET /api/private/hello` — requiere header `Authorization: Bearer <token>`

## Security & run

This application implements a stateless JWT-based authentication flow. The frontend sends the user's credentials to `POST /api/auth/login`, and the backend validates them against a PostgreSQL database using Spring Security's `AuthenticationManager` with BCrypt password hashing. If the credentials are valid, the backend issues a signed JWT containing the username and role, and the frontend stores it in `localStorage`. Every request to a protected endpoint must include the token in the `Authorization: Bearer <token>` header, which is validated by a custom `JwtAuthFilter` before the request reaches the controller. CORS is explicitly configured on the backend to allow requests from `http://localhost:5173`, so the React frontend can call the API from the browser. Automated tests exist on both sides: the backend uses Spring Boot Test with MockMvc to verify successful and failed logins, and the frontend uses Vitest with Testing Library to verify the login component on success and on error. To run the full stack with Docker, execute `docker compose up --build` from the repository root; this starts PostgreSQL, the Spring Boot API on port 8080, and the Vite dev server on port 5173 in a single command.