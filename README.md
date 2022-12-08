# Приложение Explore With Me

[PR](https://github.com/Dmitrii51/java-explore-with-me/pull/1#issue-1439368369)

Explore With Me — приложение, которое дает возможность делиться информацией об интересных событиях 
и помогает найти компанию для участия в них.

## Используемые технологии

* Spring Boot
* Maven
* Lombok
* JPA
* PostgreSQL
* Docker

## Краткое описание сервисов

Проект состоит из двух микросервисов:

1. Основной сервис — содержит всё необходимое для работы продукта.  
   Запускается на 8080 порту.
   [API-спецификация основного сервиса](https://github.com/Dmitrii51/java-explore-with-me/blob/main/ewm-main-service-spec.json)

2. Сервис статистики — хранит количество просмотров и позволяет делать различные
   выборки для анализа работы приложения. Запускается на 9090 порту.
   [API-спецификация сервиса статистики](https://github.com/Dmitrii51/java-explore-with-me/blob/main/ewm-stats-service-spec.json)

Каждый из сервисов работает со своей базой данных postgres.
Для просмотра спецификаций используйте [Swagger](https://editor-next.swagger.io/).


## Запуск приложения

Для запуска проекта потребуется наличие на компьютере Docker.
Запуск приложения осуществляется следующей командой:

```Bash
mvn install
docker-compose up
```

