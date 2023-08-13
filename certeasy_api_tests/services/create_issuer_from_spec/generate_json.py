import json
import os
from certeasy_api_tests.services.create_issuer_from_spec.generate_json_values import generate_name, generate_key_strength, generate_path_length, \
    generate_validity, \
    generate_address


def generate_issuer_json(schema):
    data = {}

    for prop, prop_schema in schema.items():
        if isinstance(prop_schema, str):
            if prop_schema == "name":
                data[prop] = generate_name(10)
            elif prop_schema == "key_strength":
                data[prop] = generate_key_strength()
            elif prop_schema == "number":
                if prop == "path_length":
                    data[prop] = generate_path_length()
            # Add more cases for other property types

        elif isinstance(prop_schema, dict):
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
                data[prop] = generate_issuer_json(prop_schema)

    return data


def load_schema(schema_path):
    absolute_schema_path = os.path.abspath(schema_path)
    with open(absolute_schema_path, "r") as schema_file:
        schema = json.load(schema_file)
    return schema


# modify_json_values(generated_json, "from", None)
# modify_json_values(generated_json, "key_strength", "     ")
# modify_dict_in_json(generated_json, "address")
# remove_key_from_dict(generated_json, "address")
# remove_dict_items(generated_json, "validity", "until")


