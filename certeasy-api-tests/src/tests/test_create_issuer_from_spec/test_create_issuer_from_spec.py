import requests
import pytest
from testcontainers.core.container import DockerContainer


@pytest.fixture(scope="module")
def docker_container():
    # Create a Docker container
    docker_image = 'ghcr.io/certeasy:test'

    # Create docker container
    container = DockerContainer(docker_image)
    container.with_exposed_ports(8080)

    # Start the container
    with container as docker_container:
        # Get the container host and port
        host = docker_container.get_container_host_ip()
        port = '8080'

        # Pass the container details to the tests
        yield host, port
    # The container will automatically be stopped and removed after the yield statement


def test_should_return_status_code(docker_container):
    host, port = docker_container
    try:
        response = requests.get(url=f'http://{host}:8080/', verify=False)
        print(response.json)
        assert response.status_code == 200
    except Exception as e:
        pytest.fail(f"An exception occurred: {str(e)}")



