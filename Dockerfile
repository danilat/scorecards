#
# Build stage
#
FROM openjdk:14-jdk-alpine AS build
COPY . /home/app/
WORKDIR /home/app/
RUN ./mvnw -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:14-jdk-alpine
ARG JAR_FILE=boot/target/*.jar
COPY --from=build /home/app/boot/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]