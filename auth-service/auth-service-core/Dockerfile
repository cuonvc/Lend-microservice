FROM eclipse-temurin:17

COPY ./target/auth-service-core-0.0.1-SNAPSHOT.jar auth-service-core.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/auth-service-core.jar"]