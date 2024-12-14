FROM eclipse-temurin:17-jdk-alpin
WORKDIR /app
COPY target/docker-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]