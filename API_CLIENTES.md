# API de Clientes

Esta documentação descreve os endpoints disponíveis para o gerenciamento de clientes no sistema da oficina.

## Base URL
```
http://localhost:8080/api/v1/clientes
```

## Autenticação
Todos os endpoints desta API requerem autenticação via Token JWT. O token deve ser enviado no cabeçalho `Authorization` com o prefixo `Bearer`.

---

## Endpoints

### 1. Criar Cliente
**POST** `/api/v1/clientes`

Cria um novo cliente na base de dados.

**Request Body:**
```json
{
    "nome": "Carlos Andrade",
    "cpfCnpj": "11144477735",
    "email": "carlos.andrade@example.com",
    "telefone": "11987654321"
}
```

**Response (201 Created):**
```json
{
    "id": "c1b3a2d4-e5f6-7890-1234-567890abcdef",
    "nome": "Carlos Andrade",
    "cpfCnpj": "11144477735",
    "email": "carlos.andrade@example.com",
    "telefone": "11987654321"
}
```

### 2. Listar Todos os Clientes
**GET** `/api/v1/clientes`

Retorna uma lista de todos os clientes cadastrados.

**Response (200 OK):**
```json
[
    {
        "id": "c1b3a2d4-e5f6-7890-1234-567890abcdef",
        "nome": "Carlos Andrade",
        "cpfCnpj": "11144477735",
        "email": "carlos.andrade@example.com",
        "telefone": "11987654321"
    }
]
```

### 3. Buscar Cliente por ID
**GET** `/api/v1/clientes/{id}`

Retorna um cliente específico pelo seu ID (UUID).

**Response (200 OK):**
```json
{
    "id": "c1b3a2d4-e5f6-7890-1234-567890abcdef",
    "nome": "Carlos Andrade",
    "cpfCnpj": "11144477735",
    "email": "carlos.andrade@example.com",
    "telefone": "11987654321"
}
```

### 4. Atualizar Cliente
**PUT** `/api/v1/clientes/{id}`

Atualiza os dados de um cliente existente.

**Request Body:**
```json
{
    "nome": "Carlos Andrade da Silva",
    "cpfCnpj": "11144477735",
    "email": "carlos.silva@example.com",
    "telefone": "11988887777"
}
```

**Response (200 OK):**
```json
{
    "id": "c1b3a2d4-e5f6-7890-1234-567890abcdef",
    "nome": "Carlos Andrade da Silva",
    "cpfCnpj": "11144477735",
    "email": "carlos.silva@example.com",
    "telefone": "11988887777"
}
```

### 5. Excluir Cliente
**DELETE** `/api/v1/clientes/{id}`

Remove um cliente da base de dados.

**Response (204 No Content)**

---

## Validações

- **CPF/CNPJ**: Deve ser um CPF (11 dígitos) ou CNPJ (14 dígitos) válido e único no sistema.
- **Campos Obrigatórios**: `nome`, `cpfCnpj`.

## Códigos de Erro

- **400 Bad Request**: Dados inválidos (ex: CPF/CNPJ com formato incorreto).
- **403 Forbidden**: Acesso negado (falta de token JWT ou token inválido).
- **404 Not Found**: Cliente não encontrado para o ID fornecido.
