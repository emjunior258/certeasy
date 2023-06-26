import os

DOCKER_IMAGE_PORT = '8080'
DOCKER_IMAGE_NAME = os.getenv('DOCKER_IMAGE_NAME')
# DOCKER_IMAGE_NAME = 'ghcr.io/emjunior258/certeasy:develop'
BASE_URL = "http://0.0.0.0:8080/api/"
