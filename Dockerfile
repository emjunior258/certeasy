FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
COPY *-runner /work/application
RUN chmod 775 /work/application
EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]