# Stage 1: build
# Start with a Maven image that includes JDK 17
FROM maven:3-eclipse-temurin-17 AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

#Stage 2: create image
# Start with Eclipse Temurin JDK 17
FROM eclipse-temurin:17-alpine

# Set working folder to App and copy complied file from above step
WORKDIR /app

COPY --from=build /app/target/*.jar sporter-api.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","sporter-api.jar"]
