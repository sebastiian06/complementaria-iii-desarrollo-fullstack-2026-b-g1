# Parcial Práctico — Corte 3
**Juan Sebastian Agudelo Quintero**

---

## Problema 1 — Seguridad (JWT, autenticación vs autorización)

### 1.1 Flujo de login con JWT

**Emisión del token (backend):**

```pseudo
FUNCION login(email, password):
    user = DB.buscarUsuarioPorEmail(email)
    SI user NO existe O NO verificarHash(password, user.passwordHash):
        RETORNAR 401 Unauthorized

    payload = { sub: user.id, rol: user.rol, iat: ahora() }
    token   = jwt.firmar(payload, SECRET, expiraEn: "15m")

    RETORNAR { accessToken: token }
```

El servidor firma con una clave secreta (`SECRET`, guardada en variable de entorno). El cliente **nunca** genera ni modifica el token; solo lo transporta.

**Almacenamiento en el frontend:**

- **Recomendado:** `localStorage` (o `sessionStorage`) para el `accessToken` corto; `refreshToken` en cookie `HttpOnly` + `Secure` + `SameSite=Strict` para mitigar XSS/CSRF.
- **Menos recomendado pero válido:** mantener el token solo en memoria (contexto de React) — más seguro ante XSS, se pierde al recargar.

**Protección de un endpoint (backend):**

```pseudo
MIDDLEWARE autenticar(req, res, next):
    header = req.headers.authorization           // "Bearer <token>"
    SI !header O !header.startsWith("Bearer "):
        RETORNAR 401
    token = header.split(" ")[1]
    INTENTAR:
        payload = jwt.verificar(token, SECRET)   // valida firma + expiración
        req.user = payload
        next()
    CATCH:
        RETORNAR 401 (token inválido o expirado)

MIDDLEWARE autorizar(rolesPermitidos):
    RETORNAR FUNCION(req, res, next):
        SI req.user.rol NO está en rolesPermitidos:
            RETORNAR 403 Forbidden
        next()

// Uso:
app.get("/api/pedidos", autenticar, autorizar(["admin","cliente"]), handlerPedidos)
```

### 1.2 Autenticación vs autorización

- **Autenticación:** *¿quién eres?* — verifica identidad (login con usuario/contraseña). Se resuelve emitiendo el JWT.
- **Autorización:** *¿qué puedes hacer?* — verifica permisos sobre un recurso (rol admin vs cliente). Se resuelve con el middleware `autorizar` y el `rol` del payload.

Ambas se aplican **en ese orden**: primero `autenticar` (401 si falla) y luego `autorizar` (403 si no tiene permiso).

### 1.3 English requirement

Authentication is the process of **verifying who a user is** (for example, checking credentials at login and issuing a JWT). Authorization happens **after** authentication and determines **what that identified user is allowed to do**, such as accessing admin-only endpoints based on their role. In short: authentication answers *"who are you?"*, while authorization answers *"what can you do?"*.

---

## Problema 2 — Integración y pruebas

### 2.1 Consumo de un endpoint protegido desde el frontend

```javascript
// hooks/usePedidos.js (React)
import { useEffect, useState } from "react";

export function usePedidos() {
  const [data, setData]       = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError]     = useState(null);

  useEffect(() => {
    const controller = new AbortController();           // cancelar si se desmonta
    const token = localStorage.getItem("accessToken");

    async function fetchPedidos() {
      setLoading(true);
      try {
        const res = await fetch(`${import.meta.env.VITE_API_URL}/api/pedidos`, {
          headers: { Authorization: `Bearer ${token}` },
          signal: controller.signal,
        });

        if (res.status === 401) { window.location.href = "/login"; return; }
        if (!res.ok) throw new Error(`Error ${res.status}: ${res.statusText}`);

        setData(await res.json());
      } catch (e) {
        if (e.name !== "AbortError") setError(e.message);
      } finally {
        setLoading(false);
      }
    }
    fetchPedidos();
    return () => controller.abort();
  }, []);

  return { data, loading, error };
}
```

- **Loading:** spinner mientras `loading === true`.
- **Error:** pantalla de reintento con `error`; 401 redirige a login.
- **Token:** se envía en `Authorization: Bearer <token>`.

