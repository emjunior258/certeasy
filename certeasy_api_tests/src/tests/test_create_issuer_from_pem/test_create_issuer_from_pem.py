# import requests
#
# from certeasy_api_tests.services.create_issuer_from_pem.generate_pem_cert import generate_certificate_pem
# from certeasy_api_tests.services.create_issuer_from_pem.generate_pem_json import generate_cert_and_key_schema
# from certeasy_api_tests.services.create_issuer_from_pem.generate_pem_key import generate_private_key_pem
# from certeasy_api_tests.services.start_docker_image import app_container
# from certeasy_api_tests.services.file_reader import file_reader
# from certeasy_api_tests.src.config import BASE_URL
#
#
# def test_should_create_a_new_issuer_from_spec(app_container):
#     VALID_BODY = generate_cert_and_key_schema()
#     print(VALID_BODY)
#     # Make a request to the API
#     response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=VALID_BODY)
#     # assert response.status_code == 200
#     assert 'issuer_id' in response.text
#
#
# def test():
#     cert_content = generate_certificate_pem()
#     key_content = generate_private_key_pem()
#     VALID_BODY = {
#         "cert_file": cert_content,
#         "key_file": key_content
#     }
#     # Make a request to the API
#     response = requests.post(url=f'{BASE_URL}/issuers/cert-pem', json=VALID_BODY)
#     # assert response.status_code == 200
#     assert 'issuer_id' in response.text
