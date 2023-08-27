incomplete_cert_pem = "-----BEGIN CERTIFICATE-----\n" \
                      "MIICyjCCAbKgAwIBAgIUFufrz//pKZwz6Dqyv4U20gUHWZYwDQYJKoZIhvcNAQEL\n"

incomplete_key_pem = "-----BEGIN PRIVATE KEY-----\n" \
                     "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDMmQlryOcq8A48\n" \
                     "J8XE9/k0PTU2O2RfIQEtatpJnTG8IZXSF1w==\n"

missing_cert_pem = "-----BEGIN CERTIFICATE-----\n" \
                   "-----END CERTIFICATE-----\n"

missing_key_pem = "-----BEGIN PRIVATE KEY-----\n" \
                  "-----END PRIVATE KEY-----\n"

missing_begin_cert_pem = "MIICyjCCAbKgAwIBAgIUFufrz//pKZwz6Dqyv4U20gUHWZYwDQYJKoZIhvcNAQEL\n" \
                         "BQAwFjEUMBIGA1UEAwwLZXhhbXBsZS5jb20wHhcNMjIwODI3MTIzMjM1WhcNMjMw\n" \
                         "ODI2MTIzMjM1WjAWMRQwEgYDVQQDDAtleGFtcGxlLmNvbTCCASIwDQYJKoZIhvcN\n" \
                         "AQEBBQADggEPADCCAQoCggEBAMyZCWvI5yrwDjzJTtkx+8IMGRO+5SicDw6eCoGk\n" \
                         "-----END CERTIFICATE-----\n"

missing_begin_key_pem = "MIICyjCCAbKgAwIBAgIUFufrz//pKZwz6Dqyv4U20gUHWZYwDQYJKoZIhvcNAQEL\n" \
                        "BQAwFjEUMBIGA1UEAwwLZXhhbXBsZS5jb20wHhcNMjIwODI3MTIzMjM1WhcNMjMw\n" \
                        "ODI2MTIzMjM1WjAWMRQwEgYDVQQDDAtleGFtcGxlLmNvbTCCASIwDQYJKoZIhvcN\n" \
                        "AQEBBQADggEPADCCAQoCggEBAMyZCWvI5yrwDjzJTtkx+8IMGRO+5SicDw6eCoGk\n" \
                        "-----END PRIVATE KEY-----\n"

invalid_base64_encoding_cert_content = "-----BEGIN CERTIFICATE-----\n" \
                            "ThisIsNotValidBase64Encoding\n" \
                            "-----END CERTIFICATE-----\n"

invalid_base64_encoding_key_content = "-----BEGIN PRIVATE KEY-----\n" \
                            "ThisIsNotValidBase64Encoding\n" \
                            "-----END PRIVATE KEY-----\n"