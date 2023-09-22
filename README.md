# shipments-service
Fulfillment Domain - Shipments Service

## Fulfillment Domain, Shipments

* Shipments have:

    - Account
    - Shipping Address
    - Order Line Items
    - Shipped Date
    - Delivery Date

## Local build and testing

* Build, gradle clean build bootRun

## DOCKER build and testing

* Run script
  - accounts-docker.sh

- Pre-requisites

1. Docker installation required, linux for script build with docker, docker-compose
2. Normal gradle clean and build *before* running docker script