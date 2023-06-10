#!/bin/bash
cd certeasy-core
./mvnw clean install
cd ../certeasy-bouncycastle
./mvnw clean install
cd ../certeasy-backend-app
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
mkdir /tmp/certeasy-build
cp Dockerfile /tmp/certeasy-build
cp target/*-runner /tmp/certeasy-build/
chmod +x /tmp/certeasy-build/*-runner
cd /tmp/certeasy-build
docker build -t certeasy:local .