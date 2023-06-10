import docker
import requests
import time
import pytest

from certeasy_api_tests.src.config import BASE_URL, DOCKER_IMAGE_NAME, DOCKER_IMAGE_PORT


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

    # Start the container with volume mounting and port mapping
    container = client.containers.run(image_name, detach=True, ports={f'{container_port}/tcp': host_port},
                                      volumes=volume_mount)
    print(container)

    # Wait for the container to start
    time.sleep(2)

    # Manually check the container's port mapping
    container_info = client.api.inspect_container(container.id)
    port_mapping = container_info['NetworkSettings']['Ports']
    print("Container Port Mapping:")
    print(port_mapping)

    # Print container logs for debugging
    print("Container Logs:")
    print(container.logs().decode('utf-8'))

    yield container

    # Stop and remove the container
    container.stop()
    container.remove()


def test_should_return_200(app_container):
    # Make a request to the API
    response = requests.get(url=f'{BASE_URL}/issuers')
    # Assert the response status
    expected_status = 200
    assert response.status_code == expected_status, f'Expected status {expected_status}, but received {response.status_code}'


def test_should_return_a_list(app_container):
    # Make a request to the API
    response = requests.get(url=f'{BASE_URL}/issuers')
    print(response.json())
    assert type(response.json()) == list, f'Expected data type "LIST, but received {type(response.json())}'


def test_validate_content_of_body_response(app_container):
    # Make a request to the API
    response = requests.get(url=f'{BASE_URL}/issuers')
    assert "dummy test" in response.json(), f'Expected data type "LIST, but received {response.json()}'
