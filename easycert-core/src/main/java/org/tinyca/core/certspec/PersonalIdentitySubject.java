package org.tinyca.core.certspec;

import org.tinyca.core.*;

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
        if(emails==null || emails.isEmpty())
            throw new IllegalArgumentException("emails set MUST not be null nor empty");

        DistinguishedName.Builder builder = DistinguishedName.builder();
        builder.append(personName);
        builder.append(address);
        builder.append(new RelativeDistinguishedName(SubjectAttributeType.TelephoneNumber, telephone));

        if(organizationBinding!=null)
            builder.append(organizationBinding);

        Set<SubjectAlternativeName> subjectAlternativeNames = new HashSet<>();
        emails.stream()
                .map(email -> new SubjectAlternativeName(SubjectAlternativeNameType.EMAIL, email))
                .forEach(subjectAlternativeNames::add);
        if(usernames!=null){
            usernames.stream()
                    .map(name -> new RelativeDistinguishedName(SubjectAttributeType.UserID,name))
                    .forEach(builder::append);

            usernames.stream()
                    .map(name -> new SubjectAlternativeName(SubjectAlternativeNameType.OTHER_NAME, name))
                    .forEach(subjectAlternativeNames::add);
        }

        this.setDistinguishedName(builder.build());
        this.setAlternativeNames(subjectAlternativeNames);

    }

}
