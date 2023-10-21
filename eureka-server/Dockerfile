# Use a multi-architecture base image
FROM --platform=$BUILDPLATFORM eclipse-temurin:17 AS builder

# Set up working directory
WORKDIR /app

# Copy your Spring Boot application JAR into the container
COPY ./target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar

# Build an image for the target architecture
FROM --platform=$TARGETPLATFORM eclipse-temurin:17 AS final

# Set up working directory
WORKDIR /app

# Copy the JAR file from the builder image
COPY --from=builder /app/eureka-server.jar .

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "/app/eureka-server.jar"]
