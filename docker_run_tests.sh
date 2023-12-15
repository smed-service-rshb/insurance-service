#!/bin/bash

# Запускаем контейнер интеграционных тестов (с ожидание ответа от ключевых систем), результат выполнения интеграционных тестов будет сохранен в директорию билда
docker-compose -f docker-compose.test.yml -f docker-compose.integration-tests.yml run -u $(id -u) integration-test \
    ../wait-for-it.sh insurance-service-app:8080 -s -t 100 -- \
    mvn verify