from certeasy_api_tests.services.create_issuer_from_spec.generate_json_values import generate_validity
from certeasy_api_tests.services.issue_tls_server_certificate.generate_json import generate_name, generate_key_strength, \
    generate_address

def generate_sub_ca_data(schema):
    data = {}
    user_name = generate_name(5)

    for prop, prop_schema in schema.items():
        if isinstance(prop_schema, dict):
            if prop == "validity":
                from_date, until_date = generate_validity()
                data[prop] = {
                    "from": from_date,
                    "until": until_date
                }
            elif prop == "address":
                country, state, locality, street_address = generate_address()
                data[prop] = {
                    "country": country,
                    "state": state,
                    "locality": locality,
                    "street_address": street_address
                }
            elif prop == "organization":
                data[prop] = {
                    "organization_name": "CertEasy Enterprise",
                    "organization_unit": "Technology"
                }
            else:
                data[prop] = generate_sub_ca_data(prop_schema)

        elif isinstance(prop_schema, str):
            if prop_schema == "my-sub-ca-name":
                data[prop] = f"my-sub-ca-{user_name}"
            elif prop_schema == "certificate-key-strength":
                data[prop] = generate_key_strength()
        else:
            data[prop] = generate_sub_ca_data(prop_schema)

    return data


