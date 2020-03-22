#!/bin/bash

echo "********************************************************"
echo "Starting Transaction Agent Service ..."
echo "********************************************************"
java -Ddebug=$DEBUG_MODE \
     -Dserver.port=$SERVERPORT \
     -Dspring.datasource.url=jdbc:mysql://$DBSERVER:$DBPORT/$JDBCURL \
     -Dspring.datasource.username=$DBUSERNAME \
     -Dspring.datasource.password=$DBPASSWORD -jar /usr/local/authsrv/@project.build.finalName@.jar
