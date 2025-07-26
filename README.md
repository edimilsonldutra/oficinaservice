# 📝 Microsserviço de Gestão de Oficina

## 1. Visão Geral

Este projeto consiste num microsserviço robusto para a gestão de uma oficina mecânica, desenvolvido em **Java 21** com **Spring Boot 3**. O seu principal objetivo é gerir Ordens de Serviço (OS), Clientes, Veículos, Peças e Serviços, aplicando conceitos modernos de arquitetura de software para garantir escalabilidade, manutenibilidade e qualidade.

A aplicação foi construída sobre os pilares da **Arquitetura Hexagonal (Portas e Adaptadores)** e do **Domain-Driven Design (DDD)**, o que garante um baixo acoplamento entre a lógica de negócio e as tecnologias de infraestrutura (como base de dados e frameworks web).

---

## 2. 🛠️ Conceitos e Tecnologias

- **Arquitetura Hexagonal:** Isola o núcleo da aplicação (domínio e casos de uso) de detalhes externos. As "Portas" (interfaces no domínio) definem os contratos, e os "Adaptadores" (classes na infraestrutura) implementam esses contratos, conectando a aplicação ao mundo exterior (API REST, base de dados, etc.).
- **Domain-Driven Design (DDD):** Foca em modelar o software para corresponder a um domínio de negócio. Utilizamos conceitos como Agregados (`OrdemServico`), Entidades, e Serviços de Domínio para criar um modelo rico e expressivo.
- **Java 21 & Spring Boot 3:** Plataforma e framework principal para o desenvolvimento do back-end.
- **PostgreSQL:** Base de dados relacional escolhida pela sua robustez, fiabilidade e funcionalidades avançadas.
- **Spring Data JPA:** Para a camada de persistência de dados.
- **Spring Security & JWT:** Para garantir a segurança da API através de autenticação e autorização baseadas em tokens.
- **Docker & Docker Compose:** Para containerização da aplicação e orquestração do ambiente de desenvolvimento, garantindo uma configuração simples e consistente.
- **Maven:** Como ferramenta de gestão de dependências e build.
- **Swagger (OpenAPI 3):** Para documentação interativa da API RESTful.

---

## 3. ✨ Funcionalidades Implementadas

### Fluxos Principais
- **Criação de Ordem de Serviço:**
    - Identificação do cliente e do veículo.
    - Adição de peças e serviços.
    - Cálculo automático do orçamento.
- **Acompanhamento da OS:**
    - Gestão de status: `RECEBIDA`, `EM_DIAGNOSTICO`, `AGUARDANDO_APROVACAO`, `EM_EXECUCAO`, `FINALIZADA`, `ENTREGUE`.
    - API pública para consulta de status por ID.

### Gestão Administrativa (CRUDs)
- Gestão completa de Clientes.
- Gestão completa de Veículos.
- Gestão completa de Peças (com controlo de stock).
- Gestão completa de Serviços.

### Segurança e Qualidade
- Autenticação JWT para todos os endpoints administrativos.
- Validação de dados de entrada nos DTOs.
- Testes unitários e de integração para os principais fluxos.

---

## 4. ✅ Pré-requisitos

Para executar este projeto localmente, necessitará de ter instalado:

- **Java 21** ou superior
- **Maven 3.8** ou superior
- **Docker**
- **Docker Compose**

---

## 5. 🚀 Como Executar o Projeto

A forma mais simples de executar a aplicação é através do Docker Compose, que orquestra tanto o serviço da aplicação como a base de dados.

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
Na raiz do projeto (onde se encontra o ficheiro `docker-compose.yml`), execute o seguinte comando:
```bash
docker-compose up --build
```
Este comando irá:
1. Construir a imagem Docker da aplicação Java (`oficina-app`).
2. Descarregar a imagem do PostgreSQL (`oficina-db`).
3. Iniciar ambos os containers numa rede dedicada, garantindo que a aplicação só arranca depois de a base de dados estar pronta.

### Passo 3: Aceder à Aplicação
Após a inicialização, os seguintes serviços estarão disponíveis:

