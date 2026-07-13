FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY route-service/target/route-service-1.0.0-SNAPSHOT.jar /app/route-service.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/route-service.jar"]