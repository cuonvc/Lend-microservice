FROM eclipse-temurin:17

COPY ./target/product-service-core-0.0.1-SNAPSHOT.jar product-service-core.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/product-service-core.jar"]