# Stage 1: Build the Maven artifact
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built shaded JAR, ignoring the un-shaded 'original-' JAR
COPY --from=builder /app/target/!(*original*).jar app.jar

# Create volume target for persistent data (e.g., userData.ser)
VOLUME ["/app/data"]

ENTRYPOINT ["java", "-jar", "app.jar"]