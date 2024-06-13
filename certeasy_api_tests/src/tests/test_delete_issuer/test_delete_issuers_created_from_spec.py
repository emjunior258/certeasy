import requests
import pytest
from certeasy_api_tests.services.create_issuer_from_pem.create_issuer_from_pem import create_issuer_from_pem
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.services.create_issuer_from_spec.generate_issuer_id import generate_hex_id
from certeasy_api_tests.services.global_var import EMPTY_ISSUER_ID, SEQUENCE_OF_SPACE_ISSUER_ID
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container

class TestIssuerDeletion:

    @staticmethod
    @pytest.mark.dependency(name="create_spec_issuer")
    def test_should_delete_issuer_created_from_spec(app_container):
        ISSUER_ID = create_issuer_from_spec()
        delete_issuer = requests.delete(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}')
        assert delete_issuer.status_code == 204

    @staticmethod
    @pytest.mark.dependency(name="create_pem_issuer")
    def test_should_delete_issuer_created_from_pem(app_container):
        ISSUER_ID = create_issuer_from_pem()
        delete_issuer = requests.delete(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}')
        assert delete_issuer.status_code == 204

    @staticmethod
    def test_should_not_delete_a_nonexistent_issuer(app_container):
        NON_EXISTED_ISSUER_ID = generate_hex_id()
        delete_issuer = requests.delete(url=f'{BASE_URL}/issuers/{NON_EXISTED_ISSUER_ID}')
        assert delete_issuer.status_code == 404
        response_data = delete_issuer.json()
        assert isinstance(response_data, dict)
        assert len(response_data) > 1

    @staticmethod
    def test_should_not_delete_an_empty_issuer_id(app_container):
        delete_issuer = requests.delete(url=f'{BASE_URL}/issuers/{EMPTY_ISSUER_ID}')
        assert delete_issuer.status_code == 405

    @staticmethod
    def test_should_not_delete_an_sequence_of_space_issuer_id(app_container):
        delete_issuer = requests.delete(url=f'{BASE_URL}/issuers/{SEQUENCE_OF_SPACE_ISSUER_ID}')
        assert delete_issuer.status_code == 400
        response_data = delete_issuer.json()
        assert isinstance(response_data, dict)
        assert len(response_data) > 1
