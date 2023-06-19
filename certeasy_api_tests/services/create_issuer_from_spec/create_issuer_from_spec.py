from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.services.generate_issuer_id import generate_issuer_id
from certeasy_api_tests.src.config import BASE_URL
import requests


def create_issuer_from_spec():
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    try:
        requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
        return ISSUER_ID
    except (requests.RequestException, ValueError) as e:
        raise RuntimeError(f"Error occurred while trying to create issuer if from spec: {str(e)}")
