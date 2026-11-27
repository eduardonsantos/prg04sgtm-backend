# prg04sgtm-backend

Backend do **Sistema de Gerenciamento de Tributos Municipais (SGTM)** — API REST desenvolvida em **Java 21** com **Spring Boot** e **PostgreSQL** para o gerenciamento de contribuintes, imóveis, impostos municipais (como IPTU), lançamentos, boletos, pagamentos e usuários.

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.0.6**
  - Spring Web
  - Spring Data JPA
  - Spring Validation (Bean Validation)
- **PostgreSQL** (driver `org.postgresql:postgresql`)
- **Lombok**
- **Maven / Maven Wrapper** (`mvnw` / `mvnw.cmd`)
- **JUnit / Spring Boot Starter Test**

---

## Estrutura e Organização de Pastas

O repositório está organizado com o projeto Maven/Spring Boot dentro do subdiretório `prg04sgtm-backend/`, seguindo uma arquitetura **modular por domínio** (cada domínio concentra suas próprias camadas `controller`, `dto`, `entity`, `repository` e `service`), além de um pacote `infrastructure` para componentes transversais.

```text
prg04sgtm-backend/                        # Raiz do repositório
├── README.md                             # Documentação do projeto
├── login e senha admin.txt               # Credenciais padrão do administrador inicial
└── prg04sgtm-backend/                    # Diretório raiz do projeto Maven / Spring Boot
    ├── pom.xml                           # Dependências e configurações de build (Java 21, Spring Boot 4.0.6)
    ├── mvnw / mvnw.cmd                   # Maven Wrapper (Linux/macOS e Windows)
    ├── .mvn/                             # Configurações do Maven Wrapper
    ├── .env                              # Variáveis de ambiente locais (DB_URL, DB_USERNAME, DB_PASSWORD)
    └── src/
        ├── main/
        │   ├── java/br/com/ifba/
        │   │   ├── Application.java      # Classe principal de inicialização do Spring Boot
        │   │   │
        │   │   ├── boleto/               # Gestão de boletos da prefeitura
        │   │   │   ├── controller/       # BoletosPrefeituraController
        │   │   │   ├── dto/              # BoletosPrefeituraRequestDto, BoletosPrefeituraResponseDto, BoletoStatusRequestDto
        │   │   │   ├── entity/           # BoletosPrefeitura
        │   │   │   ├── repository/       # BoletosPrefeituraRepository
        │   │   │   └── service/          # BoletosPrefeituraIService, BoletosPrefeituraService
        │   │   │
        │   │   ├── contribuinte/         # Cadastro e gestão de contribuintes
        │   │   │   ├── controller/       # ContribuinteController
        │   │   │   ├── dto/              # ContribuinteRequestDto, ContribuinteResponseDto
        │   │   │   ├── entity/           # Contribuinte
        │   │   │   ├── repository/       # ContribuinteRepository
        │   │   │   └── service/          # ContribuinteIService, ContribuinteService
        │   │   │
        │   │   ├── endereco/             # Modelo compartilhado de endereço
        │   │   │   ├── dto/              # EnderecoDto
        │   │   │   └── entity/           # Endereco
        │   │   │
        │   │   ├── imovel/               # Cadastro e gestão de imóveis
        │   │   │   ├── controller/       # ImovelController
        │   │   │   ├── dto/              # ImovelRequestDto, ImovelResponseDto
        │   │   │   ├── entity/           # Imovel
        │   │   │   ├── repository/       # ImovelRepository
        │   │   │   └── service/          # ImovelIService, ImovelService
        │   │   │
        │   │   ├── imposto/              # Definição de tributos/impostos (ex.: IPTU)
        │   │   │   ├── controller/       # ImpostoController
        │   │   │   ├── dto/              # ImpostoRequestDto, ImpostoResponseDto
        │   │   │   ├── entity/           # Imposto, ImpostoIPTU
        │   │   │   ├── repository/       # ImpostoRepository
        │   │   │   └── service/          # ImpostoIService, ImpostoService
        │   │   │
        │   │   ├── lancamento/           # Lançamentos de impostos para contribuintes/imóveis
        │   │   │   ├── controller/       # LancamentoImpostoController
        │   │   │   ├── dto/              # LancamentoImpostoRequestDto, LancamentoImpostoResponseDto
        │   │   │   ├── entity/           # LancamentoImposto
        │   │   │   ├── repository/       # LancamentoImpostoRepository
        │   │   │   └── service/          # LancamentoImpostoIService, LancamentoImpostoService
        │   │   │
        │   │   ├── pagamento/            # Registro e consulta de pagamentos
        │   │   │   ├── controller/       # PagamentoController
        │   │   │   ├── dto/              # PagamentoRequestDto, PagamentoResponseDto
        │   │   │   ├── entity/           # Pagamento
        │   │   │   ├── repository/       # PagamentoRepository
        │   │   │   └── service/          # PagamentoIService, PagamentoService
        │   │   │
        │   │   ├── usuario/              # Gestão de usuários e autenticação
        │   │   │   ├── controller/       # UsuarioController
        │   │   │   ├── dto/              # LoginRequestDto, UsuarioRequestDto, UsuarioResponseDto
        │   │   │   ├── entity/           # Usuario
        │   │   │   ├── repository/       # UsuarioRepository
        │   │   │   └── service/          # UsuarioIService, UsuarioService
        │   │   │
        │   │   └── infrastructure/       # Componentes transversais e infraestrutura
        │   │       ├── configuration/    # ProgramInitializer (seed inicial de administrador)
        │   │       ├── entity/           # PersistenceEntity (entidade base JPA)
        │   │       ├── exception/        # GlobalExceptionHandler, ErrorResponseDto
        │   │       └── util/             # GeradorDocumentos, StringUtil
        │   │
        │   └── resources/
        │       └── application.properties # Configurações de banco de dados, JPA e importação de .env
        │
        └── test/
            ├── java/br/com/ifba/
            │   ├── ApplicationTests.java # Testes de contexto Spring Boot
            │   └── testes-api.http       # Requisições HTTP para testes manuais da API
            └── resources/postman/
                └── sgtm-postman-collection.json # Coleção do Postman para testar os endpoints
```

