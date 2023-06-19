import requests
from certeasy_api_tests.services.file_reader import file_reader
from certeasy_api_tests.services.generate_issuer_id import generate_issuer_id
from certeasy_api_tests.src.config import BASE_URL
from certeasy_api_tests.services.start_docker_image import app_container


def test_should_create_a_new_issuer_from_spec(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 204


def test_should_create_new_issuer_with_zero_path_length(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec_with_zero_path_length.json')
    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 204


def test_should_not_allows_creation_of_issuer_with_empty_issuer_id(app_container):
    ISSUER_ID = ''
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 405


def test_should_not_allows_creation_of_issuer_with_sequence_of_space_issuer_id(app_container):
    ISSUER_ID = '     '
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_existed_issuer_id(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    response_status = int
    # Make a request to the API
    for i in range(2):
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
        response_status = response.status_code
    assert response_status == 409


def test_should_not_allows_creation_of_issuer_with_empty_body(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/valid_spec.json')
    response_status = int
    # Make a request to the API
    for i in range(2):
        response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
        response_status = response.status_code
    assert response_status == 409


def test_should_not_allows_creation_of_issuer_with_empty_name(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/name/empty_name.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_sequence_of_space_name(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/name/sequence_of_space_name.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_null_name(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/name/null_name.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_empty_strength(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/key_strength/empty_key_strength.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_sequence_of_space_strength(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/key_strength/sequence_of_space_key_strength.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_null_strength(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/key_strength/null_key_strength.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_empty_validity_object(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/validity/empty_validity.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_null_validity_object(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/validity/null_validity.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_empty_from_date(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/validity/empty_from_date.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_sequence_of_space_from_date(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/validity/sequence_of_space_from_date.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_null_from_date(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/validity/null_from_date.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_empty_until_date(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/validity/empty_until_date.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_sequence_of_space_until_date(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/validity/sequence_of_space_until_date.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_null_until_date(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/validity/null_until_date.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_invalid_until_date(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/validity/invalid_until_date.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_invalid_from_date(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/validity/invalid_from_date.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_empty_address_object(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/address/empty_address.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_null_address_object(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/address/null_address.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_empty_country(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/address/empty_country.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_sequence_of_space_country(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/address/sequence_of_space_country.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_null_country(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/address/empty_country.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_empty_state(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/address/empty_state.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_sequence_of_space_state(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/address/sequence_of_space_state.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_null_state(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/address/null_state.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_empty_locality(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/address/empty_locality.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_sequence_of_space_locality(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/address/sequence_of_space_locality.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_null_locality(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/address/null_locality.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_empty_street_address(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/address/empty_street_address.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_sequence_of_space_street_address(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(
        filename='../services/data/create_issuer/specs/invalid_body/address/sequence_of_space_address.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422


def test_should_not_allows_creation_of_issuer_with_null_street_address(app_container):
    ISSUER_ID = generate_issuer_id()
    VALID_BODY = file_reader(filename='../services/data/create_issuer/specs/invalid_body/address/null_street_address.json')

    # Make a request to the API
    response = requests.post(url=f'{BASE_URL}/issuers/{ISSUER_ID}/cert-spec', json=VALID_BODY)
    assert response.status_code == 422