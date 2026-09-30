# Inventario API

REST API built with Spring Boot for managing an inventory of products. It implements a layered architecture (entity, repository, service, controller), full CRUD operations, JPA persistence with PostgreSQL, and interactive documentation with Swagger.

---

## Tech Stack

- **Java 25**
- **Spring Boot 4.1.x**
- **Spring Data JPA** (Hibernate)
- **PostgreSQL 16** (via Docker)
- **springdoc-openapi** (Swagger UI)
- **Maven**

---

## Project Structure
```
src/main/java/com/agudelo/inventario_api
├── config/ # OpenAPI configuration
├── controller/ # REST controllers
├── entity/ # JPA entities
├── exception/ # Custom exceptions and global handler
├── repository/ # Spring Data repositories
├── service/ # Service interfaces
│ └── impl/ # Service implementations
└── InventarioApiApplication.java
```
---

## Prerequisites

- Java 25 (or higher)
- Docker and Docker Compose
- Maven (or use the included `mvnw` wrapper)

---

## How to Run

### 1. Start the PostgreSQL database

```bash
docker compose up -d
```
This launches a PostgreSQL 16 container exposed on port 5433 (to avoid conflicts with any local PostgreSQL installation).

### 2. Run the application
```bash
./mvnw spring-boot:run
```
The API will be available at: http://localhost:8080

### 3. Stop the database (when done)
```bash
docker compose down
```
To also delete the database volume:

```bash
docker compose down -v
```
----

## Swagger UI

Interactive API documentation is available at:

- Swagger UI: http://localhost:8080/swagger-ui.html

- OpenAPI JSON: http://localhost:8080/v3/api-docs

----

## Postman Collection

The postman/ folder contains a ready-to-import Postman collection with all the CRUD requests, including examples for error cases (404 and 400).

To use it:

1. Open Postman.

2. Click Import and select postman/inventario-api.postman_collection.json.

3. Run the requests against http://localhost:8080.

------

## API Reference
This section describes all the endpoints exposed by the API and what each one does.

The GET /api/productos endpoint returns the complete list of products stored in the database, or an empty array if there are no products yet. The GET /api/productos/{id} endpoint retrieves a single product by its unique identifier and returns a 404 status if the product does not exist. The POST /api/productos endpoint creates a new product from the JSON body sent in the request, validating all required fields and returning a 400 status if the data is invalid. The PUT /api/productos/{id} endpoint updates an existing product identified by its ID with the new information provided in the request body. Finally, the DELETE /api/productos/{id} endpoint removes a product from the database and returns a 204 status when the deletion is successful.

|Method |Endpoint |Description |Success |Errors|
|-------|---------|------------|--------|------|
|GET	|/api/productos	|List all products	|200	|—|
|GET	|/api/productos/{id}	|Get a single product by ID	|200	|404|
|POST	|/api/productos	|Create a new product	|201	|400|
|PUT	|/api/productos/{id}	|Update an existing product	|200	|400, 404|
|DELETE	|/api/productos/{id}	|Delete a product	|204	|404|
-----
### Example Request Body (POST / PUT)
```json
{
  "nombre": "Laptop HP",
  "descripcion": "Laptop with 16GB RAM and 512GB SSD",
  "precio": 1200.50,
  "stock": 10
}
```

### Example Error Response (404)
```json
{
  "timestamp": "2026-09-30T10:15:00",
  "status": 404,
  "error": "Not Found",
  "message": "Producto no encontrado con id: 9999"
}
```

### Example Error Response (400)
```json
{
  "timestamp": "2026-09-30T10:15:00",
  "status": 400,
  "error": "Bad Request",
  "message": {
    "nombre": "El nombre es obligatorio",
    "precio": "El precio debe ser mayor a 0",
    "stock": "El stock no puede ser negativo"
  }
}
```
-----------

# Author
**Juan Sebastían Agudelo**

**Full Stack Development Course — Corte 2**