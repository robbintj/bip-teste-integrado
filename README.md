# 🏗️ Sistema de Gerenciamento de Benefícios - Fullstack

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-18-red.svg)](https://angular.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Sistema completo de gerenciamento de benefícios desenvolvido com **Spring Boot**, **Jakarta EE (EJB)**, **PostgreSQL** e **Angular 18** com Standalone Components.

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)
- [Endpoints da API](#endpoints-da-api)
- [Testes](#testes)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Decisões Técnicas](#decisões-técnicas)
- [Licença](#licença)

---

## 🎯 Sobre o Projeto

Sistema fullstack para gerenciamento de benefícios corporativos com funcionalidades de:
- ✅ CRUD completo de benefícios
- ✅ Transferência de valores entre benefícios (com validações e controle de concorrência)
- ✅ Interface moderna e responsiva
- ✅ Documentação Swagger/OpenAPI
- ✅ Testes automatizados

### 🏆 Destaques

- **Pessimistic Locking** para evitar race conditions em transferências
- **Clean Architecture** com separação clara de responsabilidades
- **Standalone Components** do Angular 18 (sem NgModule)
- **Bootstrap 5** para UI/UX profissional
- **Toast Notifications** para feedback ao usuário
- **MapStruct** para mapeamento eficiente de DTOs
- **Docker Compose** para ambiente de desenvolvimento

---

## ✨ Funcionalidades

### Backend
- 🔹 **Listagem de Benefícios** - GET com paginação
- 🔹 **Busca por ID** - GET com validação
- 🔹 **Criação** - POST com validações Jakarta
- 🔹 **Atualização** - PUT com controle de versão
- 🔹 **Exclusão** - DELETE com verificações
- 🔹 **Transferência** - POST com Pessimistic Locking e validações de saldo

### Frontend
- 🎨 **Dashboard** - Cards responsivos com informações dos benefícios
- ➕ **Criar Benefício** - Formulário reativo com validações
- ✏️ **Editar Benefício** - Formulário pré-preenchido
- 🗑️ **Excluir Benefício** - Confirmação antes da exclusão
- 💸 **Transferir Valores** - Interface intuitiva com cálculo de saldo em tempo real
- 🔔 **Notificações Toast** - Feedback visual de operações

---

## 🚀 Tecnologias

### Backend
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.2.5 | Framework web |
| Jakarta EE | 10 | EJB para lógica de negócio |
| PostgreSQL | 17 | Banco de dados |
| Hibernate | 6.4.4 | ORM |
| Lombok | 1.18.30 | Redução de boilerplate |
| MapStruct | 1.5.5 | Mapeamento de DTOs |
| SpringDoc | 2.3.0 | Documentação OpenAPI/Swagger |
| JUnit 5 | - | Testes unitários |
| Mockito | - | Mocks para testes |

### Frontend
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Angular | 18 | Framework SPA |
| TypeScript | 5.x | Linguagem tipada |
| Bootstrap | 5.3 | Framework CSS |
| Bootstrap Icons | 1.13 | Ícones |
| RxJS | 7.8 | Programação reativa |
| Angular Signals | - | Reatividade moderna |
| Zone.js | - | Change detection |

### DevOps
- **Docker** - Containerização do PostgreSQL
- **Maven** - Gerenciamento de dependências e build
- **Git** - Controle de versão

---

## 🏛️ Arquitetura

### Backend - Clean Architecture

```
┌─────────────────────────────────────────────┐
│          REST API (Controllers)             │
├─────────────────────────────────────────────┤
│         Service Layer (Spring)              │
├─────────────────────────────────────────────┤
│       Business Logic (EJB Services)         │
├─────────────────────────────────────────────┤
│          Persistence (JPA/Hibernate)        │
├─────────────────────────────────────────────┤
│           Database (PostgreSQL)             │
└─────────────────────────────────────────────┘
```

### Fluxo de Dados

```
HTTP Request → Controller → Service → EJB → JPA → Database
                  ↓            ↓
                DTO        Mapper
```

### Frontend - Component Architecture

```
App Component
├── Beneficio List Component
├── Beneficio Form Component  
├── Transfer Component
└── Toast Component

Services
├── Beneficio Service (HTTP)
└── Toast Service (State Management)
```

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17+** ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **Node.js 18+** ([Download](https://nodejs.org/))
- **Angular CLI** (`npm install -g @angular/cli`)
- **Docker & Docker Compose** ([Download](https://www.docker.com/))
- **Git** ([Download](https://git-scm.com/))

---

## 🔧 Instalação e Execução

### 1️⃣ Clone o Repositório

```bash
git clone <repository-url>
cd bip-teste-integrado
```

### 2️⃣ Iniciar PostgreSQL (Docker)

```bash
docker-compose up -d
```

**Verificar se está rodando:**
```bash
docker ps | grep bip-db
```

### 3️⃣ Executar Scripts SQL

```bash
docker exec -i bip-db psql -U postgres -d bip < db/schema.sql
docker exec -i bip-db psql -U postgres -d bip < db/seed.sql
```

### 4️⃣ Iniciar Backend

**Importante:** O projeto requer Java 17 ou superior. Se tiver problemas de compilação, configure o JAVA_HOME:

```bash
# Configurar Java (ajuste o caminho conforme sua instalação)
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home

# Ou no Windows:
# set JAVA_HOME=C:\Program Files\Java\jdk-21
```

Depois execute:

```bash
cd backend-module
mvn clean install -DskipTests
mvn spring-boot:run
```

**Aguarde a mensagem:**
```
Started BackendApplication in X.XXX seconds
```

### 5️⃣ Iniciar Frontend

**Terminal separado:**
```bash
cd frontend
npm install
ng serve
```

**Aguarde a mensagem:**
```
✔ Browser application bundle generation complete.
Local: http://localhost:4200/
```

### 6️⃣ Acessar Aplicação

- **Frontend:** http://localhost:4200
- **API:** http://localhost:8080/api/v1/beneficios
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **H2 Console:** (não aplicável - usando PostgreSQL)

---

## 📡 Endpoints da API

### Base URL: `http://localhost:8080/api/v1/beneficios`

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| GET | `/` | Lista todos os benefícios | - |
| GET | `/{id}` | Busca benefício por ID | - |
| POST | `/` | Cria novo benefício | BeneficioRequestDTO |
| PUT | `/{id}` | Atualiza benefício | BeneficioRequestDTO |
| DELETE | `/{id}` | Remove benefício | - |
| POST | `/transfer` | Transfere valores | TransferRequestDTO |

### Exemplos de Request/Response

#### ➕ Criar Benefício

**POST** `/api/v1/beneficios`

```json
{
  "nome": "Vale Alimentação",
  "descricao": "Benefício de alimentação para colaboradores",
  "valor": 1000.00,
  "ativo": true
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "nome": "Vale Alimentação",
  "descricao": "Benefício de alimentação para colaboradores",
  "valor": 1000.00,
  "ativo": true,
  "version": 0
}
```

#### 💸 Transferir Valores

**POST** `/api/v1/beneficios/transfer`

```json
{
  "fromId": 1,
  "toId": 2,
  "amount": 250.00
}
```

**Response:** `200 OK`

---

## 🧪 Testes

### Backend - JUnit + Mockito

**Executar todos os testes:**
```bash
cd backend-module
mvn test
```

**Testes implementados:**
- ✅ `BeneficioServiceTest` - 8 testes unitários
- ✅ `BeneficioControllerTest` - 7 testes de integração

**Cobertura:** >90% do código crítico

### Frontend - Jasmine + Karma

**Executar testes:**
```bash
cd frontend
ng test
```

**Testes implementados:**
- ✅ `app.component.spec.ts` - Testes do componente principal

---

## 📂 Estrutura do Projeto

```
bip-teste-integrado/
├── backend-module/                 # Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/backend/
│   │   │   │       ├── BeneficioController.java
│   │   │   │       ├── service/
│   │   │   │       │   └── BeneficioService.java
│   │   │   │       ├── dto/
│   │   │   │       │   ├── BeneficioRequestDTO.java
│   │   │   │       │   ├── BeneficioResponseDTO.java
│   │   │   │       │   └── TransferRequestDTO.java
│   │   │   │       ├── mapper/
│   │   │   │       │   └── BeneficioMapper.java
│   │   │   │       ├── exception/
│   │   │   │       │   ├── GlobalExceptionHandler.java
│   │   │   │       │   └── ErrorResponseDTO.java
│   │   │   │       └── config/
│   │   │   │           ├── EjbConfig.java
│   │   │   │           ├── JpaConfig.java
│   │   │   │           └── CorsConfig.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── java/
│   │           ├── BeneficioServiceTest.java
│   │           └── BeneficioControllerTest.java
│   └── pom.xml
│
├── ejb-module/                     # Módulo EJB
│   ├── src/main/java/
│   │   └── com/example/ejb/
│   │       ├── Beneficio.java
│   │       ├── BeneficioEjbService.java
│   │       └── exception/
│   │           ├── InsufficientBalanceException.java
│   │           ├── BeneficioNotFoundException.java
│   │           └── InvalidTransferException.java
│   └── pom.xml
│
├── frontend/                       # Frontend Angular
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   │   ├── beneficio-list/
│   │   │   │   ├── beneficio-form/
│   │   │   │   ├── transfer/
│   │   │   │   └── toast/
│   │   │   ├── services/
│   │   │   │   ├── beneficio.service.ts
│   │   │   │   └── toast.service.ts
│   │   │   ├── models/
│   │   │   │   ├── beneficio-request.dto.ts
│   │   │   │   ├── beneficio-response.dto.ts
│   │   │   │   ├── transfer-request.dto.ts
│   │   │   │   └── error-response.dto.ts
│   │   │   ├── app.component.ts
│   │   │   ├── app.routes.ts
│   │   │   └── app.config.ts
│   │   ├── main.ts
│   │   └── styles.scss
│   ├── package.json
│   └── angular.json
│
├── db/                             # Scripts SQL
│   ├── schema.sql
│   └── seed.sql
│
├── docker-compose.yml              # PostgreSQL Docker
├── pom.xml                         # Parent POM
└── README.md                       # Este arquivo
```

---

## 💡 Decisões Técnicas

### 🔒 Controle de Concorrência

**Problema:** Transferências simultâneas podem causar race conditions e inconsistências.

**Solução:** Implementado **Pessimistic Locking** no método de transferência:

```java
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public void transfer(Long fromId, Long toId, BigDecimal amount) {
    // Adquire lock exclusivo nos registros
    Beneficio from = em.find(Beneficio.class, fromId, LockModeType.PESSIMISTIC_WRITE);
    Beneficio to = em.find(Beneficio.class, toId, LockModeType.PESSIMISTIC_WRITE);
    
    // Validações e transferência
    // ...
}
```

### 🎯 Separação de DTOs

**Por que?** Separar Request e Response DTOs oferece:
- ✅ Flexibilidade para adicionar campos sem quebrar contratos
- ✅ Validações específicas para entrada vs saída
- ✅ Segurança (não expor campos internos)

### 🔄 MapStruct vs Reflection

**Por que MapStruct?** 
- ✅ Performance superior (compilação vs runtime)
- ✅ Type-safe
- ✅ Erros detectados em tempo de compilação

### 🎨 Standalone Components (Angular)

**Por que?** Angular 18 recomenda standalone components:
- ✅ Menos boilerplate (sem NgModule)
- ✅ Lazy loading mais simples
- ✅ Bundle menor
- ✅ Futuro do Angular

### 📊 Signals vs Observables

**Por que Signals?** Usamos Signals para estado local:
- ✅ Mais simples que BehaviorSubject
- ✅ Change detection otimizada
- ✅ API mais intuitiva

---

## 🐛 Troubleshooting

### Backend não inicia

**Problema:** `ClassNotFoundException` ou `Port already in use`

**Solução:**
```bash
# Limpar e recompilar
cd backend-module
mvn clean install
mvn spring-boot:run
```

### Frontend não carrega dados

**Problema:** CORS ou backend não respondendo

**Solução:**
```bash
# 1. Verificar se backend está rodando
curl http://localhost:8080/api/v1/beneficios

# 2. Reiniciar backend se necessário
cd backend-module
mvn spring-boot:run

# 3. Hard reload no navegador
# Mac: Cmd+Shift+R
# Windows: Ctrl+Shift+F5
```

### PostgreSQL não conecta

**Problema:** `Connection refused`

**Solução:**
```bash
# Reiniciar container
docker-compose down
docker-compose up -d

# Verificar se está rodando
docker ps | grep bip-db
```

---

## 📊 Princípios Aplicados

### SOLID
- **S**RP - Single Responsibility Principle ✅
- **O**CP - Open/Closed Principle ✅
- **L**SP - Liskov Substitution Principle ✅
- **I**SP - Interface Segregation Principle ✅
- **D**IP - Dependency Inversion Principle ✅

### Clean Code
- ✅ Nomes descritivos e intencionais
- ✅ Funções pequenas e focadas
- ✅ DRY (Don't Repeat Yourself)
- ✅ Comentários apenas quando necessário
- ✅ Tratamento de erros consistente

### Best Practices
- ✅ DTOs para camada de apresentação
- ✅ Validações Jakarta Bean Validation
- ✅ Exception handling centralizado
- ✅ Logging estruturado
- ✅ Testes automatizados
- ✅ Documentação OpenAPI/Swagger

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👥 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📞 Contato

**Desenvolvedor:** Robert R Serra  
**Especialidade:** Java Fullstack Developer  
**Data:** Novembro 2025  
**Versão:** 1.0.0

---

## 🙏 Agradecimentos

- Spring Boot Team
- Angular Team
- PostgreSQL Community
- Bootstrap Team
- Todos os contribuidores de bibliotecas open-source utilizadas

---

## 📈 Próximos Passos

Possíveis melhorias futuras:
- [ ] Implementar paginação na listagem
- [ ] Adicionar autenticação e autorização (JWT)
- [ ] Implementar auditoria de operações
- [ ] Deploy em cloud (AWS/Azure/GCP)
- [ ] CI/CD com GitHub Actions
- [ ] Internacionalização (i18n)
- [ ] Testes E2E com Cypress

---

** -  Desenvolvido por Robert R Serra - Java Fullstack Developer**
