
<div align="center">

--- 

### Marcos Araújo

*Engenharia de Software & Desenvolvimento Full-Stack*

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/marcos-araujo-517201212/)
[![WhatsApp](https://img.shields.io/badge/WhatsApp-25D366?style=for-the-badge&logo=whatsapp&logoColor=white)](https://wa.me/5511940292792)

</div>

## Blog & News Platform — Admin Panel API

O Admin Panel API é a aplicação backend desenvolvida em Java com Spring Boot 3 responsável pelo gerenciamento de dados, controle de publicações, cálculo de métricas e persistência da plataforma de notícias e notas tech (marcosaraujo.dev), essa plataforma foi desenvolvida sob a ideia de ser gerenciada a nivel localhost mesmo, faz o inicio da aplicação, publica gerencia totalmente isolada.

## Sumário

* [Visão Geral](#visão-geral)
* [Funcionalidades Principais](#funcionalidades-principais)
* [Tecnologias Utilizadas](#tecnologias-utilizadas)
* [Arquitetura do Projeto](#arquitetura-do-projeto)
* [Como Executar o Projeto](#como-executar-o-projeto)
* [Pré-requisitos](#pré-requisitos)
* [Variáveis de Ambiente](#variáveis-de-ambiente)
* [Passo a Passo](#passo-a-passo)

## Visão Geral

Esta API provê a camada de inteligência de negócios e acesso aos dados para o portal web. Desenvolvida seguindo boas práticas de arquitetura REST, a aplicação cuida do ciclo de vida dos posts, formatação de conteúdo, cálculo automático de tempo estimado de leitura e integração segura com o banco de dados nuvem via Supabase.

## Funcionalidades Principais

* CRUD de Artigos/Posts: Criação, edição, listagem e remoção de conteúdos.

* Geração Automática de Slugs: URLs amigáveis e otimizadas para SEO.

* Cálculo de Tempo de Leitura: Algoritmo que calcula os minutos de leitura com base no volume de palavras do post.

* Segurança e Isolamento de Variáveis: Configurações sensíveis totalmente abstraídas via variáveis de ambiente.

* Persistência Relacional: Modelagem de dados com JPA/Hibernate integrada ao PostgreSQL/Supabase.

* Tecnologias Utilizadas
<br>

### Linguagem: Java 17+



* Framework: Spring Boot 3.x

* Módulos Spring:

* Spring Web (REST)

* Spring Data JPA (Persistência)

* Spring Validation (Validação de DTOs)

* Banco de Dados: PostgreSQL / Supabase

* Gerenciador de Dependências: Maven

## Utilitários: Lombok, Jakarta Persistence

### Arquitetura do Projeto

O projeto segue uma estrutura em camadas limpa e bem definida (Layered Architecture):

```src/main/java/br/com/marcosaraujo/
├── config/          # Configurações de CORS, Segurança e Beans Globais
├── controller/      # Endpoints REST e Controllers MVC
├── dto/             # Data Transfer Objects (Payloads de Request/Response)
├── model/           # Entidades JPA (Mapeamento ORM)
├── repository/      # Interfaces Spring Data JPA
├── service/         # Regras de Negócio e Casos de Uso
└── utils/           # Helper classes (Gerador de Slug, Calculador de Leitura)
```


## Como Executar o Projeto

Pré-requisitos

* Java 17 ou superior instalado

* Maven 3.8+ instalado

* Conta no Supabase (ou instância do PostgreSQL local)

* Variáveis de Ambiente

Crie um arquivo .env na raiz do projeto (use o arquivo .env.example como referência):

```
DATABASE_URL=jdbc:postgresql://seu-supabase-host:5432/postgres
DATABASE_USERNAME=seu_usuario
DATABASE_PASSWORD=sua_senha
SPRING_PROFILES_ACTIVE=dev
```

### Passo a Passo

#### Clone o repositório:

```
git clone https://github.com/marcosaraujodev083-tech/NOME-DO-REPOSITORIO.git
cd NOME-DO-REPOSITORIO
```


#### Instale as dependências:

```
mvn clean install
```

#### Execute a aplicação:

```
mvn spring-boot:run
```

A API estará rodando por padrão em:
```
http://localhost:8080.
```

## Roadmap de Funcionalidades

### Autenticação & Painel Administrativo (Core)
- [x] CRUD completo de artigos (Criar, Editar, Listar e Deletar)
- [x] Integração com Supabase (PostgreSQL) para persistência de dados
- [x] Suporte a Markdown / HTML para o conteúdo dos posts
- [ ] Implementação de **Tema Escuro (Dark Mode)** no painel e no blog
- [ ] Painel Dashboard com estatísticas básicas (total de posts, views, assinantes)

### Módulo de Newsletter & Comunicação
- [ ] Cadastro e gerenciamento de inscritos da newsletter
- [ ] Criação e rascunho de e-mails para envio em massa
- [ ] **Integração com API Externa de Disparo** (Orquestração de microsserviço/worker para envio via Mailgun, SendGrid ou Resend)
- [ ] Histórico de campanhas e e-mails disparados

### Gerenciamento de Agenda, Contatos & Mensagens
- [ ] Agenda centralizada de contatos (nome, e-mail, notas)
- [ ] Caixa de entrada interna para mensagens recebidas pelo formulário `/contato`
- [ ] Status de atendimento para cada mensagem (Pendente, Respondido, Arquivado)
- [ ] Envio de respostas diretas para contatos via e-mail direto do painel

### Funcionalidades Aconselhadas para o Futuro 
- [ ] **Gerenciador de Mídia / Upload de Imagens:** Upload direto de imagens de capa para o Supabase Storage.
- [ ] **Exportação de Dados:** Permitir exportar contatos e inscritos em arquivo CSV/JSON.
- [ ] **SEO & OpenGraph Dynamic:** Gerador automático de meta tags para redes sociais a partir do título do post.
- [ ] **API Worker/Bot em Python** (Coletor), monitora e traz possiveis temas em alta para dentro do painel admin.