### Padrão Interno dos Módulos

Cada módulo de negócio (`boleto`, `contribuinte`, `imovel`, `imposto`, `lancamento`, `pagamento`, `usuario`) segue a separação em camadas:

- **`controller/`**: Exposição dos endpoints REST (`/api/v1/...`), validação de entrada e retorno de DTOs.
- **`dto/`**: Objetos de requisição (`*RequestDto`) e resposta (`*ResponseDto`) para desacoplar a API das entidades JPA.
- **`entity/`**: Entidades JPA mapeadas para as tabelas do PostgreSQL.
- **`repository/`**: Interfaces Spring Data JPA para persistência e consultas.
- **`service/`**: Interface (`*IService`) e implementação (`*Service`) contendo as regras de negócio.

---

## Configuração e Execução

### Pré-requisitos

- **Java 21** instalado
- **PostgreSQL** em execução com um banco de dados criado para a aplicação

### Variáveis de Ambiente

O arquivo `prg04sgtm-backend/src/main/resources/application.properties` carrega automaticamente um arquivo `.env` (caso exista) e espera as seguintes variáveis de conexão com o banco:

```properties
DB_URL=jdbc:postgresql://localhost:5432/seu_banco
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
```

> **Observação:** O Hibernate está configurado com `spring.jpa.hibernate.ddl-auto=update`, criando/atualizando as tabelas automaticamente ao iniciar a aplicação.

### Executando a Aplicação

Acesse a pasta do projeto Maven e utilize o Maven Wrapper:

**Linux / macOS:**
```bash
cd prg04sgtm-backend
./mvnw spring-boot:run
```

**Windows (PowerShell / CMD):**
```powershell
cd prg04sgtm-backend
.\mvnw.cmd spring-boot:run
```

### Usuário Administrador Inicial

Ao iniciar a aplicação com o banco sem usuários ativos, a classe `ProgramInitializer` cria automaticamente um usuário administrador padrão:

- **Login:** `admin`
- **Senha:** `admin123`
- **Nível de acesso:** `ADMIN`

---

## Endpoints Principais da API

Todos os recursos estão versionados sob o prefixo `/api/v1`:

| Módulo | Base URL | Operações disponíveis |
| :--- | :--- | :--- |
| **Boletos** | `/api/v1/boletos` | `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}`, `PATCH /{id}/status` |
| **Contribuintes** | `/api/v1/contribuintes` | `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| **Imóveis** | `/api/v1/imoveis` | `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| **Impostos** | `/api/v1/impostos` | `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| **Lançamentos** | `/api/v1/lancamentos` | `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| **Pagamentos** | `/api/v1/pagamentos` | `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| **Usuários** | `/api/v1/usuarios` | `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}`, `POST /login` |

---

## Testes e Ferramentas de API

- **Executar testes automatizados:**
  ```bash
  cd prg04sgtm-backend
  ./mvnw test
  ```
- **Testes manuais via IDE (HTTP Client):** utilize o arquivo `prg04sgtm-backend/src/test/java/br/com/ifba/testes-api.http`.
- **Postman:** importe a coleção disponível em `prg04sgtm-backend/src/test/resources/postman/sgtm-postman-collection.json`.
