import requests
from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.src.config import BASE_URL


def test_should_create_a_new_issuer_from_spec():
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=VALID_BODY)
    assert response.status_code == 200


