FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8003
ARG JAR_FILE=build/libs/shipments-service-0.0.1.jar
ARG JAR_FILE
COPY ${JAR_FILE} shipments-service-0.0.1.jar
ENTRYPOINT ["java","-jar","/shipments-service-0.0.1.jar"]


# BUILD
# docker build --tag=shipments-servicemessage-server:latest .

# RUN
# docker run -p8003:8003 shipments-service:latest

# INSPECT
# docker inspect message-server
# docker stop message-server
# docker rm message-server

