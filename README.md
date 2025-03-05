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
  
## Migrations
Os scripts de criação estão disponibilizados também nos resources, PATH: `/resources/db/migrations`, é estritamente necessário rodar na ordem que está numerado, ou então rodar o arquivo de sqls combinados (escolher entre rodar as tabelas separadas `1-users.sql` e depois `2-tasks.sql` ou rodar apenas o arquivo combinado `combined_migrations.sql` que já contem tudo que é necessário)

## Internationalização
A API do BeeTasky está configurada para responder em dois idiomas (pt-br e en) como padrão o inglês, mapeado com o header `accept-language` das requisições, para brasileiro: `pt-br` para inglês: `en` ou qualquer outro valor

## Endpoints da API
A API do BeeTasky segue o padrão RESTful e está documentada com Swagger. Acesse a documentação em: http://localhost:8080/swagger-ui.html.
Também está disponibilizado em código a coleção do Postman para facilitar as consultas, PATH: `/resources/api`

### Listagem de cURLs disponíveis para uso:

#### POST /user: Criação de usuário

```bash
curl --location 'http://127.0.0.1:8080/user' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "username",
    "password": "password",
    "email":  "username@email.com"
}'
```

#### PUT /user: Atualização de usuário

```bash
curl --location --request PUT 'http://127.0.0.1:8080/user/{identifier}' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer ****' \
--data-raw '{
    "username": "username",
    "password": "password",
    "currentPassword": "new-password",
    "email":  "username@email.com"
}'
```

#### DELETE /user: Deleção de usuário
A Deleção do usuário é lógica

```bash
curl --location --globoff --request DELETE 'http://127.0.0.1:8080/user/{{identifier}}' \
--header 'Authorization: Bearer ****' \
--data ''
```

#### POST /auth: autenticação
O atributo "login" pode ser tanto username quanto o e-mail do usuário

```bash
curl --location 'http://127.0.0.1:8080/auth' \
--header 'Accept-Language: pt-br' \
--header 'Content-Type: application/json' \
--data '{
    "login": "ana.silva",
    "password": "password"
}'
```

#### POST /tasks: Criação de task

```bash
curl --location 'http://127.0.0.1:8080/tasks' \
--header 'Accept-Language: pt-br' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer ****' \
--data '{
    "title": "Criação de novo endpoint",
    "description": "Criação de novo endpoint para atualização de valores",
    "status": "COMPLETED",
    "deadline": "2025-05-30T23:59:59"
}'
```

#### PUT /tasks: Atualização de task

```bash
curl --location --globoff --request PUT 'http://127.0.0.1:8080/tasks/{{identifier}}' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer ****' \
--data '{
    "title": "Criação de novo endpoint - urgente",
    "description": "Criação de novo endpoint para atualização de valores",
    "status": "PENDING",
    "deadline": "2025-04-12T23:59:59"
}'
```

#### DELETE /task: Deleção de task
A Deleção da task é lógica

```bash
curl --location --globoff --request DELETE 'http://127.0.0.1:8080/tasks/{{identifier}}' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer ****'
```

#### GET /task: Busca de task por usuário, com paginação, ordenação e filtros
O cURL abaixo mostra a request com a maior quantidade de parametros possíveis, cada um deles é opcional fazendo com que a consulta de tasks se torne o mais dinamica possível.
* Parametros de filtros: title, status, createdOnStartDate, createdOnEndDate, deadlineStartDate, deadlineEndDate
* A createdOn e deadlineOn possuem dois atributos cada, indicando o começo e o fim do periodo de busca, caso insira apenas o startDate ele vai pegar as tasks a partir daquela data ao maior infinito, se inserir apenas o endDate, vai pegar as tasks do menor infinito até o endDate, e caso passe ambos, vai buscar as tasks daquele periodo.
* Há validação para startDate não ser posterior ao endDate de ambos os atributos (deadline e createdOn)
* A ordenação é feita pelo Pageable, definindo a coluna e a orientação (asc,desc) como no exemplo temos a ordenação `title,asc` assim, novamente, deixando a consulta o mais dinamica possível.

```bash
curl --location 'http://127.0.0.1:8080/tasks?taskIdentifier=TASK123&title=Minha%20Tarefa&status=IN_PROGRESS&createdOnStartDate=2024-03-01T00%3A00%3A00&createdOnEndDate=2024-03-05T23%3A59%3A59&deadlineStartDate=2024-03-10T00%3A00%3A00&deadlineEndDate=2024-03-15T23%3A59%3A59&page=0&size=10&sort=title%2Casc' \
--header 'Authorization: Bearer ****'
```
