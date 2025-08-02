# 📝 Microsserviço de Gestão de Oficina

## 1. Visão Geral

Este projeto consiste num microsserviço robusto para a gestão de uma oficina mecânica, desenvolvido em **Java 21** com **Spring Boot 3**. O seu principal objetivo é gerir Ordens de Serviço (OS), Clientes, Veículos, Peças e Serviços, aplicando conceitos modernos de arquitetura de software para garantir escalabilidade, manutenibilidade e qualidade.

A aplicação foi construída sobre os pilares da **Arquitetura Hexagonal (Portas e Adaptadores)** e do **Domain-Driven Design (DDD)**, o que garante um baixo acoplamento entre a lógica de negócio e as tecnologias de infraestrutura.

---

## 2. 🛠️ Conceitos e Tecnologias

- **Arquitetura Hexagonal:** Isola o núcleo da aplicação de detalhes externos. As "Portas" (interfaces no domínio) definem os contratos, e os "Adaptadores" (classes na infraestrutura) implementam esses contratos.
- **Domain-Driven Design (DDD):** Foca em modelar o software para corresponder a um domínio de negócio. Utilizamos conceitos como Agregados, Entidades e Objetos de Valor.
- **Java 21 & Spring Boot 3:** Plataforma e framework principal.
- **PostgreSQL:** Base de dados relacional.
- **Spring Data JPA:** Para a camada de persistência.
- **Spring Security & JWT:** Para a segurança da API.
- **Spring Boot Mail:** Para a funcionalidade de envio de e-mails de notificação.
- **Docker & Docker Compose:** Para containerização e orquestração do ambiente.
- **Maven & JaCoCo:** Para gestão de dependências, build e relatório de cobertura de testes.
- **Swagger (OpenAPI 3):** Para documentação interativa da API RESTful.

---

## 3. ✨ Funcionalidades Implementadas

- **Fluxos Principais:** Criação e acompanhamento de Ordens de Serviço com gestão de status.
- **Envio de Orçamento:** Envio automático de um e-mail para o cliente quando o orçamento está pronto para aprovação.
- **Gestão Administrativa:** CRUDs completos para Clientes, Veículos, Peças e Serviços.
- **Segurança e Qualidade:** Autenticação JWT, validação de dados sensíveis (CPF/CNPJ, Placa) e testes automatizados.

---

## 4. ✅ Pré-requisitos

- **Java 21** ou superior
- **Maven 3.8** ou superior
- **Docker** & **Docker Compose**

---

## 5. 🚀 Como Executar o Projeto

### Passo 1: Configurar a Variável de Ambiente JWT
É uma boa prática de segurança fornecer o segredo do JWT através de uma variável de ambiente.

**No Linux/macOS:**
```bash
export JWT_SECRET="a29mYXNlNjRtdWl0b3NlZ3VyYWVzZWNyZXRhZG9zZWpjcmV0b2Rhb2ZpY2luYQ=="
```

**No Windows (PowerShell):**
```powershell
$env:JWT_SECRET="a29mYXNlNjRtdWl0b3NlZ3VyYWVzZWNyZXRhZG9zZWpjcmV0b2Rhb2ZpY2luYQ=="
```
*> Nota: Se esta variável não for definida, será utilizado um valor padrão não seguro, presente no ficheiro `docker-compose.yml`.*

### Passo 2: Iniciar os Containers
Na raiz do projeto, execute:
```bash
docker-compose up --build
```

### Passo 3: Aceder à Aplicação
- **API:** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Passo 4: Utilizar a API
1.  **Obter Token:** Use o endpoint `POST /api/v1/auth/login` com o utilizador `admin` e a palavra-passe `password`.
2.  **Autorizar:** Clique no botão "Authorize" no Swagger e cole o token no formato `Bearer <seu_token>`.

### Passo 5: Parar a Aplicação
Pressione `Ctrl + C` no terminal e depois execute `docker-compose down -v` para uma limpeza completa.

---

## 6. 📊 Gerando Relatório de Cobertura de Testes (JaCoCo)

Para gerar o relatório de cobertura, execute o seguinte comando na raiz do projeto:
```bash
mvn clean verify
```
O relatório estará disponível em `target/site/jacoco/index.html`.

---

## 7. 🏛️ Arquitetura de Agregados (DDD)

O design do domínio foi refinado para seguir as melhores práticas de DDD, focando em agregados pequenos e independentes para garantir transações atómicas e baixo acoplamento.

- **Agregado `OrdemDeServico` (Raiz):**
  - **Entidades Internas:** `ItemServico`, `ItemPeca`.
  - **Referências (IDs):** `clienteId`, `veiculoId`.
  - **Regra:** A `OrdemDeServico` não contém os objetos `Cliente` e `Veiculo` completos, apenas os seus IDs. Isto mantém o agregado coeso e as transações focadas. A validação de que o veículo pertence ao cliente é feita na camada de aplicação.

- **Agregado `Cliente` (Raiz):**
  - Entidade principal com o seu próprio ciclo de vida.

- **Agregado `Veiculo` (Raiz):**
  - Entidade principal com o seu próprio ciclo de vida. Referencia o `clienteId`.

- **Outros Agregados:** `Peca` e `Servico` também são raízes dos seus próprios agregados.

---

## 8. 📁 Estrutura do Projeto

```plaintext
oficina-service/
└── src/
    └── main/
        ├── java/
        │   └── br/com/grupo99/oficinaservice/
        │       ├── domain/
        │       │   ├── model/ (Entidades e Objetos de Valor)
        │       │   ├── repository/ (Interfaces/Ports)
        │       │   └── service/ (Serviços de Domínio)
        │       ├── application/
        │       │   ├── dto/ (Data Transfer Objects)
        │       │   ├── exception/ (Exceções Customizadas)
        │       │   ├── service/ (Implementação dos Casos de Uso)
        │       │   ├── usecase/ (Interfaces dos Casos de Uso)
        │       │   └── validator/ (Validadores Customizados)
        │       └── infrastructure/
        │           ├── config/ (Configurações do Spring)
        │           ├── notification/ (Adaptador de E-mail)
        │           ├── persistence/ (Adaptadores de Persistência)
        │           ├── rest/ (Controllers da API)
        │           └── security/ (Configuração do JWT)
        └── resources/
            └── application.properties
