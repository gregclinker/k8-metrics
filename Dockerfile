FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY target/k8-metrics-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]