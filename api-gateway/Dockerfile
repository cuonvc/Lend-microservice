FROM eclipse-temurin:17

COPY ./target/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar

CMD ["java","-Dspring.profiles.active=prod","-jar","api-gateway.jar"]