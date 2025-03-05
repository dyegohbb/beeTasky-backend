# BeeTasky

O **BeeTasky** é uma aplicação de gerenciamento de tarefas desenvolvida em Java com Spring Boot. Ele permite que os usuários criem, atualizem, listem e excluam tarefas, além de gerenciar suas contas de usuário. A aplicação também suporta internacionalização (i18n) para português (BR) e inglês (EN).

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.4.3**
- **Spring Data JPA**
- **Hibernate**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Postgres**
- **Maven**
- **Swagger** (documentação da API)
- **i18n** (internacionalização)

## Configuração do Projeto

### Pré-requisitos

- Java 21 instalado.
- Maven instalado.
- IDE de sua preferência (IntelliJ, Eclipse, etc.).

### Clonando o Repositório

```bash
git clonegit@github.com:seu-usuario/beeTasky-backend.git
cd beeTasky-backend
``` 

### Configurando o Banco de Dados

```
spring.datasource.url=${BEE_TASKY_DATASOURCE_URL:jdbc:postgresql://localhost:5432/bee_tasky}
spring.datasource.driverClassName=${BEE_TASKY_DATASOURCE_DRIVER:org.postgresql.Driver}
spring.datasource.username=${BEE_TASKY_DATASOURCE_USER}
spring.datasource.password=${BEE_TASKY_DATASOURCE_PASS}
spring.jpa.show-sql=${BEE_TASKY_JPA_SHOW_SQL:false}
spring.jpa.hibernate.ddl-auto=${BEE_TASKY_HIBERNATE_DDL_AUTO:none}
```

- `BEE_TASKY_DATASOURCE_URL`: URL do banco de dados PostgreSQL.
- `BEE_TASKY_DATASOURCE_DRIVER`: Classe do driver JDBC para o banco de dados. No caso do PostgreSQL, é org.postgresql.Driver.
- `BEE_TASKY_DATASOURCE_USER`: Nome de usuário para autenticação no banco de dados.
- `BEE_TASKY_DATASOURCE_PASS`: Senha do usuário para autenticação no banco de dados.
- `BEE_TASKY_JPA_SHOW_SQL`: Define se o SQL gerado pelo JPA deve ser mostrado nos logs.
- `BEE_TASKY_HIBERNATE_DDL_AUTO`: Configuração do Hibernate para a criação do banco. none significa que não há modificações automáticas no esquema do banco e que é necessário rodar os scripts de criação manualmente

### Outras configurações

```
jwt.secret=${BEE_TASKY_JWT_SECRET:!b33T4skyS3cr3t@2025!}
jwt.expiration=${BEE_TASKY_JWT_EXPIRATION:1800000}
```

- `BEE_TASKY_JWT_SECRET`: Chave secreta usada para assinar e verificar tokens JWT.
- `BEE_TASKY_JWT_EXPIRATION`: Tempo de expiração do token JWT, em milissegundos. (valor padrão 30min)

## Endpoints da API
A API do BeeTasky segue o padrão RESTful e está documentada com Swagger. Acesse a documentação em: http://localhost:8080/swagger-ui.html.
Também está disponibilizado em código a coleção do Postman para facilitar as consultas, PATH: `/resources/api`

## Internationalização
A API do BeeTasky está configurada para responder em dois idiomas (pt-br e en) como padrão o inglês, mapeado com o header `accept-language` das requisições, para brasileiro: `pt-br` para inglês: `en` ou qualquer outro valor

## Migrations
Os scripts de criação estão disponibilizados também nos resources, PATH: `/resources/db/migrations`, é estritamente necessário rodar na ordem que está numerado, ou então rodar o arquivo de sqls combinados (escolher entre rodar as tabelas separadas `1-users.sql` e depois `2-tasks.sql` ou rodar apenas o arquivo combinado `combined_migrations.sql` que já contem tudo que é necessário)
