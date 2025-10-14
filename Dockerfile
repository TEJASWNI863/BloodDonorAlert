# Build stage
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM tomcat:11.0-jdk17
WORKDIR /usr/local/tomcat/webapps
# Remove default ROOT application
RUN rm -rf ROOT
# Copy your WAR file and rename it to ROOT.war so it runs at /
COPY --from=build /app/target/BloodDonorAlert.war ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]