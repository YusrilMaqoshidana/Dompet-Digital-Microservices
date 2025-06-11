FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/eureka-service-0.0.1-SNAPSHOT.jar /app/eureka-service-0.0.1-SNAPSHOT.jar

EXPOSE 8761

CMD ["java", "-jar", "/app/eureka-service-0.0.1-SNAPSHOT.jar"]