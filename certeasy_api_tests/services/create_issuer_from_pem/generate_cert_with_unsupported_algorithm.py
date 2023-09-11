from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import ec


def generate_private_key_with_unsupported_algorithm():
    # Generate a private key using an unsupported algorithm (e.g., elliptic curve)
    private_key = ec.generate_private_key(
        curve=ec.SECP256K1(),  # Use an unsupported curve
        backend=default_backend()
    )

    # Serialize private key to PEM format
    private_key_pem = private_key.private_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PrivateFormat.PKCS8,
        encryption_algorithm=serialization.NoEncryption()
    )

    cert_and_key_schema = {
        "cert_file": "valid_certificate_pem_content",
        "key_file": private_key_pem.decode('utf-8')
    }
    return cert_and_key_schema
