call docker-compose stop registration-service-app
call docker-compose rm --force registration-service-app
cd ../registration-service
call mvn clean package -Dmaven.test.skip=true -DskipITs -DskipTests=true -Darguments=-DskipTests -Dmaven.javadoc.skip=true
call docker image rm --force docker.dos.softlab.ru/rshb/rshb-insurance/registration-service/dev/registration-service-app:latest
cd ../insurance-service
call docker-compose build registration-service-app
call docker-compose up -d --no-deps registration-service-app