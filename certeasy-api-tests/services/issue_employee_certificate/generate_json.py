from certeasy_api_tests.services.create_issuer_from_spec.generate_json_values import generate_validity
from certeasy_api_tests.services.issue_personal_certificate.generate_json import generate_email
from certeasy_api_tests.services.issue_tls_server_certificate.generate_json import generate_name, generate_key_strength, \
    generate_address
import random

def generate_user_telephone(country_codes):
    country_code = random.choice(country_codes)

    if country_code == "Mozambique":
        # Mozambique telephone numbers have the format +2588XXXXXXX
        number = "+2588" + "".join([str(random.randint(0, 9)) for _ in range(7)])
    elif country_code == "South Africa":
        # South Africa telephone numbers have the format +2783XXXXXXX
        number = "+2783" + "".join([str(random.randint(0, 9)) for _ in range(7)])
    elif country_code == "Spain":
        # Spain telephone numbers have the format +34XXXXXXXXX
        number = "+34" + "".join([str(random.randint(0, 9)) for _ in range(9)])
    elif country_code == "EUA":
        # USA telephone numbers have the format +1-XXX-XXX-XXXX
        number = "+1-" + "".join([str(random.randint(0, 9)) for _ in range(3)]) + "-" + "".join(
            [str(random.randint(0, 9)) for _ in range(3)]) + "-" + "".join(
            [str(random.randint(0, 9)) for _ in range(4)])
    else:
        number = "Country code not recognized."

    return number

def generate_employee_data(schema):
    data = {}
    country_codes = ["Mozambique", "South Africa", "Spain", "EUA"]
    user_name = generate_name(5)
    user_surname = generate_name(4)

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
            elif prop == "employment":
                data[prop] = {
                    "organization_name": "CertEasy Enterprise",
                    "department": "Technology",
                    "job_title": "QA Engineer",
                    "email_address": generate_email(user_name, user_surname),
                    "username": user_surname
                }
            else:
                data[prop] = generate_employee_data(prop_schema)
        elif isinstance(prop_schema, str):
            if prop_schema == "employee-name":
                data[prop] = user_name
            elif prop_schema == "employee-surname":
                data[prop] = user_surname
            elif prop_schema == "employee-telephone":
                data[prop] = generate_user_telephone(country_codes)
            elif prop_schema == "certificate-key-strength":
                data[prop] = generate_key_strength()


    return data


