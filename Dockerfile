FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
COPY --chown=quarkus:quarkus *-runner /work/application
RUN chmod 775 /work/application

# Create the data directory
RUN mkdir /work/target
RUN chmod 775 /work/target

# Ensure execution permission for the application file
RUN chmod +x /work/application

EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
