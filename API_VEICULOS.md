# API de Veículos

Esta documentação descreve os endpoints disponíveis para o gerenciamento de veículos.

## Base URL
```
http://localhost:8080/api/v1/veiculos
```

## Autenticação
Todos os endpoints desta API requerem autenticação via Token JWT.

---

## Endpoints

### 1. Criar Veículo
**POST** `/api/v1/veiculos`

Cria um novo veículo e o associa a um cliente existente.

**Request Body:**
```json
{
    "placa": "BRA2E19",
    "renavam": "12345678901",
    "marca": "Honda",
    "modelo": "Civic",
    "ano": 2022,
    "clienteId": "c1b3a2d4-e5f6-7890-1234-567890abcdef"
}
```

**Response (201 Created):**
```json
{
    "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
    "placa": "BRA2E19",
    "renavam": "12345678901",
    "marca": "Honda",
    "modelo": "Civic",
    "ano": 2022
}
```

### 2. Listar Todos os Veículos
**GET** `/api/v1/veiculos`

Retorna uma lista de todos os veículos cadastrados.

**Response (200 OK):**
```json
[
    {
        "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
        "placa": "BRA2E19",
        "renavam": "12345678901",
        "marca": "Honda",
        "modelo": "Civic",
        "ano": 2022
    }
]
```

### 3. Buscar Veículo por ID
**GET** `/api/v1/veiculos/{id}`

Retorna um veículo específico pelo seu ID (UUID).

**Response (200 OK):** (similar à resposta do `POST`)

### 4. Atualizar Veículo
**PUT** `/api/v1/veiculos/{id}`

Atualiza os dados de um veículo existente.

**Request Body:**
```json
{
    "placa": "BRA2E19",
    "renavam": "12345678901",
    "marca": "Honda",
    "modelo": "Civic Touring",
    "ano": 2023,
    "clienteId": "c1b3a2d4-e5f6-7890-1234-567890abcdef"
}
```

**Response (200 OK):** (similar à resposta do `POST`, com os dados atualizados)

### 5. Excluir Veículo
**DELETE** `/api/v1/veiculos/{id}`

Remove um veículo da base de dados.

**Response (204 No Content)**

---

## Validações

- **Placa**: Deve seguir o padrão Mercosul (`ABC1D23`) ou o padrão antigo (`ABC-1234`).
- **Campos Obrigatórios**: `placa`, `marca`, `modelo`, `ano`, `clienteId`.
- **`clienteId`**: Deve ser o ID de um cliente que já existe no sistema.

## Códigos de Erro

- **400 Bad Request**: Dados inválidos (ex: placa com formato incorreto).
- **403 Forbidden**: Acesso negado.
- **404 Not Found**: Veículo ou Cliente não encontrado para o ID fornecido.
