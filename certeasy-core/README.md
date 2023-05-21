# certeasy-core
The core certeasy-engine.

## Core model
* You may generate certificates of the following types:
  * Personal - Certificate for personal usage
  * Employee - Corporate certificate for Employee
  * TLSServer - Certificate to Authenticate a Web server 
  * Authority - Certificate for a Certificate Authority
  * Custom - Certificate with arbitrary DN
* Request for certificates are defined as CertificateSpec.
* Each CertificateType has its own CertificateSpec class
* Certificates are issued by CertificateAuthority
* CertificateAuthority uses a CertificateGenerator to generate X509 compliant certs and stores the certificates under a CertificateRepository.


## Contributions
Yet to be documented


