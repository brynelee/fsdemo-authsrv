#!/bin/bash

echo "********************************************************"
echo "Starting Authorization Service ..."
echo "********************************************************"
java -Ddebug=$DEBUG_MODE \
     -Dserver.port=$SERVERPORT \
     -Dspring.datasource.url=jdbc:mysql://$DBSERVER:$DBPORT/$JDBCURL \
     -Dspring.datasource.username=$DBUSERNAME \
     -Dspring.datasource.password=$DBPASSWORD -jar /usr/src/app/fsdemo-authsrv-0.0.1-SNAPSHOT.jar
