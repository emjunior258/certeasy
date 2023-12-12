import requests
from certeasy_api_tests.services.issue_employee_certificate.issue_employee_certificate import issue_employee_certs
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


class TestListEmployeeIssuedCerts:
    def test_should_return_details_of_issued_employee_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_employee_certs()
        PARAMS = {"type": "EMPLOYEE"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert response.status_code == 200

    def test_should_return_employee_as_cert_type_when_get_issued_tls_server(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_employee_certs()
        PARAMS = {"type": "EMPLOYEE"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert response.json()[0]["type"] == "EMPLOYEE"

    def test_should_return_correct_cert_name_when_get_issued_tls_server(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_employee_certs()
        PARAMS = {"type": "EMPLOYEE"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert response.json()[0]["name"] == CERT_NAME

    def test_should_return_correct_cert_serial_when_get_issued_tls_server(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_employee_certs()
        PARAMS = {"type": "EMPLOYEE"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert response.json()[0]["serial"] == CERT_SERIAL

    def test_should_not_return_issued_tls_server_details_when_pass_employee_type(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_employee_certs()
        PARAMS = {"type": "TLS_SERVER"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert len(response.json()) == 0

    def test_should_return_the_correct_ca_name_associated_with_issued_tls_when_pass_ca_type(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_employee_certs()
        PARAMS = {"type": "CA"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert len(response.json()) == 1 and response.json()[0]["name"] == CA_NAME

    def test_should_return_issued_employee_details_when_pass_employee_type(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_employee_certs()
        PARAMS = {"type": "EMPLOYEE"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert len(response.json()) == 1

    def test_should_not_return_issued_employee_details_when_pass_custom_type(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_employee_certs()
        PARAMS = {"type": "CUSTOM"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert len(response.json()) == 0
