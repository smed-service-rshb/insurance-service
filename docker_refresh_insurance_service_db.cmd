call docker-compose stop insurance-service-db
call docker-compose rm --force insurance-service-db
call docker image rm --force docker.dos.softlab.ru/smedservice/insurance-service/insurance-service-db:build
call docker-compose build insurance-service-db
call docker-compose up -d --no-deps insurance-service-db