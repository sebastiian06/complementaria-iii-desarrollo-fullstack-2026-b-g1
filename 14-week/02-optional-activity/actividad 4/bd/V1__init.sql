CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'ROLE_USER'
);

-- Seed: usuario admin / password "admin123"
-- Hash BCrypt (strength 10) precalculado
INSERT INTO users (username, password, role)
VALUES (
    'admin',
    '$2a$10$EwD98Ln7IhKasvelPsUyJego2LCEi.xjxUbEAa6p3ddkWyBBaLem2',
    'ROLE_ADMIN'
);