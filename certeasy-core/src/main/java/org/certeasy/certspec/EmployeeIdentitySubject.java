package org.certeasy.certspec;

import org.certeasy.GeographicAddress;

import java.util.Set;


public final class EmployeeIdentitySubject extends PersonalIdentitySubject {

    public EmployeeIdentitySubject(PersonName personName, GeographicAddress address, String telephone, String email, String username, OrganizationBinding organizationBinding) {
        super(personName, address, telephone, email!=null? Set.of(email) : null, username!=null? Set.of(username) : null, organizationBinding);
    }

}
