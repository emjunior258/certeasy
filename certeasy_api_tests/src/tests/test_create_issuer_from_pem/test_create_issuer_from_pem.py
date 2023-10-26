import requests
from certeasy_api_tests.services.create_issuer_from_pem.generate_cert_with_unsupported_algorithm import \
    generate_private_key_with_unsupported_algorithm
from certeasy_api_tests.services.create_issuer_from_pem.generate_certs_with_no_CA import generate_certs_with_no_ca
from certeasy_api_tests.services.create_issuer_from_pem.generate_expired_pem import generate_expired_certs_with_ca
from certeasy_api_tests.services.create_issuer_from_pem.generate_pem_json import generate_valid_certs_with_ca
from certeasy_api_tests.services.create_issuer_from_pem.invalid_pem_content import incomplete_cert_pem, \
    incomplete_key_pem, missing_cert_pem, missing_key_pem, missing_begin_cert_pem, missing_begin_key_pem, \
    invalid_base64_encoding_cert_content, invalid_base64_encoding_key_content
from certeasy_api_tests.services.create_issuer_from_spec.modify_issuer_spec import modify_json_values, \
    remove_json_values
from certeasy_api_tests.services.start_docker_image import app_container
from certeasy_api_tests.src.config import BASE_URL


class TestIssuerCreationFromPEM:

    def test_should_create_a_new_issuer_from_pem(self, app_container):
        VALID_BODY = generate_valid_certs_with_ca()
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=VALID_BODY)
        print("Test body", VALID_BODY)
        print("Test response", response.json())
        assert response.status_code == 200
        assert 'issuer_id' in response.text

    def test_should_not_create_issuer_from_pem_with_existed_cert_details(self, app_container):
        global response
        VALID_BODY = generate_valid_certs_with_ca()
        for i in range(2):
            # Make a request to the API
            response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=VALID_BODY)
        assert response.status_code == 409

    def test_should_not_create_issuer_from_pem_with_null_cert(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        remove_json_values(INVALID_BODY, "cert_file")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_null_key_file(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        remove_json_values(INVALID_BODY, "key_file")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_empty_key_file_and_cert(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "cert_file", "")
        modify_json_values(INVALID_BODY, "key_file", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_null_key_file_and_cert(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        remove_json_values(INVALID_BODY, "cert_file", )
        remove_json_values(INVALID_BODY, "key_file")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_empty_cert_file(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "cert_file", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_empty_key_fule(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "key_file", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_cert_file_without_not_CA_basic_contraint(self, app_container):
        INVALID_BODY = generate_certs_with_no_ca()
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_expired_pem(self, app_container):
        INVALID_BODY = generate_expired_certs_with_ca()
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_invalid_pem_key(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "key_file", "invalid key")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_invalid_cert(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "cert_file", "invalid key")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_uncompleted_cert(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "cert_file", incomplete_cert_pem)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_uncompleted_key(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "key_file", incomplete_key_pem)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_missing_cert_content(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "key_file", missing_cert_pem)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_missing_key_content(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "key_file", missing_key_pem)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_missing_begin_cert(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "key_file", missing_begin_cert_pem)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_missing_begin_key(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "key_file", missing_begin_key_pem)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_cert_generated_with_unsupported_algorithm(self,
                                                                                                    app_container):
        INVALID_BODY = generate_private_key_with_unsupported_algorithm()
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_incorrect_base_64_encoding_cert(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "key_file", invalid_base64_encoding_cert_content)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422

    def test_should_not_create_a_new_issuer_from_pem_with_incorrect_base_64_encoding_key(self, app_container):
        INVALID_BODY = generate_valid_certs_with_ca()
        modify_json_values(INVALID_BODY, "key_file", invalid_base64_encoding_key_content)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=INVALID_BODY)
        assert response.status_code == 422
