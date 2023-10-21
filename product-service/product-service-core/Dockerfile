# Use a multi-architecture base image
FROM --platform=$BUILDPLATFORM eclipse-temurin:17 AS builder

# Set up working directory
WORKDIR /app

# Copy your Spring Boot application JAR into the container
COPY ./target/product-service-core-0.0.1-SNAPSHOT.jar product-service.jar

# Build an image for the target architecture
FROM --platform=$TARGETPLATFORM eclipse-temurin:17 AS final

# Set up working directory
WORKDIR /app

# Copy the JAR file from the builder image
COPY --from=builder /app/product-service.jar .

# Specify the command to run your Spring Boot application
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "/app/product-service.jar"]