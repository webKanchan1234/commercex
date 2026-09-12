FROM maven:3.9.10-eclipse-temurin-21 AS builder

WORKDIR /build

COPY . .

RUN mvn clean package \
    -pl services/order-service \
    -am \
    -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder \
    /build/services/order-service/target/order-service-*.jar \
    app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]