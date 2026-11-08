import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Login from './Login';

describe('Login', () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('login exitoso: llama a onLogin con el token', async () => {
    global.fetch = vi.fn().mockResolvedValueOnce({
      ok: true,
      json: async () => ({ token: 'fake-jwt-token' })
    });

    const onLogin = vi.fn();
    render(<Login onLogin={onLogin} />);

    await userEvent.click(screen.getByRole('button', { name: /entrar/i }));

    await waitFor(() => {
      expect(onLogin).toHaveBeenCalledWith('fake-jwt-token');
    });
  });

  it('login con error: muestra mensaje "Credenciales inválidas"', async () => {
    global.fetch = vi.fn().mockResolvedValueOnce({
      ok: false,
      status: 401,
      json: async () => ({})
    });

    const onLogin = vi.fn();
    render(<Login onLogin={onLogin} />);

    await userEvent.click(screen.getByRole('button', { name: /entrar/i }));

    expect(await screen.findByRole('alert')).toHaveTextContent('Credenciales inválidas');
    expect(onLogin).not.toHaveBeenCalled();
  });
});