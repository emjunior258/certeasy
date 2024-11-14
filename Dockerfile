FROM registry.access.redhat.com/ubi8/ubi-minimal:8.8-1014
RUN microdnf install -y nginx
WORKDIR /work/
ENV CERTEASY_DATADIR=/work/data
ENV QUARKUS_LOG_LEVEL=INFO
COPY *-runner /work/app
COPY certeasy-console-app/dist /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/
COPY startup.sh /work/
RUN mkdir -p /work/data
RUN chmod -R 775 /work
EXPOSE 8080
EXPOSE 80
CMD ["./startup.sh"]