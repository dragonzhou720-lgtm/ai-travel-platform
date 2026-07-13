FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY gateway-service/target/gateway-service-1.0.0-SNAPSHOT.jar /app/gateway-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/gateway-service.jar"]