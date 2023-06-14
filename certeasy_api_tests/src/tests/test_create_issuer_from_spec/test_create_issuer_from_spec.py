import requests
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.services.generate_issuer_id import generate_issuer_id
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


def test_should_create_a_new_issuer_from_spec(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 204


def test_should_create_new_issuer_with_zero_path_length(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec_with_zero_path_length.json')
    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 204


def test_should_not_allows_creation_of_issuer_with_empty_issuer_id(app_container):
    ISSUER_ID = ''
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 405


def test_should_not_allows_creation_of_issuer_with_sequence_of_space_issuer_id(app_container):
    ISSUER_ID = '     '
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_existed_issuer_id(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    response_status = int
    # Make a request to the API
    for i in range(2):
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
        response_status = response.status_code
    assert response_status == 409


def test_should_not_allows_creation_of_issuer_with_empty_body(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    response_status = int
    # Make a request to the API
    for i in range(2):
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
        response_status = response.status_code
    assert response_status == 409
