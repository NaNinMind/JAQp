#
# Build stage
#
FROM maven:3.9.6-sapmachine-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -e -f /home/app/pom.xml clean package -Dmaven.test.skip

#
# Package stage
#
FROM openjdk:22-ea-17-slim
COPY --from=build /home/app/target/JAQpApi-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]