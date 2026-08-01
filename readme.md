
<div align="center">

--- 

### Marcos Araújo

*Engenharia de Software & Desenvolvimento Full-Stack*

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/marcos-araujo-517201212/)
[![WhatsApp](https://img.shields.io/badge/WhatsApp-25D366?style=for-the-badge&logo=whatsapp&logoColor=white)](https://wa.me/5511940292792)

</div>

## Blog & News Platform — Admin Panel API

O Admin Panel API é a aplicação backend desenvolvida em Java com Spring Boot 3 responsável pelo gerenciamento de dados, controle de publicações, cálculo de métricas e persistência da plataforma de notícias e notas tech (marcosaraujo.dev).

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
