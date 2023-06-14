import random
import string


def generate_issuer_id():
    characters = list(string.ascii_lowercase)
    char = random.choices(characters, k=4)
    issuer_id = (''.join(char)) + '-issuer'
    return issuer_id
