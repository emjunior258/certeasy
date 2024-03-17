import requests
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.services.create_issuer_from_spec.generate_issuer_id import generate_hex_id
from certeasy_api_tests.services.create_issuer_from_spec.generate_json_values import generate_invalid_validity_dates
from certeasy_api_tests.services.create_issuer_from_spec.modify_issuer_spec import remove_json_values, \
    modify_json_values, turn_empty_list_in_json, turn_empty_dict_in_json, set_empty_dict_value, remove_dict, \
    remove_dict_items, add_values_into_list_in_json
from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.services.issue_employee_certificate.generate_json import generate_employee_data
from certeasy_api_tests.services.issue_personal_certificate.generate_json import generate_personal_data
from certeasy_api_tests.services.issue_tls_server_certificate.generate_json import get_two_years_ago_date, \
    generate_invalid_key_strength
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


class TestIssuePersonalCertificate:

    @classmethod
    def setup_class(cls):
        cls.loaded_schema = file_reader(
            filename='../services/issue_employee_certificate/issue_employee_certificate_schema.json')

    def test_should_issue_an_employee_certificate_using_a_new_issuer(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 200
        assert "serial" in response.json() and len(response.json()) > 0

    def test_should_issue_an_employee_certificate_when_pass_null_telephone(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_json_values(VALID_BODY, "telephone")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 200
        assert "serial" in response.json() and len(response.json()) > 0


    def test_should_issue_an_employee_certificate_when_pass_null_organization_name(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "employment", "organization_name")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 200
        assert "serial" in response.json() and len(response.json()) > 0

    def test_should_issue_an_employee_certificate_when_pass_null_department(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "employment", "department")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 200
        assert "serial" in response.json() and len(response.json()) > 0


    def test_should_issue_an_employee_certificate_when_pass_null_email_address(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "employment", "email_address")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 200
        assert "serial" in response.json() and len(response.json()) > 0


    def test_should_issue_an_employee_certificate_when_pass_null_username(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "employment", "username")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 200
        assert "serial" in response.json() and len(response.json()) > 0


    def test_should_not_issue_an_employee_certificate_when_pass_null_job_title(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "employment", "job_title")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_null_employment_object(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict(VALID_BODY, "employment")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_null_validity_object(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict(VALID_BODY, "validity")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_null_until_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "validity", "until")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_null_from_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "validity", "from")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_null_key_strength(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_json_values(VALID_BODY, "key_strength")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_null_address_object(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict(VALID_BODY, "address")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_null_country(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "address", "country")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_null_state(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "address", "state")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_null_locality(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "address", "locality")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_null_street_address(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_dict_items(VALID_BODY, "address", "street_address")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_null_name(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_json_values(VALID_BODY, "name")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_null_surname(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        remove_json_values(VALID_BODY, "surname")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    #
    def test_should_issue_an_employee_certificate_when_pass_an_empty_telephone(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "telephone", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_employment_object(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        turn_empty_dict_in_json(VALID_BODY, "employment")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_issue_an_employee_certificate_when_pass_an_empty_organization_name(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "employment", "organization_name", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_issue_an_employee_certificate_when_pass_an_empty_department(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "employment", "department", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_issue_an_employee_certificate_when_pass_an_empty_email_address(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "employment", "email_address", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422
    
    def test_should_issue_an_employee_certificate_when_pass_an_empty_username(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "employment", "username", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_job_title(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "employment", "job_title", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_validity_object(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        turn_empty_dict_in_json(VALID_BODY, "validity")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_until_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "validity", "until", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_from_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "validity", "from", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_key_strength(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "key_strength", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_address_object(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        turn_empty_dict_in_json(VALID_BODY, "address")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_country(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "address", "country", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_state(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "address", "state", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_locality(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "address", "locality", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_street_address(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "address", "street_address", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_name(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "name", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_not_issue_an_employee_certificate_when_pass_an_empty_surname(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "surname", "")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422
    
    # 
    def test_should_issue_an_employee_certificate_when_pass_a_sequence_of_space_email_address(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "employment", "email_address", "       ")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_email_address_spaces(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_personal_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "employment", "example domain com")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_email_address_missing_domain(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_personal_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "employment", "no.domain@")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_email_address_special_characters(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_personal_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "employment", "example@domain!com")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_email_address_missing_symbol(self, app_container):
        """missing "@" symbol"""
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_personal_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "employment", "examplegmail.com")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_issue_an_employee_certificate_when_pass_uppercase_email_address(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_personal_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "employment", "EXAMPLE@GMAIL.COM")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 200

    def test_should_not_issue_an_employee_certificate_when_pass_email_address_accents(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_personal_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "employment", "certidão@gmail.com")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_an_email_missing_top_level_domain(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_personal_data(self.loaded_schema)
        add_values_into_list_in_json(VALID_BODY, "employment", "invalid.email@domain")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_invalid_key_strength(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY =  generate_personal_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "key_strength", generate_invalid_key_strength())
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422


    def test_should_issue_an_employee_certificate_when_pass_an_invalid_telephone_data(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "telephone", "INVALID-TELEPHONE")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_issue_an_employee_certificate_when_pass_an_invalid_telephone(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_employee_data(self.loaded_schema)
        modify_json_values(VALID_BODY, "telephone", "+2588499o2432")
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422
    
    def test_should_not_issue_an_employee_certificate_when_pass_invalid_format_until_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY = generate_personal_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "validity", "until", generate_invalid_validity_dates())
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_invalid_format_from_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY =  generate_personal_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "validity", "from", generate_invalid_validity_dates())
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422

    def test_should_not_issue_an_employee_certificate_when_pass_out_of_range_until_validity(self, app_container):
        ISSUER_ID = create_issuer_from_spec()
        VALID_BODY =  generate_personal_data(self.loaded_schema)
        set_empty_dict_value(VALID_BODY, "validity", "until", get_two_years_ago_date())
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID[0]}/certificates/employee', json=VALID_BODY)
        assert response.status_code == 422