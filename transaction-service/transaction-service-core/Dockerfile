FROM eclipse-temurin:17

COPY ./target/transaction-service-core-0.0.1-SNAPSHOT.jar transaction-service-core.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/transaction-service-core.jar"]