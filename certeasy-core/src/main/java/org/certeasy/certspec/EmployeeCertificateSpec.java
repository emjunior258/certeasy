package org.certeasy.certspec;

import org.certeasy.KeyStrength;

import java.time.LocalDate;

public class EmployeeCertificateSpec  extends PersonalCertificateSpec {

    public EmployeeCertificateSpec(EmployeeIdentitySubject subject, KeyStrength keyStrength, LocalDate expiryDate) {
        super(subject, keyStrength, expiryDate);
    }

}
