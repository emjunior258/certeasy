import docker
import requests
import time
import pytest


@pytest.fixture(scope="module")
def app_container():
    # Create a Docker client
    client = docker.from_env()

    # Define the Docker image and port
    image_name = 'ghcr.io/emjunior258/certeasy:develop'
    container_port = 8080
    host_port = 8000  # Change the host port to your desired port number

    # Start the container
    container = client.containers.run(image_name, detach=True, ports={f'{container_port}/tcp': host_port})
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


def test_api_status(app_container):
    # Make a request to the API
    url = f'http://localhost:8000/'  # Use the host port you specified above
    response = requests.get(url)

    # Assert the response status
    expected_status = 200
    assert response.status_code == expected_status, f'Expected status {expected_status}, but received {response.status_code}'