- **API Principal:** `http://localhost:8080`
- **Documentação Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Passo 4: Utilizar a API
1.  **Obter um Token de Autenticação:**
    - Aceda à documentação do Swagger.
    - Vá ao endpoint `POST /api/v1/auth/login`.
    - Utilize o utilizador de demonstração:
      ```json
      {
        "username": "admin",
        "password": "password"
      }
      ```
    - Copie o token JWT da resposta.

2.  **Autorizar os Pedidos:**
    - No topo direito da página Swagger, clique no botão "Authorize".
    - Na janela que se abre, cole o token JWT no formato `Bearer <seu_token>`.
    - Agora pode testar todos os endpoints protegidos.

### Passo 5: Parar a Aplicação
Para parar os containers, pressione `Ctrl + C` no terminal. Para os remover completamente (incluindo a rede), execute:
```bash
docker-compose down
```
Para remover também os dados persistidos da base de dados:
```bash
docker-compose down -v
```

---

## 6. 📁 Estrutura do Projeto

O projeto segue a estrutura da Arquitetura Hexagonal, que promove a separação de responsabilidades:
```plaintext
oficina-service/
└── src/
    └── main/
        ├── java/
        │   └── br/com/grupo/oficinaservice/
        │       ├── domain/
        │       │   ├── model/
        │       │   │   ├── Cliente.java
        │       │   │   ├── ItemPeca.java
        │       │   │   ├── ItemServico.java
        │       │   │   ├── OrdemServico.java (Aggregate Root)
        │       │   │   ├── Peca.java
        │       │   │   ├── Servico.java
        │       │   │   ├── StatusOS.java
        │       │   │   └── Veiculo.java
        │       │   ├── repository/ (Ports)
        │       │   │   ├── ClienteRepository.java
        │       │   │   ├── OrdemServicoRepository.java
        │       │   │   ├── PecaRepository.java
        │       │   │   ├── ServicoRepository.java
        │       │   │   └── VeiculoRepository.java
        │       │   └── service/
        │       │       └── OrcamentoService.java
        │       ├── application/
        │       │   ├── dto/
        │       │   │   ├── ClienteDTOs.java
        │       │   │   ├── OrdemServicoDTOs.java
        │       │   │   ├── PecaDTOs.java
        │       │   │   ├── ServicoDTOs.java
        │       │   │   └── VeiculoDTOs.java
        │       │   ├── exception/
        │       │   │   ├── BusinessException.java
        │       │   │   └── ResourceNotFoundException.java
        │       │   ├── service/ (Use Case Implementations)
        │       │   │   ├── ClienteApplicationService.java
        │       │   │   ├── OrdemServicoApplicationService.java
        │       │   │   ├── PecaApplicationService.java
        │       │   │   ├── ServicoApplicationService.java
        │       │   │   └── VeiculoApplicationService.java
        │       │   └── usecase/ (Use Case Interfaces)
        │       │       ├── AcompanharOrdemServicoUseCase.java
        │       │       ├── AutenticarUsuarioUseCase.java
        │       │       ├── ... (e outros casos de uso)
        │       └── infrastructure/
        │           ├── config/
        │           │   ├── OpenApiConfig.java
        │           │   └── SecurityConfig.java
        │           ├── persistence/ (Adapters)
        │           │   ├── jpa/
        │           │   │   ├── ClienteJpaRepository.java
        │           │   │   ├── OrdemServicoJpaRepository.java
        │           │   │   ├── ... (e outros repositórios JPA)
        │           │   └── repository/
        │           │       ├── ClienteRepositoryImpl.java
        │           │       ├── OrdemServicoRepositoryImpl.java
        │           │       ├── ... (e outras implementações)
        │           ├── rest/ (Adapters)
        │           │   ├── AuthController.java
        │           │   ├── ClienteController.java
        │           │   ├── OrdemServicoController.java
        │           │   ├── PecaController.java
        │           │   ├── ServicoController.java
        │           │   └── VeiculoController.java
        │           └── security/
        │               ├── jwt/
        │               │   ├── AuthRequest.java
        │               │   ├── AuthResponse.java
        │               │   ├── JwtRequestFilter.java
        │               │   └── JwtUtil.java
        │               └── UserDetailsServiceImpl.java
        └── resources/
            └── application.properties
