from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.services.global_var import EMPTY_ISSUER_ID, SEQUENCE_OF_SPACE_ISSUER_ID
from certeasy_api_tests.services.start_docker_image import app_container
from certeasy_api_tests.src.config import BASE_URL
import requests


def test_should_delete_issuer_created_from_spec(app_container):
    # Create issuer ID before list them
    ISSUER_ID = create_issuer_from_spec()
    print(ISSUER_ID)
    # Delete issuer
    delete_issuer = requests.delete(url=f'{BASE_URL}issuers/{ISSUER_ID}')
    assert "h" in delete_issuer.json()
    assert delete_issuer.status_code == 204


def test_should_not_delete_a_nonexistent_issuer(app_container):
    # Create issuer ID before list them
    ISSUER_ID = create_issuer_from_spec()
    # Delete issuer
    delete_issuer = requests.delete(url=f'{BASE_URL}issuers/{ISSUER_ID}')
    assert delete_issuer.status_code == 404
    assert type(delete_issuer.json()) == dict
    assert len(delete_issuer.json()) > 1


def test_should_not_delete_an_empty_issuer_id(app_container):
    # Delete issuer
    delete_issuer = requests.delete(url=f'{BASE_URL}issuers/{EMPTY_ISSUER_ID}')
    assert delete_issuer.status_code == 422
    assert type(delete_issuer.json()) == dict
    assert len(delete_issuer.json()) > 1


def test_should_not_delete_an_sequence_of_space_issuer_id(app_container):
    # Delete issuer
    delete_issuer = requests.delete(url=f'{BASE_URL}issuers/{SEQUENCE_OF_SPACE_ISSUER_ID}')
    assert delete_issuer.status_code == 422
    assert type(delete_issuer.json()) == dict
    assert len(delete_issuer.json()) > 1
