FROM eclipse-temurin:17

COPY ./target/mail-service-0.0.1-SNAPSHOT.jar mail-service.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/mail-service.jar"]