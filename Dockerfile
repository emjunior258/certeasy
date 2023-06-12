FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
ENV CERTEASY_DATADIR=/work/data
ENV QUARKUS_LOG_LEVEL=INFO
COPY *-runner /work/app
RUN mkdir -p /work/data
RUN chmod -R 775 /work
EXPOSE 8080
CMD ["./app", "-Dquarkus.http.host=0.0.0.0"]