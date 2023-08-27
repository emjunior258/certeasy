import requests
from assertpy import assert_that
from precisely import assert_that, is_sequence
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


class TestIssuerValidation:

    @staticmethod
    def test_validate_if_the_body_response_include_the_newest_issuer(app_container):
        ISSUER_ID = create_issuer_from_spec()
        response = requests.get(url=f'{BASE_URL}/issuers')
        assert ISSUER_ID[0] == response.json()[0]['id'], f'body response {response.json}'

    @staticmethod
    def test_should_return_200(app_container):
        create_issuer_from_spec()
        response = requests.get(url=f'{BASE_URL}/issuers')
        expected_status = 200
        assert response.status_code == expected_status, f'Expected status {expected_status}, but received {response.status_code}, body response {response.json}'

    @staticmethod
    def test_validate_content_of_body_response(app_container):
        create_issuer_from_spec()
        response = requests.get(url=f'{BASE_URL}/issuers')
        assert_that(response.json()[0],
                    is_sequence("id",
                                "name",
                                "type",
                                "serial",
                                "dn",
                                "path_length",
                                "children_count")), f'body response {response.json}'

    @staticmethod
    def test_validate_type_of_issuer_created_with_path_length_equal_to_zero(app_container):
        create_issuer_from_spec()
        response = requests.get(url=f'{BASE_URL}/issuers')
        assert response.json()[0]["type"] == "ROOT"
