FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/transaction-service-0.0.1-SNAPSHOT.jar /app/transaction-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/transaction-service-0.0.1-SNAPSHOT.jar"]