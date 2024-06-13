import requests
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.services.issue_sub_ca_certificate.issue_sub_ca_certificate import issue_sub_ca_certs
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


class TestGetCertInformation:
    def test_should_return_information_of_issued_sub_ca_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert len(response.json()) == 11

    def test_should_return_the_correct_cert_name_after_get_information_of_issued_sub_ca_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert CERT_NAME == response.json()["name"]

    def test_should_return_the_correct_cert_type_after_get_information_of_issued_sub_ca_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "CA" == response.json()["type"]

    def test_should_include_validity_on_response_after_get_information_of_issued_sub_ca_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "validity" in response.json()

    def test_should_include_key_usages_on_response_after_get_information_of_issued_sub_ca_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "key_usages" in response.json()

    def test_should_include_issuer_on_response_after_get_information_of_issued_sub_ca_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "issuer" in response.json()

    def test_should_include_serial_on_response_after_get_information_of_issued_sub_ca_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "serial" in response.json()

    def test_should_include_key_size_on_response_after_get_information_of_issued_sub_ca_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "key_size" in response.json()

    def test_should_include_distinguished_name_on_response_after_get_information_of_issued_sub_ca_certs(self,
                                                                                                          app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "distinguished_name" in response.json()

    def test_should_include_basic_constraints_on_response_after_get_information_of_issued_sub_ca_certs(self,
                                                                                                         app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "basic_constraints" in response.json()

    def test_should_include_extended_key_usages_on_response_after_get_information_of_issued_sub_ca_certs(self,
                                                                                                           app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CA_NAME = issue_sub_ca_certs()
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 200
        assert "extended_key_usages" in response.json()


    def test_should_return_information_of_issued_ca_certs(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        # get cert serial
        get_cert_serial = requests.get(url=f'{BASE_URL}/issuers')
        CERT_SERIAL = get_cert_serial.json()[0]['serial']
        CERT_NAME = get_cert_serial.json()[0]['name']
        response = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/{CERT_SERIAL}')
        print(response.text)
        assert response.status_code == 200
        assert CERT_NAME == response.json()["name"]


