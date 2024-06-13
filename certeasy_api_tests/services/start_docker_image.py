import time
import docker
import pytest
from certeasy_api_tests.src.config import DOCKER_IMAGE_NAME, DOCKER_IMAGE_PORT

def wait_for_container(container, timeout=30):
    start_time = time.time()
    while time.time() - start_time < timeout:
        if container.status == 'running':
            return True
        time.sleep(0.5)
        container.reload()
    return False

@pytest.fixture(scope="module")
def app_container():
    client = docker.from_env()
    image_name = DOCKER_IMAGE_NAME
    container_port = DOCKER_IMAGE_PORT
    host_port = DOCKER_IMAGE_PORT
    empty_directory = '/path/to/empty_directory'
    volume_mount = {empty_directory: {'bind': '/work/target', 'mode': 'rw'}}

    container = client.containers.run(image_name, detach=True, ports={f'{container_port}/tcp': host_port}, volumes=volume_mount)

    if not wait_for_container(container):
        pytest.fail("Docker container did not start in time")

    yield container

    container.stop()
    container.remove()
