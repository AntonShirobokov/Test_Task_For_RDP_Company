# Inventory Reservation Service

## Клонирование репозитория
```bash
git clone https://github.com/AntonShirobokov/Test_Task_For_RDP_Company
cd Test_Task_For_RDP_Company
```

Все последующие команды выполняются в корне проекта.

## Запуск через Docker

**Требования:** Docker и Docker Compose должны быть установлены.
```bash
docker compose up --build
```

Приложение будет доступно по адресу: `http://localhost:8080`

Остановить приложение:
```bash
docker compose down
```

## Запуск тестов

**Требования:** Docker должен быть запущен — PostgreSQL поднимается автоматически через Testcontainers.

MacOS / Linux:
```bash
./mvnw test
```

Windows:
```cmd
mvnw.cmd test
```
