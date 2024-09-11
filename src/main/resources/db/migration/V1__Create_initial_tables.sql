-- Script para criar a tabela `client`
CREATE TABLE IF NOT EXISTS client (
    id SERIAL PRIMARY KEY,
    client_id VARCHAR(255) NOT NULL,
    client_secret VARCHAR(255) NOT NULL,
    grant_types VARCHAR(255),
    scopes VARCHAR(255),
    redirect_uri VARCHAR(255),
    blocked BOOLEAN DEFAULT FALSE,
    failed_attempts INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Script para criar a tabela `user`
CREATE TABLE "user" (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(255)
);