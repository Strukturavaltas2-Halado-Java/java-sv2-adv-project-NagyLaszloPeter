#### SQL
create user 'poster'@'localhost' identified by 'poster';
grant all on *.* to 'poster'@'localhost';
#
#### DOCKER
docker run -d -e MYSQL_DATABASE=poster -e MYSQL_USER=poster -e MYSQL_PASSWORD=poster -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -p 3306:3306 --name poster-mariadb mariadb

