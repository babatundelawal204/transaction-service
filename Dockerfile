FROM maven:3.9.9-eclipse-temurin-21-alpine as build

WORKDIR /TransferService/user-service

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /TransferService/user-service

COPY --from=build target/*.jar /home/transfer-service.jar

EXPOSE 8080

CMD ["java", "-jar", "/home/transfer-service.jar"]