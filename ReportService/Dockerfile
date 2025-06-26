FROM openjdk:17-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY target/ReportService-0.0.1-SNAPSHOT.jar /app/ReportService-0.0.1-SNAPSHOT.jar

EXPOSE 8085

CMD ["java", "-jar", "/app/ReportService-0.0.1-SNAPSHOT.jar"]