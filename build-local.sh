#!/bin/bash
cd certeasy-core
./mvnw clean install
cd ../certeasy-bouncycastle
./mvnw clean install
cd ../certeasy-backend-app
./mvnw package -Dnative -DskipTests -Dquarkus.native.container-build=true
cd ../
mkdir -p /tmp/certeasy-build
cp Dockerfile /tmp/certeasy-build/
cp startup.sh /tmp/certeasy-build/
cp -r certeasy-console-app /tmp/certeasy-build/
cp certeasy-backend-app/target/*-runner /tmp/certeasy-build/
chmod +x /tmp/certeasy-build/*-runner
cd /tmp/certeasy-build
docker build -t certeasy:local .