---

### 2.2 Resolución de CORS

CORS se resuelve **en el backend** (el navegador es quien bloquea):

```javascript
// Express
import cors from "cors";
app.use(cors({
  origin: ["http://localhost:5173", "https://mi-front.vercel.app"],
  methods: ["GET","POST","PUT","DELETE"],
  allowedHeaders: ["Content-Type","Authorization"],
  credentials: true,
}));

// Manejo de preflight OPTIONS:
app.options("*", cors());
```

Puntos clave: no usar `origin: "*"` si se envían cookies; incluir `Authorization` en `allowedHeaders`; en producción, listar los dominios reales en una variable de entorno `CORS_ORIGIN`.

### 2.3 Pruebas propuestas

**Prueba unitaria** — `auth.middleware.test.js` (Jest):

```javascript
test("autenticar rechaza token inválido con 401", () => {
  const req  = { headers: { authorization: "Bearer token-falso" } };
  const res  = { status: jest.fn().mockReturnThis(), json: jest.fn() };
  const next = jest.fn();

  autenticar(req, res, next);

  expect(res.status).toHaveBeenCalledWith(401);
  expect(next).not.toHaveBeenCalled();
});
```

- **Qué verifica:** que el middleware de autenticación rechaza tokens inválidos y no deja pasar la petición. Aísla una unidad (sin BD ni red).

**Prueba de integración** — `pedidos.routes.test.js` (Supertest + BD de prueba):

```javascript
test("GET /api/pedidos con token válido devuelve 200 y lista", async () => {
  const token = firmarToken({ sub: 1, rol: "cliente" });
  const res = await request(app)
    .get("/api/pedidos")
    .set("Authorization", `Bearer ${token}`);

  expect(res.status).toBe(200);
  expect(Array.isArray(res.body)).toBe(true);
});

test("GET /api/pedidos sin token devuelve 401", async () => {
  const res = await request(app).get("/api/pedidos");
  expect(res.status).toBe(401);
});
```

- **Qué verifica:** el flujo completo **ruta → middleware → controlador → BD**, incluyendo autorización por rol y códigos HTTP reales.

---

## Problema 3 — Contenedores y despliegue

### 3.1 Dockerfile del backend

```dockerfile
# --- Etapa 1: build/dependencias ---
FROM node:20-alpine AS deps
WORKDIR /app
COPY package*.json ./
RUN npm ci --omit=dev

# --- Etapa 2: runtime ---
FROM node:20-alpine
WORKDIR /app
ENV NODE_ENV=production
COPY --from=deps /app/node_modules ./node_modules
COPY . .
EXPOSE 3000
USER node
CMD ["node", "src/server.js"]
```

---

### 3.2 Pasos de despliegue

**Local:**
```bash
docker build -t mi-backend:1.0 .
docker run --env-file .env -p 3000:3000 mi-backend:1.0
```
Verificar con `curl http://localhost:3000/health`.

**Nube (ej. Render / Railway / AWS ECS):**
1. Subir la imagen a un registry (`docker push`).
2. Crear el servicio apuntando a la imagen.
3. Configurar las variables de entorno en el panel del proveedor (no en el código).
4. Exponer el puerto y configurar el dominio + CORS del frontend.
5. Habilitar HTTPS y logs/monitoreo.

---

### 3.3 Variables de entorno

| Variable | Ejemplo | Por qué |
|---|---|---|
| `PORT` | `3000` | Distinto por entorno (local/nube). |
| `DATABASE_URL` | `postgres://...` | Credenciales de BD — nunca en el repo. |
| `JWT_SECRET` | (secreto largo) | Firma de tokens; rotarlo sin recompilar. |
| `JWT_EXPIRES_IN` | `15m` | Ajustar política de expiración. |
| `NODE_ENV` | `production` | Activa optimizaciones y desactiva debug. |
| `CORS_ORIGIN` | `https://mi-front.vercel.app` | Permitir solo el dominio real. |

**Razón general:** separar **configuración** del **código** permite desplegar la misma imagen en cualquier entorno, evita filtrar secretos en el repositorio y cumple el principio de *twelve-factor app*.

---