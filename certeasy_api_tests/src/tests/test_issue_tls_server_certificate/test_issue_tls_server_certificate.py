import requests
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.services.create_issuer_from_spec.generate_issuer_id import generate_hex_id
from certeasy_api_tests.services.create_issuer_from_spec.generate_json_values import generate_invalid_validity_dates, \
    generate_validity
from certeasy_api_tests.services.create_issuer_from_spec.modify_issuer_spec import turn_empty_dict_in_json, \
    set_empty_dict_value, modify_json_values, turn_empty_list_in_json, remove_dict, remove_json_values, \
    remove_dict_items, add_values_into_list_in_json
from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.services.issue_tls_server_certificate.generate_json import generate_tls_server_data, \
    get_two_years_ago_date, generate_invalid_key_strength, generate_invalid_domains
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


class TestIssueTLSServerCertificate:

    @classmethod
    def setup_class(cls):
        cls.loaded_schema = file_reader(
            filename='../services/issue_tls_server_certificate/issue_tls_server_certificate_schema.json')

    def test_should_issue_a_tls_certificate_using_a_new_issuer(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 200
        assert "serial" in response.json() and len(response.json()) > 0

    def test_should_not_issue_a_tls_certificate_using_a_non_existed_issuer_id(self, app_container):
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        NON_EXISTED_ISSUER = generate_hex_id()
        response = requests.post(url=f'{BASE_URL}/issuers/{NON_EXISTED_ISSUER}/certificates/tls-server',
                                 json=VALID_BODY)
        assert response.status_code == 404

    def test_should_not_issue_a_tls_certificate_when_pass_empty_validity_object(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        turn_empty_dict_in_json(VALID_BODY, "validity")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_empty_until_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "validity", "until", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_empty_from_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "validity", "from", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_empty_key_strength(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "key_strength", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_empty_address_object(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        turn_empty_dict_in_json(VALID_BODY, "address")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_empty_country(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "address", "country", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_empty_state(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "address", "state", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_empty_locality(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "address", "locality", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_empty_street_address(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "address", "street_address", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_empty_name(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "name", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_empty_domains(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        turn_empty_list_in_json(VALID_BODY, "domains")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    #
    def test_should_not_issue_a_tls_certificate_when_pass_null_validity_object(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_dict(VALID_BODY, "validity")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_null_until_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "validity", "until")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_null_from_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "validity", "from")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_null_key_strength(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_json_values(VALID_BODY, "key_strength")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_null_address_object(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_dict(VALID_BODY, "address")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_null_country(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "address", "country")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_null_state(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "address", "state")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_null_locality(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "address", "locality")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_null_street_address(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "address", "street_address")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_null_name(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_json_values(VALID_BODY, "name")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_null_domains(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_dict(VALID_BODY, "domains")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_issue_a_tls_certificate_when_pass_null_organization_name(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        remove_json_values(VALID_BODY, "organization")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 200

    def test_should_issue_a_tls_certificate_when_pass_empty_organization_name(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "organization", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_invalid_format_until_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "validity", "until", generate_invalid_validity_dates())
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_invalid_format_from_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "validity", "from", generate_invalid_validity_dates())
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_out_of_range_until_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "validity", "until", get_two_years_ago_date())
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_invalid_key_strength(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "key_strength", generate_invalid_key_strength())
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_domains_with_spaces(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "domains", "example domain com")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        print(VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_domains_with_special_characters(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "domains", "example@domain!com")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        print(VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_uppercase_domains(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "domains", "EXAMPLE.COM")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_domains_with_accents(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "domains", "certidão.com")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_missing_domain_name(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "domains", ".com")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_a_tls_certificate_when_pass_missing_domain_extension(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "domains", "domain.")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_a_tls_certificate_when_pass_missing_a_dot(self, app_container):
        """missing a dot separator between the domain name and extension"""
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_tls_server_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "domains", "missingdotcom")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/tls-server', json=VALID_BODY)
        assert response.status_code == 422