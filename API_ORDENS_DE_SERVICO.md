# API de Ordens de Serviço

Esta documentação descreve os endpoints para o gerenciamento de Ordens de Serviço (OS).

## Base URL
```
http://localhost:8080/api/v1/ordens-servico
```

## Autenticação
A criação e atualização de status requerem autenticação via Token JWT. A consulta por ID é pública.

---

## Endpoints

### 1. Criar Ordem de Serviço
**POST** `/api/v1/ordens-servico`

Cria uma nova Ordem de Serviço. A aplicação valida se o veículo pertence ao cliente informado.

**Request Body:**
```json
{
  "cpfCnpjCliente": "11144477735",
  "placaVeiculo": "BRA2E19",
  "servicosIds": [
    "ID_DO_SERVICO_CRIADO_ANTERIORMENTE"
  ],
  "pecasIds": [
    "ID_DA_PECA_CRIADA_ANTERIORMENTE"
  ]
}
```

**Response (201 Created):**
```json
{
    "id": "d1e2f3a4-b5c6-7890-1234-567890abcdef",
    "clienteNome": "Carlos Andrade",
    "placaVeiculo": "BRA2E19",
    "status": "RECEBIDA",
    "valorTotal": 395.50,
    "dataCriacao": "2025-07-27T18:00:00"
}
```

### 2. Listar Todas as Ordens de Serviço
**GET** `/api/v1/ordens-servico`

Retorna uma lista de todas as OS.

**Response (200 OK):** (Array com objetos similares à resposta do `POST`)

### 3. Buscar Detalhes de uma OS (Público)
**GET** `/api/v1/ordens-servico/{id}`

Retorna os detalhes completos de uma OS. Este endpoint é público e não requer autenticação.

**Response (200 OK):**
```json
{
    "id": "d1e2f3a4-b5c6-7890-1234-567890abcdef",
    "status": "RECEBIDA",
    "valorTotal": 395.50,
    "dataCriacao": "2025-07-27T18:00:00",
    "dataFinalizacao": null,
    "dataEntrega": null,
    "cliente": {
        "id": "c1b3a2d4-e5f6-7890-1234-567890abcdef",
        "nome": "Carlos Andrade",
        "cpfCnpj": "11144477735",
        "email": "carlos.andrade@example.com",
        "telefone": "11987654321"
    },
    "veiculo": {
        "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
        "placa": "BRA2E19",
        "renavam": "12345678901",
        "marca": "Honda",
        "modelo": "Civic",
        "ano": 2022
    },
    "servicos": [
        {
            "id": "ID_DO_SERVICO_CRIADO_ANTERIORMENTE",
            "descricao": "Troca de óleo e filtro do motor",
            "preco": 350.00
        }
    ],
    "pecas": [
        {
            "id": "ID_DA_PECA_CRIADA_ANTERIORMENTE",
            "nome": "Filtro de Óleo Fram PH6017A",
            "fabricante": "Fram",
            "preco": 45.50,
            "estoque": 19
        }
    ]
}
```

### 4. Atualizar Status da OS
**PATCH** `/api/v1/ordens-servico/{id}/status`

Atualiza o status de uma OS existente.

**Request Body:**
```json
{
  "novoStatus": "EM_DIAGNOSTICO"
}
```

**Response (200 OK):** (similar à resposta do `POST`, com o status atualizado)

---

## Fluxo de Status
O status de uma OS só pode seguir a ordem abaixo:
`RECEBIDA` -> `EM_DIAGNOSTICO` -> `AGUARDANDO_APROVACAO` -> `EM_EXECUCAO` -> `FINALIZADA` -> `ENTREGUE`
