import os

DOCKER_IMAGE_PORT = '8080'
DOCKER_IMAGE_NAME = os.getenv('DOCKER_IMAGE_NAME', 'ghcr.io/certeasy:test')
BASE_URL = f"http://0.0.0.0:{DOCKER_IMAGE_PORT}/api/"
