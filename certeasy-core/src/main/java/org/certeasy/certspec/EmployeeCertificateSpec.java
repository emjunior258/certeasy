package org.certeasy.certspec;

import org.certeasy.DateRange;
import org.certeasy.KeyStrength;

public class EmployeeCertificateSpec  extends PersonalCertificateSpec {

    public EmployeeCertificateSpec(EmployeeIdentitySubject subject, KeyStrength keyStrength, DateRange validityPeriod) {
        super(subject, keyStrength, validityPeriod);
    }

}
