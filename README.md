## Technical stack: Java 17, Spring Boot 3(+ Web, Data JPA, Data Redis, Validation, AOP, Security with Bearer JWT token, Cloud Config, Cloud OpenFeign, Test, Test-Security), OpenAPI 3(Swagger), PostgreSQL, Redis, Liquibase, Lombok, Mapstruct, TestContainers, custom Spring Boot Starters for exception handling and logging, Docker-compose

# Functionality:

- ### you have CRUD operations for News and Comments(you should load Comments to the News separately)
- ### add, update and delete operations are secured by Spring Security
- ### CRUD operations for Comments are cached (Redis or custom cache depends on profile)

# In order to configure cache you need:

- ### set up cache.algorithm(only LRU or LFU possible) property in application.yml
- ### set up cache.size(positive number) property in application.yml
- ### cache will not be created in case of absence of described properties

# Before running the application, you should know:

- ### there 2 databases with 5432 and 5433 ports(for news and comments AND users)
- ### there are 6 news in DB (jdbc:postgresql://localhost:5432/news)
- ### there are 200 comments in DB (jdbc:postgresql://localhost:5432/news)
- ### there are 6 users in DB (jdbc:postgresql://localhost:5433/users)
- ### add and update operations with news and comments are validated

## Configurations for core and security modules are received from config-server

## Security tips:

- ### all users have 261101 password
- ### logins: dobrowydka - admin, yaros1337 - subscriber, over228 - journalist
- ### endpoints for authorization: localhost:8081/auth/register and localhost:8081/auth/login

## In order to build the jar you should run the following command:

> ./gradlew clean build -x test

## In order to run application you should run the following command(before this you should build the jar):

> docker-compose up

## To run tests:

> ./gradlew test

## You are able to see and test all endpoints on [swagger-ui for core module](http://localhost:8080/swagger-ui) or [swagger-ui for security module](http://localhost:8081/swagger-ui) when you RUN the application