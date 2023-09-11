def modify_json_values(json_data, key, value):
    if key in json_data:
        json_data[key] = value


def remove_json_values(json_data, key):
    if key in json_data:
        del json_data[key]


def turn_empty_dict_in_json(json_data, key):
    if key in json_data and isinstance(json_data[key], dict):
        json_data[key] = {}


def remove_dict(json_data, key_to_remove):
    if key_to_remove in json_data:
        del json_data[key_to_remove]


def remove_dict_items(json_data, dict_key, second_key):
    if dict_key in json_data and isinstance(json_data[dict_key], dict):
        if second_key in json_data[dict_key]:
            del json_data[dict_key][second_key]


def set_empty_dict_value(json_data, dict_key, second_key, value):
    if dict_key in json_data and isinstance(json_data[dict_key], dict):
        if second_key in json_data[dict_key]:
            json_data[dict_key][second_key] = value
