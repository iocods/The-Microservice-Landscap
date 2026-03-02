#!/bin/bash
echo 'Building Services'
set -e
./gradlew build

echo 'Running all the microservices in unison'

java -jar composite/build/libs/*SNAPSHOT.jar &
java -jar product/build/libs/*SNAPSHOT.jar &
java -jar recommendation/build/libs/*SNAPSHOT.jar &
java -jar review/build/libs/*SNAPSHOT.jar

echo 'All microservices have been started successfully'