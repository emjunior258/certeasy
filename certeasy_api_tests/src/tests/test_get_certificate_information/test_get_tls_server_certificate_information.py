import requests
from certeasy_api_tests.services.issue_tls_server_certificate.issue_tls_server_certificates import \
    issue_tls_server_certs
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


class TestGetCertInformation:
    def test_should_return_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        print(response.json())
        assert response.status_code == 200
        assert len(response.json()) == 11
    
    
    def test_should_return_the_correct_cert_name_after_get_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert CERT_NAME == response.json()["name"]


    def test_should_return_the_correct_cert_type_after_get_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "TLS_SERVER" == response.json()["type"]

    def test_should_include_validity_on_response_after_get_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "validity" in response.json()


    def test_should_include_key_usages_on_response_after_get_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "key_usages" in response.json()


    def test_should_include_issuer_on_response_after_get_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "issuer" in response.json()

    def test_should_include_serial_on_response_after_get_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "serial" in response.json()

    def test_should_include_key_size_on_response_after_get_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "key_size" in response.json()


    def test_should_include_distinguished_name_on_response_after_get_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "distinguished_name" in response.json()

    def test_should_include_basic_constraints_on_response_after_get_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "basic_constraints" in response.json()

    def test_should_include_extended_key_usages_on_response_after_get_information_of_issued_tls_server_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_tls_server_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "extended_key_usages" in response.json()
