# CRUD REST - Productos

API REST sencilla para gestionar productos, construida con **Spring Boot** y **Java**.

## 🚀 Requisitos

- Java 17 o superior (probado con Java 25)
- Maven (o usar el wrapper `mvnw`)
- Postman (para probar los endpoints)

## ▶️ Cómo ejecutar

```bash
.\mvnw.cmd spring-boot:run
```
**La aplicación queda disponible en http://localhost:8080.**

## 🧩 Arquitectura
- Controller → expone los endpoints REST

- Service → lógica de negocio y validaciones

- Repository → almacenamiento en memoria (se borra al reiniciar)

## 📌 Endpoints
|Método	|URL	|Acción	|Status|
|-------|-------|-------|------|
|GET	|/productos	|Listar todos	|200|
|GET	|/productos/{id}	|Obtener uno	|200|
|POST	|/productos	|Crear	|201|
|PUT	|/productos/{id}	|Actualizar	|200|
|DELETE	|/productos/{id}	|Eliminar	|204|

## 📮 Pruebas con Postman
En la carpeta postman/ están:

- CRUD-Productos.postman_collection.json — la colección con todos los requests.

- Local.postman_environment.json — el environment con la variable baseUrl.

## Cómo importar
1. Abre Postman.

2. Clic en Import.

3. Arrastra ambos archivos (o selecciónalos).

4. En el dropdown superior derecho, selecciona el environment Local.

5. Ejecuta los requests.
