FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY attraction-service/target/attraction-service-1.0.0-SNAPSHOT.jar /app/attraction-service.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/attraction-service.jar"]