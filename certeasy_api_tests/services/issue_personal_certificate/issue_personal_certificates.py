import requests
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.services.issue_personal_certificate.generate_json import generate_personal_data
from certeasy_api_tests.services.issue_tls_server_certificate.generate_json import generate_tls_server_data
from certeasy_api_tests.src.config import BASE_URL


def issue_personal_certs():
    loaded_schema = file_reader(
        filename='../services/issue_personal_certificate/issue_personal_certificate_schema.json')
    CREATED_ISSUER = create_issuer_from_spec()
    VALID_BODY = generate_personal_data(loaded_schema)
    issue_personal_cert = requests.post(url=f'{BASE_URL}/issuers/{CREATED_ISSUER[0]}/certificates/personal',
                                          json=VALID_BODY)
    CERT_NAME = f"{VALID_BODY['name']} {VALID_BODY['surname']}"
    return issue_personal_cert.json()["serial"], CREATED_ISSUER[0], CERT_NAME, CREATED_ISSUER[1]["name"]
