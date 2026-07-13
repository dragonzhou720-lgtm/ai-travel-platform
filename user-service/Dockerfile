FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY user-service/target/user-service-1.0.0-SNAPSHOT.jar /app/user-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]