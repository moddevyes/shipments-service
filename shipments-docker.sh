#!/bin/bash

# DATABASE
echo ""
echo "Creating SHIPMENTS database."
echo ""

docker run --name shipments_mysql_container -d -p 3303:3308 -e MYSQL_ROOT_PASSWORD_FILE=/run/secrets/mysql-root-password \
-e MYSQL_DATABASE=ecommerce_shipments_db \
-e MYSQL_USER=davidking \
-e MYSQL_PASSWORD=davidking!! \
--env-file config/.env.dev -v ./secrets:/run/secrets --network commerce-net --restart unless-stopped mysql:8.0.1

# DOCKER IMAGE
echo ""
echo "Building DOCKER image for shipments-service."
echo ""
docker build -t shipments-service .

echo ""
echo "Deploying/Running DOCKER image for shipments-service."
echo ""
docker run -d --env-file config/.env.dev --name=shipments_service_container --net=commerce-net -p 8003:8003 --restart unless-stopped shipments-service

# VERIFY
echo ""
echo "VERIFY deployment"
echo ""
docker ps
echo ""
echo ""
docker images
