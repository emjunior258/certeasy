package org.certeasy.certspec;

import org.certeasy.*;

import java.util.HashSet;
import java.util.Set;

public sealed class PersonalIdentitySubject extends CertificateSubject permits EmployeeIdentitySubject {


    public PersonalIdentitySubject(PersonName personName, GeographicAddress address, String telephone, Set<String> emails, Set<String> usernames){
        this(personName,address, telephone, emails,usernames,null);
    }

    protected PersonalIdentitySubject(PersonName personName, GeographicAddress address, String telephone, Set<String> emails, Set<String> usernames, OrganizationBinding organizationBinding) {
        if(personName==null)
            throw new IllegalArgumentException("person commonName MUST not be null");
        if(address==null)
            throw new IllegalArgumentException("address MUST not be null");

        DistinguishedName.Builder builder = DistinguishedName.builder();
        builder.append(personName);
        builder.append(address);
        builder.append(new RelativeDistinguishedName(SubjectAttributeType.TELEPHONE_NUMBER, telephone));

        if(organizationBinding!=null)
            builder.append(organizationBinding);

        Set<SubjectAlternativeName> subjectAlternativeNames = new HashSet<>();
        if(emails!= null && !emails.isEmpty()) {
            emails.stream()
                    .map(email -> new SubjectAlternativeName(SubjectAlternativeNameType.EMAIL, email))
                    .forEach(subjectAlternativeNames::add);
        }
        if(usernames!=null){
            usernames.stream()
                    .map(name -> new RelativeDistinguishedName(SubjectAttributeType.USER_ID,name))
                    .forEach(builder::append);

            usernames.stream()
                    .map(name -> new SubjectAlternativeName(SubjectAlternativeNameType.OTHER_NAME, name))
                    .forEach(subjectAlternativeNames::add);
        }

        this.setDistinguishedName(builder.build());
        this.setAlternativeNames(subjectAlternativeNames);

    }

}
