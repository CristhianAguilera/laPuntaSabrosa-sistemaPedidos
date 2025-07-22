    # Use a base image with Java installed (e.g., OpenJDK)
    FROM maven:3.9.6-eclipse-temurin-21 AS build
    
    # Set the working directory inside the container
    WORKDIR /app

    # Copy the Maven project's pom.xml to leverage Maven's dependency caching
    COPY pom.xml .

    # Copy the source code
    COPY src ./src

    RUN mvn clean package -DskipTests

    # Stage 2: Create the final image
    FROM openjdk:21-jdk-slim
    WORKDIR /app
    COPY --from=build /app/target/*.jar app.jar

    # Expose the port your application listens on (if applicable)
    EXPOSE 8080

    # Define the command to run your application when the container starts
    ENTRYPOINT ["java", "-jar", "app.jar"]
