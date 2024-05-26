FROM openjdk:17

# Argument for the JAR file
ARG JAR_FILE=target/*.jar

# Copy the JAR file to the Docker image
COPY ${JAR_FILE} api-shop-sport.jar

# Expose the port the application will run on
EXPOSE 8081

# Set environment variables
ENV SERVER_PORT=8081
ENV MYSQL_HOST=mysqlcontainer
ENV MYSQL_PORT=3306
ENV DATABASE_NAME=freetime
ENV MYSQL_USERNAME=root
ENV MYSQL_PASSWORD=12345678

# Create network
RUN docker network create spring_mysql || true

# Run MySQL container and connect it to the network
RUN docker run -d --name mysqlcontainer  -e MYSQL_ROOT_PASSWORD=12345678 --network spring_mysql mysql:8.0.33

# Default command to run the jar file
ENTRYPOINT ["java","-jar","/api-shop-sport.jar"]
