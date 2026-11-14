# Sistema de Gestão de Funcionários

API REST para gestão de funcionários, desenvolvida como desafio técnico com **Java 17**, **Spring Boot 4** e princípios de **DDD** e **Clean Code**.

![CI](https://github.com/Irinius777/sistema-de-gest-/actions/workflows/ci.yml/badge.svg?branch=prod)

## Tecnologias

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Bean Validation (Jakarta Validation + Hibernate Validator)
- Springdoc OpenAPI 3.2.0 (Swagger UI)
- Lombok
- Maven Wrapper
- JUnit 5, Mockito e AssertJ

## Arquitetura

O projeto é organizado por camadas, com o domínio isolado de detalhes de infraestrutura e de HTTP.

```text
src/main/java/br/com/brasmoto/gestao_funcionarios
└── funcionario
    ├── domain
    │   ├── Funcionario.java
    │   ├── FuncionarioId.java
    │   ├── Cpf.java
    │   ├── Email.java
    │   ├── Telefone.java
    │   ├── Endereco.java
    │   ├── Salario.java
    │   ├── Designacao.java
    │   ├── FuncionarioRepository.java
    │   ├── FuncionarioNaoEncontradoException.java
    │   ├── CpfJaCadastradoException.java
    │   └── EmailJaCadastradoException.java
    ├── application
    │   ├── api
    │   │   ├── FuncionarioAPI.java
    │   │   ├── FuncionarioController.java
    │   │   └── GlobalExceptionHandler.java
    │   ├── dto
    │   │   ├── FuncionarioRequest.java
    │   │   └── FuncionarioResponse.java
    │   └── service
    │       ├── FuncionarioService.java
    │       └── FuncionarioApplicationService.java
    └── infrastructure
        └── InMemoryFuncionarioRepository.java
```

### Decisões de projeto

- **Map como banco de dados**: conforme o desafio, o repositório usa um `ConcurrentHashMap` em memória.
- **Value Objects**: `Cpf`, `Email`, `Telefone`, `Endereco`, `Salario` e `FuncionarioId` validam e normalizam seus próprios dados.
- **Agregado imutável**: `Funcionario.atualizar(...)` retorna uma nova instância, evitando alteração parcial do objeto armazenado.
- **Porta e adaptador**: `FuncionarioRepository` (domínio) é implementado por `InMemoryFuncionarioRepository` (infraestrutura).
- **DTOs na camada de aplicação**: request/response ficam em `application/dto`, evitando que o serviço de aplicação dependa da camada web.
- **Erros padronizados**: respostas de erro usam `ProblemDetail` (RFC 7807).
- **OpenAPI**: contrato documentado com anotações do Springdoc.

### Revisão técnica e refinamentos

Após a implementação inicial, o projeto passou por uma revisão de engenharia com foco em arquitetura, consistência do domínio, contrato HTTP e qualidade dos testes.

| Área | Refinamento |
|---|---|
| Arquitetura | DTOs movidos para `application/dto`; o serviço de aplicação não depende mais da camada web. |
| Domínio | Agregado `Funcionario` imutável; `equals`/`hashCode` por identidade; normalização e validação mais rígidas de CPF, telefone, e-mail, endereço e nome. |
| Regras de negócio | Unicidade de CPF e e-mail com respostas `409 Conflict`. |
| API | `POST` retorna `201` com header `Location`; listagem ordenada alfabeticamente. |
| Erros | `ProblemDetail` padronizado para `400`, `404` e `409`; UUID inválido e rota inexistente tratados sem vazar stacktrace. |
| OpenAPI | Respostas de erro documentadas, schema de `ProblemDetail`, header `Location` e metadados da API. |
| Dependências | Springdoc atualizado para a versão 3.2.0, compatível com Spring Boot 4. |
| Testes | Cobertura ampliada para 86 testes, incluindo integração real, conflitos, validações e concorrência. |
| Repositório | README, pipeline de CI, `.gitattributes`, `.gitignore` e `mvnw` executável. |

Validação final:

- `./mvnw verify` com build bem-sucedido.
- 86 testes passando.
- Pipeline de CI verde no GitHub Actions.
- Swagger UI e OpenAPI funcionando no profile padrão.
- Profile `prod` desabilita Swagger/OpenAPI sem afetar a API.

## Como executar

Pré-requisitos: **Java 17 ou superior** e `JAVA_HOME` apontando para o JDK.

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
.\mvnw.cmd spring-boot:run
```

A API sobe em:

```text
http://localhost:8081/gestao-funcionarios/api
```

### Documentação interativa

- Swagger UI: `http://localhost:8081/gestao-funcionarios/api/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/gestao-funcionarios/api/v3/api-docs`

## Endpoints

| Método | Caminho | Descrição | Sucesso |
|---|---|---|---|
| `POST` | `/v1/funcionarios` | Cria um funcionário | `201 Created` + `Location` |
| `GET` | `/v1/funcionarios` | Lista funcionários em ordem alfabética | `200 OK` |
| `GET` | `/v1/funcionarios/{id}` | Busca funcionário por ID | `200 OK` |
| `PUT` | `/v1/funcionarios/{id}` | Atualiza um funcionário | `200 OK` |
| `DELETE` | `/v1/funcionarios/{id}` | Remove um funcionário | `204 No Content` |

## Exemplos

### Criar funcionário

```bash
curl -X POST http://localhost:8081/gestao-funcionarios/api/v1/funcionarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Joao Silva",
    "designacao": "DESENVOLVEDOR",
    "salario": 5000.00,
    "telefone": "(11) 99999-9999",
    "endereco": "Rua X, 100 - Sao Paulo/SP",
    "email": "joao@test.com",
    "cpf": "529.982.247-25"
  }'
```

Resposta:

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "nome": "Joao Silva",
  "designacao": "DESENVOLVEDOR",
  "salario": 5000.00,
  "telefone": "11999999999",
  "endereco": "Rua X, 100 - Sao Paulo/SP",
  "email": "joao@test.com",
  "cpf": "52998224725"
}
```

O CPF e o telefone são normalizados para somente dígitos. O e-mail é normalizado para minúsculas.

### Listar funcionários

```bash
curl http://localhost:8081/gestao-funcionarios/api/v1/funcionarios
```

### Buscar por ID

```bash
curl http://localhost:8081/gestao-funcionarios/api/v1/funcionarios/{id}
```

### Atualizar

```bash
curl -X PUT http://localhost:8081/gestao-funcionarios/api/v1/funcionarios/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Joao Silva",
    "designacao": "GERENTE",
    "salario": 9000.00,
    "telefone": "11988888888",
    "endereco": "Rua Z, 200 - Sao Paulo/SP",
    "email": "joao.silva@test.com",
    "cpf": "529.982.247-25"
  }'
