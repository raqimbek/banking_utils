# Use the official Maven image with Eclipse Temurin JDK
FROM maven:3.9.11-eclipse-temurin-17 AS build

# Set the working directory to the web submodule
WORKDIR /banking_utils/web

# Copy the pom.xml and the source code of the web submodule
COPY pom.xml ./
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use a smaller image for the runtime
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /banking_utils/web

# Copy the jar file from the build stage
COPY --from=build /banking_utils/web/target/web-1.0-SNAPSHOT.jar app.jar

# Set metadata about the image
LABEL authors="Raqimbek"

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
