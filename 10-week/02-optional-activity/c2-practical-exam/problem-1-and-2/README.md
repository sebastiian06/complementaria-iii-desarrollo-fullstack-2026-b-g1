# Problem 1 & 2 — Entity, Repository, Service and REST Controller

Proyecto Spring Boot que resuelve los problemas 1 y 2 de la actividad:
un CRUD REST completo de **Estudiantes** con base de datos H2 en memoria.

## 🚀 Requisitos

- Java 17 o superior
- Maven (o el wrapper `mvnw`)
- Postman (para probar los endpoints)

## ▶️ Cómo ejecutar

```bash
.\mvnw.cmd spring-boot:run
```
La app queda disponible en http://localhost:8080.
Consola H2: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:testdb, user: sa, password vacío).

## 🧩 Arquitectura por capas
```text
Cliente (Postman)
       ↓ HTTP
  EstudianteController   → endpoints REST
       ↓
  EstudianteService      → lógica + validaciones
       ↓
  EstudianteRepository   → JpaRepository (JPA/Hibernate)
       ↓
     Base de datos H2
```

## 1️⃣ Problem 1 — Entity y Repository
### Entity: Estudiante
```java
@Entity
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private Integer edad;
    // getters y setters...
}
```
### Repository: EstudianteRepository
```java
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    List<Estudiante> findByNombre(String nombre);
    Estudiante findByEmail(String email);
}
```

## 🧠 Mapeo objeto-relacional (ORM)
El ORM es la técnica que traduce entre objetos Java y tablas relacionales.
En nuestro caso, Hibernate (implementación de JPA en Spring Boot) hace la
traducción automáticamente: nosotros escribimos clases Java y él genera el SQL.

| Java (objeto)	| Base de datos (relacional)|
|---------------|--------------------------|
| Clase Estudiante	| Tabla estudiantes |
| Objeto estudiante	| Fila / registro |
| Atributo nombre	| Columna nombre |
| Tipo String	| Tipo VARCHAR |
| Tipo Long	| Tipo BIGINT |
| Tipo Integer	| Tipo INTEGER |

Ventajas: menos SQL manual, código portable entre motores de BD, menos
boilerplate, y mayor enfoque en la lógica de negocio.

Ejemplo: repository.save(estudiante) genera internamente:

```sql
INSERT INTO estudiantes (nombre, email, edad) VALUES (?, ?, ?);
```

## 2️⃣ Problem 2 — Endpoints REST
| Método	| URL	| Acción	| Status éxito	| Status error |
|-----------|-------|-----------|---------------|--------------|
| GET	| /estudiantes	| Listar todos	| 200 OK	| — | 
| GET	| /estudiantes/{id}	| Obtener uno	| 200 OK	| 404 Not Found |
| GET	| /estudiantes/buscar?nombre=X	| Buscar por nombre	| 200 OK	| — |
| POST	| /estudiantes	| Crear | 201 Created	| 400 Bad Request |
| PUT	| /estudiantes/{id}	Actualizar	| 200 OK	| 404 / 400 |
| DELETE	| /estudiantes/{id}	| Eliminar	| 204 No Content	| 404 Not Found |

## 📌 Ejemplo de JSON para POST
```json
{
  "nombre": "Ana Torres",
  "email": "ana.torres@example.com",
  "edad": 21
}
```
## 3️⃣ English requirement — Layer responsibilities
The controller is the entry point of the API. It maps HTTP requests to Java
methods, receives JSON payloads, and returns the appropriate HTTP status codes
and responses. It does not contain business logic; it just delegates to the
service layer.

The service contains the business logic of the application. It applies
validation rules, coordinates operations, and decides what should happen when
data is invalid or missing (for example, throwing exceptions when a student is
not found).

The repository is responsible for data access. By extending JpaRepository,
it provides ready-to-use methods to save, find, update and delete entities, and
it abstracts database communication through JPA/Hibernate.