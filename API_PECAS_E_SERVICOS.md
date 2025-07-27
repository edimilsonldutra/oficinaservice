# API de Peças e Serviços

Esta documentação descreve os endpoints para gerenciamento de Peças e Serviços.

---

## Peças

### Base URL
```
http://localhost:8080/api/v1/pecas
```

### Endpoints de Peças

- **POST `/`**: Cria uma nova peça.
- **GET `/`**: Lista todas as peças.
- **GET `/{id}`**: Busca uma peça por ID.
- **PUT `/{id}`**: Atualiza uma peça.
- **DELETE `/{id}`**: Remove uma peça.
- **PATCH `/{id}/estoque`**: Adiciona ou remove itens do estoque.

**Request Body (POST / PUT):**
```json
{
  "nome": "Filtro de Ar Condicionado",
  "fabricante": "Bosch",
  "preco": 55.90,
  "estoque": 15
}
```

**Request Body (PATCH /estoque):**
```json
{
  "quantidade": 10
}
```

---

## Serviços

### Base URL
```
http://localhost:8080/api/v1/servicos
```

### Endpoints de Serviços

- **POST `/`**: Cria um novo serviço.
- **GET `/`**: Lista todos os serviços.
- **GET `/{id}`**: Busca um serviço por ID.
- **PUT `/{id}`**: Atualiza um serviço.
- **DELETE `/{id}`**: Remove um serviço.

**Request Body (POST / PUT):**
```json
{
  "descricao": "Troca de óleo e filtro do motor",
  "preco": 350.00
}
```

---

## Autenticação
Todos os endpoints de Peças e Serviços requerem autenticação via Token JWT.
