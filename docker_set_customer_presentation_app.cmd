call docker-compose stop web-presentation-app
call docker-compose rm --force web-presentation-app
cd ../web-custom-presentation-app
call docker build . -t docker.dos.softlab.ru/rshb/rshb-insurance/web-presentation-app/web-presentation-app:pr
cd ../insurance-service
call docker-compose build web-presentation-app
call docker-compose up -d --no-deps web-presentation-app