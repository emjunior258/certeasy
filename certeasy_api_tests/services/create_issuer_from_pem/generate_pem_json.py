import json

from certeasy_api_tests.services.create_issuer_from_pem.generate_pem_cert import generate_certificate_pem
from certeasy_api_tests.services.create_issuer_from_pem.generate_pem_key import generate_private_key_pem
from certeasy_api_tests.services.file_reader import file_reader


def generate_cert_and_key_schema():
    cert_content = generate_certificate_pem()
    key_content = generate_private_key_pem()

    cert_and_key_schema = {
        "cert_file": cert_content.decode(),
        "key_file": key_content.decode()
    }

    data = json.dumps(cert_and_key_schema)
    return data
