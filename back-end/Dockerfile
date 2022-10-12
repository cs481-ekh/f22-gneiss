# Static stage
FROM node:latest AS static
RUN mkdir /front-end
WORKDIR /front-end
ENV CI=true

COPY front-end /front-end/
RUN npm ci && npm run build

# Build stage
FROM maven:latest AS build
COPY back-end/src /home/app/src
COPY --from=static /front-end/build /home/app/src/main/resources/static
COPY back-end/pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean test package

# Package stage
FROM openjdk:latest
COPY --from=build /home/app/target/*.jar app.jar
COPY --from=build /home/app/src/main/resources/static /
RUN mkdir output
CMD ["java","-jar","app.jar"]
