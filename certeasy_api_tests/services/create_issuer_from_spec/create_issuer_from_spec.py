from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.src.config import BASE_URL
import requests
from certeasy_api_tests.services.start_docker_image import app_container


def create_issuer_from_spec(app_container):
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    try:
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=VALID_BODY)
        return response.json()[0]
    except (requests.RequestException, ValueError) as e:
        raise RuntimeError(f"Error occurred while trying to create issuer if from spec: {str(e)}")


