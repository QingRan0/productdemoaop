#https://github.com/docker-library/docs/tree/master/mysql
docker pull mysql
docker service create --name mysql --constraint node.labels.database==mysql --mount type=bind,source=/root/JavaEEPlatform/mysql/sql,destination=/sql,readonly --mount type=bind,source=/root/JavaEEPlatform/mysql/conf.d,destination=/etc/mysql/conf.d,readonly --network my-net -e MYSQL_ROOT_PASSWORD=wxhyyds_0727  -d mysql:latest
docker service ps mysql
docker exec -it mysql -uroot -p