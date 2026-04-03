# Build stage
FROM maven:3.9.12-eclipse-temurin-25 AS build

WORKDIR /banking_utils

# Copy parent POM
COPY pom.xml ./

# Copy module POMs for reactor resolution
COPY cli/pom.xml ./cli/pom.xml

# Copy modules needed for web
COPY core ./core
COPY web ./web

# Build only the web module and its dependencies
RUN mvn clean package -pl web -am -DskipTests -Pdocker

# Runtime stage
FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

# Copy the built JAR from web module
COPY --from=build /banking_utils/web/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "8080"]