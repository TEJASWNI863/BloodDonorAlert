FROM tomcat:11-jdk17

# Remove default ROOT application
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy WAR file
COPY target/BloodDonorAlert.war /usr/local/tomcat/webapps/ROOT.war

# Expose port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]