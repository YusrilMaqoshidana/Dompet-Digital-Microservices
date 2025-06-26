FROM openjdk:17-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY target/TopupService-0.0.1-SNAPSHOT.jar /app/TopupService-0.0.1-SNAPSHOT.jar

EXPOSE 8084

CMD ["java", "-jar", "/app/TopupService-0.0.1-SNAPSHOT.jar"]