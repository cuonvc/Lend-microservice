# Use a multi-architecture base image
FROM --platform=$BUILDPLATFORM eclipse-temurin:17 AS builder

# Set up working directory
WORKDIR /app

# Copy your Spring Boot application JAR into the container
COPY ./target/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar

# Build an image for the target architecture
FROM --platform=$TARGETPLATFORM eclipse-temurin:17 AS final

# Set up working directory
WORKDIR /app

# Copy the JAR file from the builder image
COPY --from=builder /app/api-gateway.jar .

# Specify the command to run your Spring Boot application
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "-jar", "/app/api-gateway.jar"]
