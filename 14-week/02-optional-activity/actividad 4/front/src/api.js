const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export async function login(username, password) {
  const res = await fetch(`${API_URL}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });

  if (!res.ok) {
    throw new Error('Credenciales inválidas');
  }
  const data = await res.json();
  return data.token;
}

export async function fetchPrivateHello(token) {
  const res = await fetch(`${API_URL}/api/private/hello`, {
    headers: { Authorization: `Bearer ${token}` }
  });

  if (!res.ok) {
    throw new Error('No autorizado');
  }
  return res.json();
}