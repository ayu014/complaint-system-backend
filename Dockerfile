# Stage 1: Build the application with Maven
FROM maven:3.8-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy just the pom.xml to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of your source code
COPY src ./src

# Build the .jar file
RUN mvn clean install -DskipTests

# Stage 2: Create the final lightweight image
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# !! IMPORTANT: Check your .jar file name !!
# Look in your backend's 'target' folder and make sure this name matches.
COPY --from=build /app/target/complaint-system-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]