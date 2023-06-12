import requests
from assertpy import assert_that
from precisely import assert_that, is_sequence
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


def test_should_return_200(app_container):
    # Make a request to the API
    response = requests.get(url=f'{BASE_URL}/issuers')
    # Assert the response status
    expected_status = 200
    assert response.status_code == expected_status, f'Expected status {expected_status}, but received {response.status_code}'


def test_should_return_a_list(app_container):
    # Make a request to the API
    response = requests.get(url=f'{BASE_URL}/issuers')
    assert type(response.json()) == list, f'Expected data type "LIST, but received {type(response.json())}'


def test_validate_if_the_body_response_include_the_newest_issuer(app_container):
    # Create issuer ID before list them
    ISSUER_ID = create_issuer_from_spec()
    # Make a request to the API
    response = requests.get(url=f'{BASE_URL}/issuers')
    assert ISSUER_ID in response.json()[0]['id']


def test_validate_content_of_body_response(app_container):
    # Create issuer ID before list them
    create_issuer_from_spec()
    # Make a request to the API
    response = requests.get(url=f'{BASE_URL}/issuers')
    i = len(response.json())
    for a in range(i):
        assert_that(response.json()[a],
                    is_sequence("id",
                                "type",
                                "serial",
                                "dn",
                                "path_length"))