from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.services.create_issuer_from_spec.generate_json import generate_issuer_json
from certeasy_api_tests.src.config import BASE_URL
import requests


def create_issuer_from_spec():
    loaded_schema = file_reader(filename='../services/create_issuer_from_spec/issuer_schema.json')
    VALID_BODY = generate_issuer_json(loaded_schema)
    try:
        create_issuer = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=VALID_BODY)  # No need for json.dumps()
        if create_issuer.status_code == 200:
            issuer_id = create_issuer.json()['issuer_id']
            return issuer_id
    except (requests.RequestException, ValueError) as e:
        raise RuntimeError(f"Error occurred while trying to create issuer from spec: {str(e)}")
