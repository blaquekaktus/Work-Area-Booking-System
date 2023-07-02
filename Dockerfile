# Use Amazon Corretto 17 as the base image
FROM amazoncorretto:17-alpine-jdk

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper files
COPY .mvn .mvn
COPY mvnw mvnw


RUN mvnw clean package -DskipTests


# Copy the Maven wrapper JAR file
COPY .mvn/wrapper/maven-wrapper.jar .mvn/wrapper/maven-wrapper.jar

# Copy the project files
COPY pom.xml .
COPY src src

# Run Maven to build the application
RUN ./mvnw clean package -DskipTests

# Use a lightweight Java image as the final image
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY --from=0 /app/target/booking-system.jar .

# Set the entry point
ENTRYPOINT ["java", "-jar", "booking-system.jar"]
