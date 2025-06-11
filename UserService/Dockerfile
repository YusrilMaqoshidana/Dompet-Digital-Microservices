FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/user-service-0.0.1-SNAPSHOT.jar /app/user-service-0.0.1-SNAPSHOT.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/user-service-0.0.1-SNAPSHOT.jar"]