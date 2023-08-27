from cryptography import x509
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.primitives import hashes
from datetime import datetime, timedelta


def generate_expired_certs_with_ca():
    # Generate a private key
    private_key = rsa.generate_private_key(
        public_exponent=65537,
        key_size=2048,
        backend=default_backend()
    )

    # Generate a CSR
    subject = x509.Name([
        x509.NameAttribute(x509.NameOID.COMMON_NAME, u"example.com"),
    ])
    csr = x509.CertificateSigningRequestBuilder().subject_name(subject).sign(
        private_key, hashes.SHA256(), default_backend()
    )

    # Generate an expired self-signed certificate
    issuer = subject
    cert = x509.CertificateBuilder().subject_name(subject).issuer_name(issuer).public_key(
        private_key.public_key()
    ).serial_number(x509.random_serial_number()).not_valid_before(
        datetime.utcnow() - timedelta(days=365)  # Set not_valid_before to the past
    ).not_valid_after(
        datetime.utcnow() - timedelta(days=1)  # Set not_valid_after to the past
    ).add_extension(
        x509.BasicConstraints(ca=True, path_length=None), critical=False,
    ).sign(private_key, hashes.SHA256(), default_backend())

    # Serialize private key and certificate to PEM format
    private_key_pem = private_key.private_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PrivateFormat.PKCS8,
        encryption_algorithm=serialization.NoEncryption()
    )
    cert_pem = cert.public_bytes(serialization.Encoding.PEM)

    cert_and_key_schema = {
        "cert_file": cert_pem.decode('utf-8'),
        "key_file": private_key_pem.decode('utf-8')
    }
    print(cert_and_key_schema)
    return cert_and_key_schema
