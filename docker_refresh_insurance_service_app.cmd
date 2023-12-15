call docker-compose stop insurance-service-app
call docker-compose rm --force insurance-service-app
call mvn clean package -Dmaven.test.skip=true -DskipITs -DskipTests=true -Darguments=-DskipTests -Dmaven.javadoc.skip=true
call docker image rm --force docker.dos.softlab.ru/smedservice/insurance-service/insurance-service-app:build
call docker-compose build insurance-service-app
call docker-compose up -d --no-deps insurance-service-app