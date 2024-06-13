import requests
from assertpy import assert_that
from precisely import assert_that, is_sequence
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.services.issue_personal_certificate.issue_personal_certificates import \
    issue_personal_certs
from certeasy_api_tests.services.issue_sub_ca_certificate.issue_sub_ca_certificate import issue_sub_ca_certs
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


class TestListSubCAlIssuedCerts:
    def test_should_return_details_of_issued_personal_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        PARAMS = {"type": "CA"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert response.status_code == 200

    def test_should_return_personal_as_cert_type_when_get_issued_tls_server(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        PARAMS = {"type": "PERSONAL"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert len(response.json()) == 0

    def test_should_not_return_issued_tls_server_details_when_pass_personal_type(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        PARAMS = {"type": "TLS_SERVER"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert len(response.json()) == 0

    def test_should_return_the_correct_ca_name_associated_with_issued_sub_ca_when_pass_ca_type(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        PARAMS = {"type": "CA"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        print(response.json())
        assert len(response.json()) == 2 and response.json()[1]["name"] == CA_NAME

    def test_should_not_return_issued_personal_details_when_pass_employee_type(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        PARAMS = {"type": "EMPLOYEE"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert len(response.json()) == 0

    def test_should_not_return_issued_personal_details_when_pass_custom_type(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        PARAMS = {"type": "CUSTOM"}
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates', params=PARAMS)
        assert len(response.json()) == 0
