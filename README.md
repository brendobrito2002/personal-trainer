# 🏋️‍♂️ Personal Trainer API - Sistema de Gestão para Personal Trainers

Projeto desenvolvido para a disciplina de **Programação Web**  
Universidade Federal do Agreste de Pernambuco (UFAPE)

---
## 1. Visão Geral do Produto

Esta API RESTful foi desenvolvida como um backend completo para um sistema de gestão voltado a **personal trainers** e seus alunos. O objetivo principal é oferecer uma solução digital moderna que permita o acompanhamento remoto ou presencial de treinos, avaliações físicas, controle financeiro e comunicação direta entre personal e aluno, abrangendo funcionalidades como criação de planos de treino personalizados com itens detalhados, registro de métricas corporais, gerenciamento de faturas, catálogo de exercícios categorizados por grupo muscular e chat individual com histórico de mensagens. O sistema foi construído com três perfis de usuário (Administrador, Personal Trainer e Aluno), garantindo segurança granular por roles e verificação de propriedade dos recursos, além de validações robustas, tratamento consistente de exceções.

---
## 2. Tecnologias Utilizadas

### 2.1 Stack Tecnológica
- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.2.5
- **Persistência:** Spring Data JPA + Hibernate
- **Banco de Dados:** PostgreSQL (principal) / H2 (testes)
- **Segurança:** Spring Security + BCryptPasswordEncoder
- **Validação:** Bean Validation (Jakarta Validation)
- **Documentação API:** SpringDoc OpenAPI (Swagger UI)
- **Redução de código:** Lombok + Records (DTOs imutáveis)
- **Inicialização de dados:** `data.sql` + CommandLineRunner (admin padrão)

---
## 3. Atores do Sistema

| Ator                | Descrição                                                                 | Permissões Chave                                                                 |
|---------------------|---------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| **Administrador**   | Usuário com acesso total ao sistema                                       | Visualizar, criar, editar e excluir qualquer recurso de qualquer usuário         |
| **Personal Trainer**| Profissional responsável pelo acompanhamento dos alunos                   | Gerenciar apenas seus próprios alunos: planos de treino, itens, avaliações, faturas, chat e exercícios |
| **Aluno**           | Cliente que contrata o personal trainer                                   | Visualizar apenas seus próprios dados: planos, itens de treino, avaliações, faturas e chat com seu personal |

**Credenciais para teste (após executar com data.sql):**
- Admin: `admin@admin.com` / `admin123`
- Personais e Alunos: `seuemail@email.com` / `123456`

---
## 4. Funcionalidades por Módulo

### 4.1 Módulo de Usuários
- Cadastro aberto de alunos e personais (sem autenticação)
- Vinculação de aluno a um personal (ativa a conta do aluno)
- Desvinculação de aluno (desativa a conta)
- Controle de acesso por role + verificação de propriedade

### 4.2 Módulo de Exercícios e Grupos Musculares
- Cadastro de grupos musculares (Peito, Costas, etc.)
- Catálogo de exercícios com descrição e associação a grupo muscular
- Busca por nome ou grupo

### 4.3 Módulo de Planos de Treino
- Criação de planos com nome, duração (semanas), data início/fim e status ativo
- Limite de apenas **um plano ativo** por aluno
- Adição de itens de treino (exercício, séries, repetições, carga, descanso)
- Visualização restrita ao dono ou seu personal

### 4.4 Módulo de Avaliações Físicas
- Registro de métricas (peso, altura, % gordura, observações, foto)
- Indicação se foi realizada pelo personal ou pelo aluno (online)
- Histórico por aluno

### 4.5 Módulo de Faturas
- Controle de cobranças com valor, vencimento, status (PENDENTE/PAGA/CANCELADA)
- Limite de apenas **uma fatura pendente** por aluno
- Visualização por aluno ou personal

### 4.6 Módulo de Chat
- Chat individual entre personal e aluno (criado automaticamente ao vincular)
- Envio de mensagens com timestamp, marcação de lida e suporte a multimídia (caminho de imagem/vídeo)
- Histórico ordenado

### 4.7 Módulo de Segurança e Validação
- Autenticação via HttpBasic (em desenvolvimento para JWT)
- Autorização granular (roles + verificação de dono do recurso)
- Validações completas com mensagens em português
- Tratamento global de exceções com respostas padronizadas

---
## 5. Guia de Execução da API

### 5.1 Pré-requisitos
- Java 21
- Maven
- PostgreSQL rodando

### 5.2 Configuração do Banco de Dados
- Banco: `personal_trainer_db`
- Usuário: `postgres`
- Senha: `personaltrainer` (conforme application.properties)

### 5.3 Como Executar
```bash
git clone https://github.com/seu-usuario/personal-trainer.git
mvn spring-boot:run