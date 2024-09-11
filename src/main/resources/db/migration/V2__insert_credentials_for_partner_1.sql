-- Inserir Credenciais para "parceiro-1"
INSERT INTO client (client_id, client_secret, grant_types, scopes, redirect_uri, blocked, failed_attempts)
VALUES ('parceiro-1', 'your_api_key', 'client_credentials', 'read,write', 'http://localhost/callback', FALSE, 0);