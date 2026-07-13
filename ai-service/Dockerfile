FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY ai-service/target/ai-service-1.0.0-SNAPSHOT.jar /app/ai-service.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/ai-service.jar"]