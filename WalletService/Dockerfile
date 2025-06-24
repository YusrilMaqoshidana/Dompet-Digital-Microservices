FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/WalletService-0.0.1-SNAPSHOT.jar /app/WalletService-0.0.1-SNAPSHOT.jar

EXPOSE 8083

CMD ["java", "-jar", "/app/WalletService-0.0.1-SNAPSHOT.jar"]