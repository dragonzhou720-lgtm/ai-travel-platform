FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY hotel-service/target/hotel-service-1.0.0-SNAPSHOT.jar /app/hotel-service.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/app/hotel-service.jar"]