# Problem 3 — Documentar y probar

Este documento describe cómo documentaría la API con **Swagger/OpenAPI**, cómo
probaría tres endpoints con **Postman** (incluyendo un caso de error), y muestra
un ejemplo de cuerpo JSON para un POST.

> La API documentada y probada corresponde al proyecto **`problem-1-and-2`**,
> un CRUD REST de estudiantes construido con Spring Boot, JPA y H2.

---

## 1. Documentación con Swagger (OpenAPI)

### ¿Qué es Swagger?

Swagger (ahora llamado **OpenAPI**) es un estándar para **describir APIs REST**
de forma legible tanto para humanos como para máquinas. En el ecosistema Spring
se implementa con **springdoc-openapi**, que genera automáticamente la
documentación a partir del código del controller.

### ¿Cómo lo integraría?

**Paso 1.** Agregar la dependencia en el `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

**Paso 2.** (Opcional pero recomendado) Anotar el controller con metadatos:

```java
@RestController
@RequestMapping("/estudiantes")
@Tag(name = "Estudiantes", description = "Operaciones CRUD sobre estudiantes")
public class EstudianteController {

    @Operation(summary = "Listar todos los estudiantes")
    @GetMapping
    public ResponseEntity<List<Estudiante>> listar() { ... }

    @Operation(summary = "Obtener un estudiante por id")
    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> uno(@PathVariable Long id) { ... }

    @Operation(summary = "Crear un estudiante")
    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante e) { ... }
}
```

**Paso 3.** Levantar la aplicación y acceder a:

- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

**¿Qué obtengo?**

- Una **interfaz web** donde se listan todos los endpoints agrupados por tag.
- Cada endpoint muestra sus **parámetros**, **cuerpo esperado** y **respuestas**.
- Un botón **"Try it out"** que permite ejecutar peticiones sin salir del navegador.
- El **JSON OpenAPI** que puede importarse a Postman o a otras herramientas.

**Ventajas:** la documentación se mantiene **siempre sincronizada** con el código
(al generarse desde las anotaciones), y sirve como contrato para el frontend.

---

## 2. Pruebas con Postman

Seleccioné **tres endpoints** para demostrar el flujo completo: uno de lectura,
uno de escritura y un **caso de error**.

### 🔧 Configuración previa

- Variable de entorno en Postman: `baseUrl = http://localhost:8080`
- Header por defecto en cada request: `Content-Type: application/json`

---

### ✅ Prueba 1 — Crear un estudiante (POST)

**Método:** `POST`
**URL:** `{{baseUrl}}/estudiantes`

**Body → raw → JSON:**

```json
{
  "nombre": "Ana Torres",
  "email": "ana.torres@example.com",
  "edad": 21
}
```

**Respuesta esperada:**

- **Status:** `201 Created`
- **Body:**

```json
{
  "id": 1,
  "nombre": "Ana Torres",
  "email": "ana.torres@example.com",
  "edad": 21
}
```

**Test de Postman (pestaña Tests):**

```javascript
pm.test("Status 201 Created", () => pm.response.to.have.status(201));
pm.test("Devuelve el id asignado", () => {
    pm.expect(pm.response.json()).to.have.property("id");
});
```

---

### ✅ Prueba 2 — Obtener un estudiante por id (GET)

**Método:** `GET`
**URL:** `{{baseUrl}}/estudiantes/1`

**Respuesta esperada:**

- **Status:** `200 OK`
- **Body:**

```json
{
  "id": 1,
  "nombre": "Ana Torres",
  "email": "ana.torres@example.com",
  "edad": 21
}
```

**Test de Postman:**

```javascript
pm.test("Status 200 OK", () => pm.response.to.have.status(200));
pm.test("Devuelve el estudiante correcto", () => {
    const data = pm.response.json();
    pm.expect(data.id).to.eql(1);
});
```

---

### ❌ Prueba 3 — Caso de error: estudiante no encontrado (GET)

**Método:** `GET`
**URL:** `{{baseUrl}}/estudiantes/999`

**Respuesta esperada:**

- **Status:** `404 Not Found`
- **Body:**

```json
{
  "error": "Estudiante no encontrado con id: 999"
}
```

**Test de Postman:**

```javascript
pm.test("Status 404 Not Found", () => pm.response.to.have.status(404));
pm.test("Devuelve mensaje de error", () => {
    pm.expect(pm.response.json()).to.have.property("error");
});
```

> **Nota:** este `404` lo devuelve el `GlobalExceptionHandler` del proyecto,
> que traduce la `RuntimeException` lanzada por el `EstudianteService`.

---

### 📋 Resumen de las pruebas

| # | Método | Endpoint | Escenario | Status esperado |
|---|--------|----------|-----------|-----------------|
| 1 | POST | `/estudiantes` | Crear | **201 Created** |
| 2 | GET | `/estudiantes/1` | Existe | **200 OK** |
| 3 | GET | `/estudiantes/999` | No existe | **404 Not Found** |

---

## 3. Ejemplo de cuerpo JSON para un POST

```json
{
  "nombre": "Ana Torres",
  "email": "ana.torres@example.com",
  "edad": 21
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Regla |
|-------|------|-------------|-------|
| `nombre` | String | Sí | No puede estar vacío |
| `email` | String | Sí | Debe contener `@` |
| `edad` | Integer | Sí | Debe ser `>= 0` |

> El campo `id` **no se envía** en el POST: la base de datos lo genera
> automáticamente gracias a `@GeneratedValue(strategy = GenerationType.IDENTITY)`.

---

## 4. Conclusión

La combinación de **Swagger** (documentación viva) y **Postman** (pruebas
funcionales manuales/automatizadas) permite:

- Tener un **contrato claro** de la API.
- **Probar cada endpoint** rápidamente.
- **Verificar códigos de estado** correctos (200, 201, 204, 400, 404).
- **Automatizar regresiones** usando Collection Runner o Newman en CI/CD.