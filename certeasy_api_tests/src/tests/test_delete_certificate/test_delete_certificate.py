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


class TestDeleteCert:
    def test_should_delete_issued_employee_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_employee_certs()

        # delete cert
        response = requests.delete(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 204

       # get cert details to make sure that the cert was deleted
        get_cert_details = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert get_cert_details.status_code == 404

    def test_should_delete_issued_personal_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_personal_certs()
        # delete cert
        response = requests.delete(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 204

        # get cert details to make sure that the cert was deleted
        get_cert_details = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert get_cert_details.status_code == 404

    def test_should_delete_issued_sub_ca_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_sub_ca_certs()
        # delete cert
        response = requests.delete(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 204

        # get cert details to make sure that the cert was deleted
        get_cert_details = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert get_cert_details.status_code == 404


    def test_should_delete_issued_tls_certs(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_tls_server_certs()
        # delete cert
        response = requests.delete(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 204

        # get cert details to make sure that the cert was deleted
        get_cert_details = requests.get(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert get_cert_details.status_code == 404

    def test_should_not_delete_issued_cert_when_pass_a_sequence_of_space_cert_serial(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_tls_server_certs()
        # delete cert
        response = requests.delete(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{SEQUENCE_OF_SPACE_SERIAL_ID}')
        assert response.status_code == 400


    def test_should_not_delete_issued_cert_when_pass_a_non_existed_cert_serial(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_tls_server_certs()
        # delete cert
        NON_EXISTED_CERT_SERIAL = generate_serial_id()
        response = requests.delete(url=f'{BASE_URL}/issuers/{ISSUER_ID}/certificates/{NON_EXISTED_CERT_SERIAL}')
        assert response.status_code == 404

    def test_should_not_delete_issued_cert_when_pass_a_non_existed_issuer_id(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_tls_server_certs()
        # delete cert
        NON_EXISTED_ISSUER_ID = generate_hex_id()
        response = requests.delete(url=f'{BASE_URL}/issuers/{NON_EXISTED_ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 404

    def test_should_not_delete_issued_cert_when_pass_a_sequence_of_space_issuer_id(self, app_container):
        CERT_SERIAL, ISSUER_ID, CERT_NAME, CREATED_ISSUER = issue_tls_server_certs()
        # delete cert
        response = requests.delete(url=f'{BASE_URL}/issuers/{SEQUENCE_OF_SPACE_ISSUER_ID}/certificates/{CERT_SERIAL}')
        assert response.status_code == 404