#!/bin/bash
echo 'Building Services'
set -e
./gradlew build
echo 'Artifacts building completed'

echo 'Building Docker images for all artifacts'

docker-compose build

echo 'Starting all microservices in their respective containers'
docker-compose up -d

echo 'All microservices have been started successfully'