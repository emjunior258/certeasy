import requests
from certeasy_api_tests.services.create_issuer_from_pem.generate_pem_json import generate_valid_certs_with_ca
from certeasy_api_tests.src.config import BASE_URL


def create_issuer_from_pem():
    VALID_BODY = generate_valid_certs_with_ca()
    try:
        create_issuer = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=VALID_BODY)
        if create_issuer.status_code == 200:
            issuer_id = create_issuer.json()['issuer_id']
            return issuer_id, VALID_BODY
    except (requests.RequestException, ValueError) as e:
        raise RuntimeError(f"Error occurred while trying to create issuer from spec: {str(e)}")