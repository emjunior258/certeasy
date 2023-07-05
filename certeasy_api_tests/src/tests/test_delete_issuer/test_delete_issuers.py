import requests
from certeasy_api_tests.services.create_issuer_from_spec.create_issuer_from_spec import create_issuer_from_spec
from certeasy_api_tests.services.generate_issuer_id import generate_issuer_id
from certeasy_api_tests.src.config import BASE_URL


def test_should_delete_issuer(app_container):
    # Create issuer ID before list them
    create_issuer_from_spec()
    # list and get issuer id
    list_issuer = requests.get(url=f'{BASE_URL}/issuers')
    issuer_id = list_issuer.json()['id']
    # Delete issuer
    delete_issuer = requests.delete(url=f'{BASE_URL}/issuers/{issuer_id}')
    assert delete_issuer.status_code == 204


def test_should_not_delete_a_nonexistent_issuer(app_container):
    # Create issuer ID before list them
    create_issuer_from_spec()
    # Delete issuer
    ISSUER_ID = generate_issuer_id
    delete_issuer = requests.delete(url=f'{BASE_URL}/issuers/{ISSUER_ID}')
    assert delete_issuer.status_code == 404