```

### Remover

```bash
curl -X DELETE http://localhost:8081/gestao-funcionarios/api/v1/funcionarios/{id}
```

## Regras de negócio e validação

- `nome`: obrigatório, até 150 caracteres.
- `designacao`: obrigatória, deve ser um valor do enum (`DESENVOLVEDOR`, `ANALISTA`, `GERENTE`, `ESTAGIARIO`, `SUPORTE`).
- `salario`: obrigatório e maior que zero.
- `telefone`: obrigatório, 10 ou 11 dígitos após normalização.
- `endereco`: obrigatório, até 255 caracteres.
- `email`: obrigatório, formato válido, até 254 caracteres e único.
- `cpf`: obrigatório, dígitos verificadores válidos, normalizado para 11 dígitos e único.

### Códigos de erro

| Status | Situação |
|---|---|
| `400 Bad Request` | Dados inválidos, JSON malformado ou parâmetro inválido |
| `404 Not Found` | Funcionário não encontrado |
| `409 Conflict` | CPF ou e-mail já cadastrado |

Exemplo de erro:

```json
{
  "type": "about:blank",
  "title": "Conflict",
  "status": 409,
  "detail": "CPF ja cadastrado: 52998224725",
  "instance": "/gestao-funcionarios/api/v1/funcionarios"
}
```

Erros de validação incluem a lista `errors`:

```json
{
  "type": "about:blank",
  "title": "Erro de validacao",
  "status": 400,
  "detail": "Um ou mais campos sao invalidos",
  "instance": "/gestao-funcionarios/api/v1/funcionarios",
  "errors": [
    "email: email invalido",
    "cpf: cpf invalido"
  ]
}
```

## Testes

```bash
# Linux/macOS
./mvnw test

# Windows
.\mvnw.cmd test
```

A suíte cobre domínio, value objects, serviço de aplicação, controller, handler de exceções e repositório em memória, incluindo cenários de conflito, validação e concorrência.

## Limitações conhecidas

- Os dados são perdidos ao reiniciar a aplicação (repositório em memória).
- Não há autenticação/autorização.
- Não há paginação ou filtros na listagem.
- A verificação de unicidade de CPF/e-mail é feita na camada de aplicação; em cenários de alta concorrência ela não é atômica.
- Não há configuração de CORS.
- CPF e e-mail são retornados na resposta; em produção, avalie mascarar dados sensíveis.
- O Swagger fica habilitado por padrão; com o profile `prod` ele é desativado (`application-prod.yaml`).

## Próximos passos sugeridos

- Persistência com JPA e PostgreSQL.
- Paginação e filtros.
- Autenticação e autorização.
- CORS configurável por ambiente.
- Testes de integração com Testcontainers.
- Observabilidade com Actuator, métricas e tracing.
- Pipeline de deploy.

## Autor

Everton — [github.com/Irinius777](https://github.com/Irinius777)
