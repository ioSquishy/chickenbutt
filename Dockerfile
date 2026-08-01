# Stage 1: Build the Maven artifact
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build application
COPY src ./src
RUN mvn clean package -DskipTests

# Delete the un-shaded JAR so target/ only contains the fat JAR
RUN rm -f target/original-*.jar


# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Now target/*.jar safely matches ONLY the executable fat JAR
COPY --from=builder /app/target/*.jar app.jar

VOLUME ["/app/data"]

ENTRYPOINT ["java", "-jar", "app.jar"]