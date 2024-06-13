import requests
from certeasy_api_tests.services.create_issuer_from_spec.generate_issuer_id import generate_hex_id
from certeasy_api_tests.services.generate_radom_serial_id import generate_serial_id
from certeasy_api_tests.services.global_var import SEQUENCE_OF_SPACE_ISSUER_ID, SEQUENCE_OF_SPACE_SERIAL_ID
from certeasy_api_tests.services.issue_employee_certificate.issue_employee_certificate import issue_employee_certs
from certeasy_api_tests.services.issue_personal_certificate.issue_personal_certificates import issue_personal_certs
from certeasy_api_tests.services.issue_sub_ca_certificate.issue_sub_ca_certificate import issue_sub_ca_certs
from certeasy_api_tests.services.issue_tls_server_certificate.issue_tls_server_certificates import \
    issue_tls_server_certs
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


class TestGetCertPem:
    def test_should_return_pem_of_issued_employee_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_employee_certs()

        # get pem of cert
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}/pem')
        assert response.status_code == 200
        assert "cert_file" in response.json()
        assert "key_file" in response.json()

    def test_should_return_pem_of_issued_personal_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_personal_certs()
        # get pem of cert
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}/pem')
        assert response.status_code == 200
        assert "cert_file" in response.json()
        assert "key_file" in response.json()

    def test_should_return_pem_of_issued_sub_ca_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_sub_ca_certs()
        # get pem of cert
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}/pem')
        assert response.status_code == 200
        assert "cert_file" in response.json()
        assert "key_file" in response.json()


    def test_should_return_pem_of_issued_tls_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_tls_server_certs()
        # get pem of cert
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}/pem')
        assert response.status_code == 200
        assert "cert_file" in response.json()
        assert "key_file" in response.json()

    def test_should_not_return_pem_of_issued_cert_when_pass_a_sequence_of_space_cert_serial(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_tls_server_certs()
        # get pem of cert
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{SEQUENCE_OF_SPACE_SERIAL_ID}/pem')
        assert response.status_code == 400


    def test_should_not_return_pem_of_issued_cert_when_pass_a_non_existed_cert_serial(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_tls_server_certs()
        # get pem of cert
        NON_EXISTED_CERT_SERIAL = generate_serial_id()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{NON_EXISTED_CERT_SERIAL}/pem')
        assert response.status_code == 404

    def test_should_not_return_pem_of_issued_cert_when_pass_a_non_existed_issuer_id(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_tls_server_certs()
        # get pem of cert
        NON_EXISTED_ISSUER_ID = generate_hex_id()
        response = requests.get(url=f'{BASE_URL}/issuers/{NON_EXISTED_ISSUER_ID}/certificates/{CERT_SERIAL}/pem')
        assert response.status_code == 404

    def test_should_not_return_pem_of_issued_cert_when_pass_a_sequence_of_space_issuer_id(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_tls_server_certs()
        # get pem of cert
        response = requests.get(url=f'{BASE_URL}/issuers/{SEQUENCE_OF_SPACE_ISSUER_ID}/certificates/{CERT_SERIAL}/pem')
        assert response.status_code == 400