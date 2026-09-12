# ==========================================
# Stage 1: Build
# ==========================================

FROM maven:3.9.10-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy complete CommerceX multi-module project
COPY . .

# Build API Gateway and required modules
RUN mvn clean package \
    -pl infrastructure/api-gateway \
    -am \
    -DskipTests


# ==========================================
# Stage 2: Runtime
# ==========================================

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder \
    /build/infrastructure/api-gateway/target/api-gateway-*.jar \
    app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]