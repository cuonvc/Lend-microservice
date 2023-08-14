FROM eclipse-temurin:17

COPY ./target/resource-service-0.0.1-SNAPSHOT.jar resource-service.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/resource-service.jar"]