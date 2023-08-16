import secrets


def generate_hex_id(length=20):
    random_id = secrets.token_hex(length)
    return random_id
