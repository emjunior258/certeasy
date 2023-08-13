import requests
from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.services.create_issuer_from_spec.generate_json import generate_issuer_json
from certeasy_api_tests.services.create_issuer_from_spec.generate_json_values import generate_invalid_validity_dates
from certeasy_api_tests.services.create_issuer_from_spec.modify_issuer_spec import modify_json_values, \
    turn_empty_dict_in_json, remove_dict, \
    set_empty_dict_value, remove_dict_items
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


class TestCreateIssuerFromSpec:

    @classmethod
    def setup_class(cls):
        cls.loaded_schema = file_reader(filename='../services/create_issuer_from_spec/issuer_schema.json')

    def test_should_create_a_new_issuer_from_spec(self, app_container):
        VALID_BODY = generate_issuer_json(self.loaded_schema)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=VALID_BODY)
        assert response.status_code == 200
        assert "issuer_id" in response.json()
        assert len(response.json()) == 1

    def test_should_create_new_issuer_with_zero_path_length(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        modify_json_values(INVALID_BODY, "path_length", 0)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 200
        assert "issuer_id" in response.json()
        assert len(response.json()) == 1

    def test_should_not_allows_creation_of_issuer_with_empty_body(self, app_container):
        INVALID_BODY = {}
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_empty_name(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        modify_json_values(INVALID_BODY, "name", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_sequence_of_space_name(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        modify_json_values(INVALID_BODY, "name", "    ")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_null_name(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        modify_json_values(INVALID_BODY, "name", None)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_empty_key_strength(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        modify_json_values(INVALID_BODY, "key_strength", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_sequence_of_space_key_strength(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        modify_json_values(INVALID_BODY, "key_strength", "    ")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_null_key_strength(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        modify_json_values(INVALID_BODY, "key_strength", None)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_empty_validity_object(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        turn_empty_dict_in_json(INVALID_BODY, "validity")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_null_validity_object(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        remove_dict(INVALID_BODY, "validity")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_empty_from_date(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "validity", "from", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_sequence_of_space_from_date(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "validity", "from", "    ")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_null_from_date(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        remove_dict_items(INVALID_BODY, "validity", "from")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_empty_until_date(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "validity", "until", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_sequence_of_space_until_date(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "validity", "until", "    ")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_null_until_date(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        remove_dict_items(INVALID_BODY, "validity", "until")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_invalid_until_date(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        INVALID_DATE_FORMAT = generate_invalid_validity_dates()
        set_empty_dict_value(INVALID_BODY, "validity", "until", INVALID_DATE_FORMAT)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_invalid_from_date(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        INVALID_DATE_FORMAT = generate_invalid_validity_dates()
        set_empty_dict_value(INVALID_BODY, "validity", "from", INVALID_DATE_FORMAT)
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_empty_address_object(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        turn_empty_dict_in_json(INVALID_BODY, "address")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_null_address_object(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        remove_dict(INVALID_BODY, "address")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_empty_country(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "address", "country", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_sequence_of_space_country(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "address", "country", "     ")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_null_country(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        remove_dict_items(INVALID_BODY, "address", "country")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_empty_state(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "address", "state", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_sequence_of_space_state(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "address", "country", "    ")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_null_state(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        remove_dict_items(INVALID_BODY, "address", "state")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_empty_locality(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "address", "locality", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_sequence_of_space_locality(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "address", "locality", "     ")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_null_locality(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        remove_dict_items(INVALID_BODY, "address", "locality")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_empty_street_address(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "address", "street_address", "")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_sequence_of_space_street_address(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        set_empty_dict_value(INVALID_BODY, "address", "street_address", "     ")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5

    def test_should_not_allows_creation_of_issuer_with_null_street_address(self, app_container):
        INVALID_BODY = generate_issuer_json(self.loaded_schema)
        remove_dict_items(INVALID_BODY, "address", "street_address")
        # Make a request to the API
        response = requests.post(url=f'{BASE_URL}/issuers/cert-spec', json=INVALID_BODY)
        assert response.status_code == 422
        assert len(response.json()) == 5


