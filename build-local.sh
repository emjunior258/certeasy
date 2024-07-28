#!/bin/bash

BUILD_DIR="/tmp/certeasy-build"

function build_java_module() {
    pushd $1
    ./mvnw clean install
    popd
}

function build_nodejs_module () {
    pushd $1
    pnpm install
    pnpm build
    cp -r ./dist "${BUILD_DIR}/$1"
    popd
}

function build_backend_app () {
    pushd certeasy-backend-app
    ./mvnw package -Dnative -DskipTests -Dquarkus.native.container-build=true
    cp ./target/*-runner "${BUILD_DIR}"
    chmod +x ${BUILD_DIR}/*-runner
    popd
}

function build_docker_image () {
    cp Dockerfile "${BUILD_DIR}/"
    cp startup.sh "${BUILD_DIR}/"
    cp nginx.conf "${BUILD_DIR}/"
    docker build -t certeasy:local ${BUILD_DIR}
}

# Ensure build dir
mkdir -p "${BUILD_DIR}"
build_nodejs_module "certeasy-console-app"
build_java_module "certeasy-core"
build_java_module "certeasy-bouncycastle"
build_backend_app
build_docker_image