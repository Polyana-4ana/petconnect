# 🐾 PetConnect

Sistema de gerenciamento de adoção de cães e gatos desenvolvido para a disciplina de Implementação de Software.

O projeto tem como objetivo facilitar o processo de adoção de pets, permitindo o gerenciamento de animais, adotantes e adoções de forma organizada e segura.

---

# 🚀 Tecnologias Utilizadas

## Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven

## Banco de Dados
- PostgreSQL

## Ferramentas
- Git
- GitHub
- Postman
- Trello

---

# 🏗 Arquitetura

O projeto segue o padrão:

MVC + Service Layer

Estrutura principal:

src/main/java/com/petconnect
├── controller
├── service
├── repository
├── entity
├── dto
├── exception
├── config

---

# 📌 Funcionalidades

## Pets
- Cadastro de pets
- Atualização de pets
- Exclusão de pets
- Listagem de pets

## Adotantes
- Cadastro de adotantes
- Consulta de adotantes

## Adoções
- Processo de adoção
- Controle de pets adotados
- Validações de negócio

## Relatórios
- Pets disponíveis
- Pets adotados
- Histórico de adoções

---

# 📋 Regras de Negócio

- Um pet não pode ser adotado duas vezes
- O adotante deve estar cadastrado
- Pets adotados não aparecem como disponíveis
- Campos obrigatórios devem ser validados

---

# 🔥 Tratamento de Exceções

A API possui tratamento global de exceções utilizando:

- @RestControllerAdvice
- ResponseEntity
- Status HTTP padronizados

Exemplo de resposta:

```json
{
  "timestamp": "2026-05-18T22:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Pet já adotado",
  "path": "/adocoes"
}
```

---

# ⚙ Como Executar

## 1. Clonar repositório

```bash
git clone URL_DO_REPOSITORIO
```

## 2. Configurar banco PostgreSQL

Criar banco:

```sql
CREATE DATABASE petconnect;
```

---

## 3. Configurar application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/petconnect
spring.datasource.username=postgres
spring.datasource.password=senha
```

---

## 4. Executar aplicação

```bash
mvn spring-boot:run
```

---

# 📡 Endpoints Principais

## Pets

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /pets | Listar pets |
| POST | /pets | Cadastrar pet |
| PUT | /pets/{id} | Atualizar pet |
| DELETE | /pets/{id} | Remover pet |

---

# 🌱 GitFlow

Estratégia utilizada:

- main
- develop
- feature/*

Fluxo de trabalho:

feature → develop → main

---

# 👥 Equipe

- Polyana Santos
- Arthur Rotthen
- Nathalia Martins
- Amanda Matos
- Arthur Auadi
- 
---

# 📌 Status do Projeto

🚧 Em desenvolvimento
