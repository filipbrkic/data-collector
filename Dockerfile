FROM openjdk:20-jdk-slim

WORKDIR /app

COPY target/collector-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties application.properties
COPY .env .env

RUN apt-get update \
    && apt-get install -y postgresql-client \
    && rm -rf /var/lib/apt/lists/*

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
