# API de Autenticação

Esta documentação descreve o endpoint disponível para a autenticação de utilizadores e a geração de tokens JWT.

## Base URL
```
http://localhost:8080/api/v1/auth
```

## Autenticação
Este é o único endpoint público da aplicação, desenhado para que os utilizadores possam obter um token de acesso.

---

## Endpoints

### 1. Autenticar Utilizador e Gerar Token
**POST** `/api/v1/auth/login`

Recebe as credenciais do utilizador (nome de utilizador e palavra-passe) e, se forem válidas, retorna um token JWT.

**Request Body:**
```json
{
    "username": "admin",
    "password": "password"
}
```

**Response (200 OK):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1MzU2MzM5NCwiZXhwIjoxNzUzNTY2OTk0fQ.Qj1pGtTeRhPp6ixxOydlweLUE6QKIom53ORApEDd4O0"
}
```
> **Nota:** O token retornado deve ser utilizado no cabeçalho `Authorization` com o prefixo `Bearer ` para aceder aos outros endpoints protegidos da API.

---

## Códigos de Erro

- **401 Unauthorized**: As credenciais (nome de utilizador ou palavra-passe) fornecidas estão incorretas.
- **403 Forbidden**: Ocorre se, por alguma falha de configuração, o endpoint de login estiver protegido.
- **400 Bad Request**: O corpo da requisição está mal formatado ou em falta.
