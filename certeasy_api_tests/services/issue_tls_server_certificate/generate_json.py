import json
import random
import string
from datetime import datetime, timedelta


def generate_random_domain(suffix=".com", length=5):
    letters = string.ascii_lowercase
    domain_name = ''.join(random.choice(letters) for _ in range(length))
    return f"{domain_name}{suffix}"


def generate_invalid_domains():
    set_invalid_domains = [".com", "domain", "domains@test.com", "domain@"]
    return random.choice(set_invalid_domains)


def generate_name(length):
    letters = string.ascii_lowercase
    return ''.join(random.choice(letters) for _ in range(length))


def generate_key_strength():
    strengths = ["LOW", "MEDIUM", "HIGH", "VERY_HIGH"]
    return random.choice(strengths)


def generate_invalid_key_strength():
    strengths = ["RSA-2048", "RSA-4096", "ECC-256", "ECC-521"]
    return random.choice(strengths)


def generate_validity():
    from_date = datetime.now().strftime("%Y-%m-%d")
    until_date = (datetime.now() + timedelta(days=random.randint(1, 365))).strftime("%Y-%m-%d")
    return from_date, until_date


def generate_address():
    countries = ['MZ', 'PT', 'ZA']
    states = ["Maputo", "Lisboa", "Cape Town"]
    localities = ["Matola", "Amadora", "Constantia"]
    streets_address = ["Patrice, Q12", "R. Elias Garcia 50", "1 Rustenburg Ave"]

    return random.choice(countries), random.choice(states), random.choice(localities), random.choice(streets_address)


def get_two_years_ago_date():
    today = datetime.now()
    two_years_ago = today - timedelta(days=365 * 2)
    return two_years_ago.strftime('%Y-%m-%d')


def generate_tls_server_data(schema):
    data = {}

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
                data[prop] = generate_tls_server_data(prop_schema)
        elif prop_schema == "certificate-key-strength":
            data[prop] = generate_key_strength()
        elif isinstance(prop_schema, str):
            if prop_schema == "server-name" or prop_schema == "org-name":
                data[prop] = generate_name(10)
            else:
                data[prop] = None
        elif isinstance(prop_schema, list):
            if prop == "domains":
                data[prop] = [generate_random_domain()]
            else:
                data[prop] = generate_tls_server_data(prop_schema)

    return data
