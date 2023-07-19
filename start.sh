#!/bin/bash
/work/app -Dquarkus.http.host=0.0.0.0 &
nginx -g daemon off