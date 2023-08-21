import random


def generate_name(max_length):
    syllables = ["ab", "ac", "ad", "ba", "be", "ca", "ce", "da", "de", "do", "fa", "fi", "ga", "ge", "ha", "he", "ja",
                 "je",
                 "ka", "ke", "la", "le", "ma", "me", "na", "ne", "pa", "pe", "ra", "re", "sa", "se", "ta", "te", "va",
                 "ve",
                 "ya", "ye", "za", "ze"]
    name_length = random.randint(4, max_length)  # Provide the lower and upper limits for the name length
    name = ''.join(random.choice(syllables) for _ in range(name_length))
    issuer_name = f"issuer-{name}"
    return issuer_name

