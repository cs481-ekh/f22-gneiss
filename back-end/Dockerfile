# Build stage
FROM maven:latest AS build
COPY back-end/src /home/app/src
COPY back-end/pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean test package

# Package stage
FROM openjdk:latest
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
