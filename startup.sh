#!/bin/bash

cleanup() {
  echo "Performing cleanup..."
  exit 0
}

# Capture SIGTERM and execute the cleanup function
trap cleanup SIGTERM

nginx -g "daemon off;" &
/work/app -Dquarkus.http.host=0.0.0.0 &
wait