import { useState } from 'react';
import Login from './Login';
import Private from './Private';

export default function App() {
  const [token, setToken] = useState(localStorage.getItem('token') || '');

  function handleLogin(t) {
    localStorage.setItem('token', t);
    setToken(t);
  }

  function handleLogout() {
    localStorage.removeItem('token');
    setToken('');
  }

  return (
    <div className="app">
      {token
        ? <Private token={token} onLogout={handleLogout} />
        : <Login onLogin={handleLogin} />}
    </div>
  );
}