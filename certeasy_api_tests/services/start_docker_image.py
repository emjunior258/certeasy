import time
import docker
import pytest

from certeasy_api_tests.src.config import DOCKER_IMAGE_NAME, DOCKER_IMAGE_PORT


@pytest.fixture(scope="module")
def app_container():
    # Create a Docker client
    client = docker.from_env()

    # Define the Docker image and port
    image_name = DOCKER_IMAGE_NAME
    container_port = DOCKER_IMAGE_PORT
    host_port = DOCKER_IMAGE_PORT

    # Define the volume mount
    empty_directory = '/path/to/empty_directory'
    volume_mount = {empty_directory: {'bind': '/work/target', 'mode': 'rw'}}

    # 
    container = client.containers.run(image_name, detach=True, ports={f'{container_port}/tcp': host_port},
                                      volumes=volume_mount)

    # Wait for the container to start
    time.sleep(2)

    yield container

    # Stop and remove the container
    container.stop()
    container.remove()
