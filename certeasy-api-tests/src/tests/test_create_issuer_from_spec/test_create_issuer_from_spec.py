import docker
import requests
import time
import pytest


@pytest.fixture(scope="module")
def app_container():
    # Create a Docker client
    client = docker.from_env()

    # Define the Docker image and port
    image_name = 'ghcr.io/certeasy:test'
    container_port = 8000

    # Start the container
    container = client.containers.run(image_name, detach=True, ports={f'{container_port}/tcp': container_port})
    print(container)
    # Wait for the container to start
    time.sleep(2)

    yield container

    # Stop and remove the container
    container.stop()
    container.remove()


def test_api_status(app_container):
    # Make a request to the API
    url = f'http://localhost:8000/'
    response = requests.get(url)

    # Assert the response status
    expected_status = 200
    assert response.status_code == expected_status, f'Expected status {expected_status}, but received {response.status_code}'

    # Add more assertions as needed
