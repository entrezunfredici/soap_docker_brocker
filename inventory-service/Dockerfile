# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application with retry logic
RUN mvn clean package -DskipTests -Dorg.slf4j.simpleLogger.defaultLogLevel=warn --fail-at-end -T 1C

# Runtime stage
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Start the application
CMD ["java", "-jar", "app.jar"]
