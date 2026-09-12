# ==========================================
# Stage 1: Build
# ==========================================

FROM maven:3.9.10-eclipse-temurin-21 AS builder

WORKDIR /build

COPY . .

RUN mvn clean package \
    -pl services/product-service \
    -am \
    -DskipTests


# ==========================================
# Stage 2: Runtime
# ==========================================

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder \
    /build/services/product-service/target/product-service-*.jar \
    app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]