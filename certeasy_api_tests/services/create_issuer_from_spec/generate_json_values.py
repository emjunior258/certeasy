import json
import random
from datetime import datetime, timezone
from dateutil.relativedelta import relativedelta


def generate_name(max_length):
    syllables = ["ab", "ac", "ad", "ba", "be", "ca", "ce", "da", "de", "do", "fa", "fi", "ga", "ge", "ha", "he", "ja",
                 "je",
                 "ka", "ke", "la", "le", "ma", "me", "na", "ne", "pa", "pe", "ra", "re", "sa", "se", "ta", "te", "va",
                 "ve",
                 "ya", "ye", "za", "ze"]
    name_length = random.randint(1, max_length)  # Provide the lower and upper limits for the name length
    name = ''.join(random.choice(syllables) for _ in range(name_length))
    issuer_name = f"issuer-{name}"
    return issuer_name


def generate_key_strength():
    key_strengths = ["LOW", "MEDIUM", "HIGH", "VERY_HIGH"]
    return random.choice(key_strengths)


def generate_path_length():
    path_lengths = [0, -1]
    return random.choice(path_lengths)


def generate_validity():
    current_datetime = datetime.now(timezone.utc)
    end_of_next_month = current_datetime + relativedelta(day=365)

    # format the datetime objects
    from_date = current_datetime.strftime("%Y-%m-%d")
    until_date = end_of_next_month.strftime("%Y-%m-%d")

    return from_date, until_date


def generate_address():
    countries = ['MZ', 'PT', 'ZA']
    states = ["Maputo", "Lisboa", "Cape Town"]
    localities = ["Matola", "Amadora", "Constantia"]
    streets_address = ["Patrice, Q12", "R. Elias Garcia 50", "1 Rustenburg Ave"]
    index = random.randint(0, 2)
    return countries[index], states[index], localities[index], streets_address[index]


def generate_invalid_validity_dates():
    current_datetime = datetime.now(timezone.utc)

    # format the datetime objects
    invalid_date_format = current_datetime.strftime("%Y/%m/%d")

    return invalid_date_format
