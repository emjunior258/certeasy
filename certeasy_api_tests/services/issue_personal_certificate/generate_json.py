import json
import random
import string
from datetime import datetime, timedelta

from certeasy_api_tests.services.issue_tls_server_certificate.generate_json import generate_validity, generate_address, \
    generate_key_strength, generate_name
import random


def generate_email(username, surname):
    domains = ["example.com", "test.com", "sample.com", "domain.com"]  # Add more domains as needed
    domain = random.choice(domains)
    email = username + surname + "@" + domain
    return email


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



def generate_personal_data(schema):
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
            else:
                data[prop] = generate_personal_data(prop_schema)
        elif prop_schema == "certificate-key-strength":
            data[prop] = generate_key_strength()
        elif isinstance(prop_schema, str):
            # Process string properties
            if prop_schema == "user-name":
                data[prop] = user_name
            elif prop_schema == "user-surname":
                data[prop] = user_surname
            elif prop_schema == "user-telephone":
                data[prop] = generate_user_telephone(country_codes)
            # Add more conditions for other string properties if needed
            else:
                data[prop] = None
        elif isinstance(prop_schema, list):
            # Process list properties
            if prop == "email_addresses":
                data[prop] = [generate_email(user_name, user_surname)]
            elif prop == "usernames":
                data[prop] = [user_surname]
            # Add more conditions for other list properties if needed
            else:
                # Assuming you want to recursively process the list elements
                data[prop] = [generate_personal_data(item) for item in prop_schema]

    return data
