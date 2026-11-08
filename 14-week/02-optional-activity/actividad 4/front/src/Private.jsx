import { useEffect, useState } from 'react';
import { fetchPrivateHello } from './api';

export default function Private({ token, onLogout }) {
  const [data, setData] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    fetchPrivateHello(token)
      .then((d) => {
        if (!cancelled) setData(d);
      })
      .catch((e) => {
        if (!cancelled) setError(e.message);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, [token]);

  return (
    <div className="card">
      <h1>Vista protegida</h1>
      {loading && <p>Cargando...</p>}
      {error && <p className="error" role="alert">{error}</p>}
      {data && (
        <div>
          <p><strong>Mensaje:</strong> {data.message}</p>
          <p><strong>Usuario:</strong> {data.user}</p>
          <p><strong>Rol:</strong> {data.authorities?.[0]?.authority}</p>
        </div>
      )}
      <button onClick={onLogout}>Cerrar sesión</button>
    </div>
  );
}