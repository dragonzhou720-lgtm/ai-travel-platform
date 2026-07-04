FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY favorite-service/target/favorite-service-1.0.0-SNAPSHOT.jar /app/favorite-service.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "/app/favorite-service.jar"]