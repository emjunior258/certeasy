package org.certeasy.backend.certs;


import io.quarkus.test.junit.QuarkusTest;
import org.certeasy.*;
import org.certeasy.backend.certs.IssuedCertType;
import org.certeasy.certspec.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class IssuedCertTypeTest {

    @Inject
    CertEasyContext context;

    Certificate issuerCert;

    GeographicAddress address;

    @BeforeEach
    void setUpIssuer(){
        address = new GeographicAddress("MZ",
                "Maputo",
                "Kampfumo",
                "AV. 25 de Setembro. Rua dos desportistas");
        CertificateAuthoritySubject certificateAuthoritySubject = new CertificateAuthoritySubject("Root",
                address);
        CertificateAuthoritySpec certificateAuthoritySpec = new CertificateAuthoritySpec(certificateAuthoritySubject, -1,
                KeyStrength.HIGH,
                new DateRange(LocalDate.of(2050, Month.OCTOBER,
                        21)));
        issuerCert = context.generator().generate(certificateAuthoritySpec);
    }


    @Test
    @DisplayName("IssuedCertType.which() must match certificate as PERSONAL")
    void certificate_must_be_matched_as_personal(){
        PersonalIdentitySubject subject = new PersonalIdentitySubject(new PersonName("John", "Doe"), address,
                "+258849901111", Set.of("john.doe@example.com"), Set.of("john.doe"));
        PersonalCertificateSpec spec = new PersonalCertificateSpec(subject,
                KeyStrength.VERY_HIGH,
                new DateRange(LocalDate.of(2050, Month.DECEMBER,
                        31)));
        Certificate certificate = context.generator().generate(spec, issuerCert);
        IssuedCertType certType = IssuedCertType.which(certificate);
        assertEquals(IssuedCertType.PERSONAL, certType);
    }

    @Test
    @DisplayName("IssuedCertType.which() must match certificate as TLS_SERVER")
    void certificate_must_be_matched_as_tls_server(){

        TLSServerSubject tlsServerSubject = new TLSServerSubject(
                "example.com",
                Set.of("example.com", "www.example.com"), address, "Example Inc"
        );

        TLSServerCertificateSpec spec = new TLSServerCertificateSpec(tlsServerSubject,
                KeyStrength.VERY_HIGH,
                new DateRange(LocalDate.of(3100, Month.DECEMBER, 31)));

        Certificate certificate = context.generator().generate(spec, issuerCert);
        IssuedCertType certType = IssuedCertType.which(certificate);
        assertEquals(IssuedCertType.TLS_SERVER, certType);

    }


    @Test
    @DisplayName("IssuedCertType.which() must match certificate as CA")
    void certificate_must_be_matched_as_ca(){

        CertificateAuthoritySubject subCaSubject = new CertificateAuthoritySubject("sub-ca",
                address);
        CertificateAuthoritySpec spec = new CertificateAuthoritySpec(subCaSubject, -1,
                KeyStrength.HIGH,
                new DateRange(LocalDate.of(2090, Month.MAY,
                        31)));

        Certificate certificate = context.generator().generate(spec, issuerCert);
        IssuedCertType certType = IssuedCertType.which(certificate);
        assertEquals(IssuedCertType.CA, certType);

    }


    @Test
    @DisplayName("IssuedCertType.which() must match certificate as EMPLOYEE")
    void certificate_must_be_matched_as_employee(){

    }

}
