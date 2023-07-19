FROM node:18-alpine as CONSOLE_BUILD
WORKDIR /work/
COPY certeasy-console-app /work/
RUN npm install
RUN npm run build

FROM registry.access.redhat.com/ubi8/ubi-minimal:8.8-1014
RUN microdnf install -y nginx
WORKDIR /work/
COPY --from=CONSOLE_BUILD /work/dist/** /usr/share/nginx/html/
ENV CERTEASY_DATADIR=/work/data
ENV QUARKUS_LOG_LEVEL=INFO
COPY *-runner /work/app
COPY startup.sh /work/
RUN mkdir -p /work/data
RUN chmod -R 775 /work
EXPOSE 8080
EXPOSE 80
CMD ["./startup.sh"]