# Система управления задачами (Task Management System)

## Описание проекта
Данный проект представляет собой веб-приложение для управления задачами и комментариями к ним с поддержкой регистрации и аутентификации пользователей. Система построена на основе Spring Boot и использует PostgreSQL в качестве основной базы данных, а также H2 для тестирования.

## Основные технологии

- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Data JPA**
- **Spring Security с JWT-аутентификацией**
- **MapStruct** для преобразования объектов
- **Lombok** для упрощения написания кода
- **Liquibase** для миграций базы данных
- **Swagger/OpenAPI** для документации API
- **JUnit и Testcontainers** для тестирования

## Архитектура приложения

### Слои приложения:
- **Controller** – REST-контроллеры, обрабатывающие HTTP-запросы.
- **Service (Facade)** – слой бизнес-логики и управления транзакциями.
- **Repository** – работа с базой данных через Spring Data JPA.
- **Security** – управление аутентификацией и авторизацией пользователей с помощью JWT.
- **Exception** – обработка исключений с кастомными сообщениями и статусами HTTP.

### Сущности:
- **User** – пользователь системы (роль: Admin/User).
- **Task** – задача с автором, исполнителем, статусом и приоритетом.
- **Comment** – комментарий к задаче с автором.

## Запуск приложения

### Через Docker Compose:
```bash
docker-compose up -d
```

### Сборка и запуск через Dockerfile:
```bash
docker build -t task-manager-app .
docker run -p 8080:8080 task-manager-app
```

## CI/CD
Настроена CI/CD с использованием GitHub Actions:
- Автоматическая сборка проекта
- Запуск unit-тестов
- Создание и загрузка артефакта (JAR-файла)

## API-документация

Документация API доступна через Swagger по адресу:

```
http://localhost:8080/task-manager/swagger-ui.html
```

### Работа с API:
1. **Регистрация**: Создайте пользователя через эндпоинт `/auth/register` или используйте админскую учётную запись, которая автоматически создается при первом запуске приложения.
2. **Аутентификация**: Выполните вход, используя эндпоинт `/auth/login`, чтобы получить JWT-токен.
3. В верхнем правом углу интерфейса Swagger нажмите кнопку **Authorize** и вставьте полученный JWT-токен.

### Роли пользователей

- **Администратор (`ROLE_ADMIN`)** может:
    - Создавать, редактировать и удалять задачи
    - Управлять комментариями (создание и удаление любых комментариев)
    - Управлять пользователями

- **Пользователь (`ROLE_USER`)** может:
    - Просматривать задачи и комментарии
    - Создавать комментарии
    - Удалять только собственные комментарии

## Основные эндпоинты

### AuthController (`/auth`)
- **POST** `/register` – Регистрация пользователя.
- **POST** `/login` – Вход пользователя в систему и получение JWT-токена.

### TaskController (`/tasks`)
- **GET** `/tasks-list` – Получение списка задач с пагинацией.
- **GET** `/task/{id}` – Получение задачи по ID.
- **POST** `/create-task` – Создание новой задачи (только для Admin).
- **PUT** `/update-task/{id}` – Обновление задачи (Admin или автор задачи).
- **DELETE** `/delete-task/{id}` – Удаление задачи (Admin или автор задачи).
- **POST** `/filter` – Получение задач с фильтрацией и пагинацией.

### CommentController (`/comments`)
- **GET** `/task/{taskId}` – Получение комментариев к задаче.
- **POST** `/create-comment` – Создание комментария к задаче.
- **DELETE** `/delete-comment/{id}` – Удаление комментария (Admin или автор комментария).

### UserController (`/users`)
- **GET** `/user/{id}` – Получение информации о пользователе по ID (только Admin).
- **GET** `/users-list` – Получение списка пользователей (только Admin).
- **POST** `/create-user` – Создание нового пользователя (только Admin).

## Тестирование

Проект использует H2 и Testcontainers для интеграционного тестирования. Настройки тестирования находятся в отдельном файле конфигурации `application-test.yaml` в папке `test/resources`.

## Структура базы данных
Используется PostgreSQL. Миграции организованы через Liquibase:
- Таблицы: `users`, `tasks`, `comments`
- Задачи связаны с авторами и исполнителями
- Комментарии связаны с задачами и авторами
- Созданы индексы для оптимизации запросов

## Конфигурация безопасности
Используется JWT-токен для аутентификации и авторизации. Пароли хранятся в зашифрованном виде с использованием BCrypt.

## Первоначальная инициализация
При первом запуске создается пользователь-администратор с параметрами из конфигурации приложения (`application.yaml`).
