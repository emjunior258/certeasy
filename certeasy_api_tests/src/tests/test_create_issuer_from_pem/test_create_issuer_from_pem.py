import requests
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


