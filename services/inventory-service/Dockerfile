FROM maven:3.9.10-eclipse-temurin-21 AS builder

WORKDIR /build

COPY . .

RUN mvn clean package \
    -pl services/inventory-service \
    -am \
    -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder \
    /build/services/inventory-service/target/inventory-service-*.jar \
    app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]