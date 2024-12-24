
# FOROHUB

![Badge](https://raw.githubusercontent.com/Qleoz12/Java-ForoHub-Oracle-Alura/refs/heads/main/341230127-952e8461-2eac-4c28-8fd0-bb676e672528.png)

Basic API for managing a forum using SpringBoot. This project is part of the challenges proposed in [#AluraLatam](https://www.aluracursos.com/) with [#OracleNexEducation](https://app.aluracursos.com/form-one/registro/latam-general), as part of the **Java and Spring Boot G7 - ONE** course.

# ✨ Features

- 📋 Environment variables with [Spring Dotenv](https://github.com/paulschwarz/spring-dotenv).
- 🚀 Implementation of authentication and authorization using JWT tokens.
- 🔎 API documentation with Swagger and springdoc.
- 📚 Use of Spring JPA, derived queries, and jpql.
- 📦 Dependency management with Maven.
- 🛠️ Database on MariaDB.
- 📁 Database management as a service using [Docker](https://www.docker.com/).
- 💪 Automation for managing the Docker database container using [GNU Make](https://www.makigas.es/series/make#:~:text=Make%20es%20una%20utilidad%20del,%2C%20CMake...).

# Technologies Used
- Java 17
- Spring Boot 3.3.1
- Hibernate
- MySQL
- Maven

### 1. Initialize the project

- Clone the repository
  
```bash
  git clone https://github.com/Qleoz12/Java-ForoHub-Oracle-Alura.git
```
### 2. Configure the database
You have two options:
* The first is to use an [external database](#external-database).
* The second is using a [Docker](#using-docker) container for MariaDB (Requirement: having [Docker](https://www.docker.com/) and GNU Make installed).

#### External database
- You just need to edit the necessary data in the .env file to connect to your database
  
dotenv
  DB_HOST=localhost
  DB_PORT=6602 # Default port for MariaDB
  DB_USER=admin
  DB_DATABASE=forohub_db
  DB_PASSWORD=12345678
  DEBUG=true
  JWT_SECRET=your_secret

#### Using Docker
- Copy the .docker/.env.dist file to .docker/.env
  
```bash
  cp .docker/.env.dist .docker/.env
```
- Edit the .docker/.env file (or leave it with the default values)
  
dotenv
  APP_NAME=forohub
  
# Dev MySQL Settings
  MYSQL_DATABASE=forohub_db
  MYSQL_USER=admin
  MYSQL_PASSWORD=12345678
  MYSQL_ROOT_PASSWORD=12345678

- Edit the .env file according to the data in .docker/.env
  
dotenv
  DB_HOST=localhost
  DB_PORT=6602
  DB_USER=admin
  DB_DATABASE=forohub_db
  DB_PASSWORD=12345678
  DEBUG=true
  JWT_SECRET=your_secret

#### Docker commands

- Run the database container
 ```bash
  make run params=-d
```
- Stop the database container
```bash
  make stop
```
- Remove the database container
```bash
  make down
```
- Restart the database container
```bash
  make restart
```
### 3. Run the application.
- This can be done with your IDE (Intellij Idea or another). The project is already configured for Spring Boot.
## Command line maven
if you have maven installed, you can run the following command in the terminal:
```bash
mvn spring-boot:run
```

### 4. Test the API.

1. To test all the endpoints, you first need to [register a user](http://localhost:8080/swagger-ui/index.html#/user-controller/create).
2. [Obtain the token](http://localhost:8080/swagger-ui/index.html#/auth-controller/authUser) for the newly registered user (the token expires every 2 hours).
3. Paste the token in the "Value" text box by clicking the "authorize" button with the green border at the [beginning of the API documentation](http://localhost:8080/swagger-ui/index.html).
4. Finally, you can test all the functionalities of the API by accessing the [API documentation generated](http://localhost:8080/swagger-ui/index.html) by Springdoc.

# Basic Features

1. [Authentication and authorization](http://localhost:8080/swagger-ui/index.html#/auth-controller) via Json Web Tokens.
2. [User creation](http://localhost:8080/swagger-ui/index.html#/user-controller/create) (users).
3. [Course creation](http://localhost:8080/swagger-ui/index.html#/subject-controller/create_2) (subjects).
4. Full [CRUD](http://localhost:8080/swagger-ui/index.html#/topic-controller) for managing topics.
5. [Create answers](http://localhost:8080/swagger-ui/index.html#/answer-controller/create_3) for registered topics.

Thank you for visiting this repository. (@OscarDevC)
