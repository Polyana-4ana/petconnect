# 🏛️ Documentação de Arquitetura e Engenharia - PetConnect

Este documento descreve os padrões arquiteturais, a topologia de infraestrutura, os fluxos de dados e os mecanismos de resiliência adotados no desenvolvimento do projeto **PetConnect**.

---

## 🏗️ 1. Padrão Arquitetural e Organização de Pastas

A aplicação foi desenvolvida sobre o ecossistema **Java com Spring Boot**, adotando o modelo de **Arquitetura em Camadas (Layered Architecture)** combinado com boas práticas do **DDD (Domain-Driven Design)** e os princípios do **SOLID**.

O sistema prioriza a alta coesão e o baixo acoplamento através da seguinte distribuição física de componentes:
- **`config`**: Configurações transversais de infraestrutura (segurança com Spring Security e documentação OpenAPI).
- **`controller`**: Camada de exposição da API HTTP, responsável pelo roteamento e validação inicial das requisições.
- **`dto`**: Objetos de transferência de dados organizados em subpastas por domínio de negócio (`adocao`, `adotante`, `pet`), blindando as entidades internas.
- **`entity`**: Classes do modelo rico de domínio mapeadas via JPA/Hibernate e enumeradores (`enums/StatusPet`).
- **`repository`**: Abstração da camada de persistência com suporte a consultas dinâmicas (`PetSpecification`).
- **`exception`**: Centralização e tratamento resiliente de falhas do sistema.

![Diagrama de Estrutura de Camadas](docs/assets/diagrama-pastas.jpeg)

---

## 🔄 2. Ciclo de Vida de uma Requisição (Diagrama de Sequência)

Para ilustrar a dinâmica síncrona entre os componentes e os pacotes da aplicação, o fluxo abaixo detalha o ciclo de vida completo de uma requisição de negócio (Criação de uma Adoção):

![Diagrama de Sequência de Adoção](docs/assets/diagrama-sequencia.jpeg)

### 📌 Destaques do Fluxo:
1. Interceptação primária pelo `SecurityConfig` para validação de credenciais.
2. Desserialização e validação sintática isolada na camada de `dto`.
3. Injeção de dependência por interface (`AdocaoService`), respeitando o princípio da inversão de dependência do SOLID.
4. Mapeamento relacional e persistência de dados isolada via `AdocaoRepository`.

---

## 🛡️ 3. Resiliência e Tratamento Global de Exceções

O projeto foi arquitetado para ser tolerante a falhas de negócio sem expor a infraestrutura ou interromper a execução do servidor.

![Diagrama de Fluxo de Resiliência](docs/assets/fluxo-resiliencia.jpeg)

### 🧩 Mecanismo de Defesa da API:
- **Exceções Customizadas:** Classes como `PetNaoEncontradoException` ou `AdocanteNaoEncontradoException` representam falhas previsíveis de negócio.
- **Interpretação Centralizada:** O `GlobalExceptionHandler` intercepta essas exceções de forma transparente via `@ControllerAdvice`.
- **Contrato de Erro Seguro (`ErrorResponse`):** Transforma falhas internas em um JSON padronizado com código HTTP 404 (Not Found), garantindo segurança da informação e uma boa integração com o frontend.

```json
{
  "timestamp": "2026-06-10T17:45:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "O pet com o ID informado não foi encontrado no sistema.",
  "path": "/api/pets/99"
}
```

---

## 🔑 4. Controle de Acesso e Matriz de Permissões

A segurança nas rotas expostas pela aplicação é segmentada dinamicamente através do Spring Security com base em perfis de acesso (*Roles*):

![Diagrama de Matriz de Permissões](docs/assets/controle-acesso.jpeg)

- **Visitante / Adotante (`ROLE_USER`):** Permissão restrita a operações de consulta e submissão do próprio cadastro/solicitação.
- **Admin / ONG (`ROLE_ADMIN`):** Acesso total para mutação de dados sensíveis, controle do ciclo de vida das adoções e gerenciamento de inventário de pets.

---
