import requests
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.services.issue_sub_ca_certificate.generate_json import generate_sub_ca_data
from certeasy_api_tests.src.config import BASE_URL


def issue_sub_ca_certs():
    loaded_schema = file_reader(
        filename='../services/issue_sub_ca_certificate/issue_sub_ca_certificate_schema.json')
    CREATED_ISSUER = create_issuer_from_spec()
    VALID_BODY = generate_sub_ca_data(loaded_schema)
    issue_sub_ca_cert = requests.post(url=f'{BASE_URL}/issuers/{CREATED_ISSUER[0]}/certificates/sub-ca',
                                          json=VALID_BODY)
    CERT_NAME = f"{VALID_BODY['name']}"
    return issue_sub_ca_cert.json()["serial"], CREATED_ISSUER[0], CERT_NAME, CREATED_ISSUER[1]["name"]

