FROM openjdk:17-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY target/transaction-service-0.0.1-SNAPSHOT.jar /app/transaction-service-0.0.1-SNAPSHOT.jar

EXPOSE 8082

CMD ["java", "-jar", "/app/transaction-service-0.0.1-SNAPSHOT.jar"]