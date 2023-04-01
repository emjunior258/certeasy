package org.certeasy.certspec;

import org.certeasy.RDNConvertible;
import org.certeasy.RelativeDistinguishedName;
import org.certeasy.SubjectAttributeType;

import java.util.Set;

public record PersonName(String givenName, String surname) implements RDNConvertible {

    public PersonName(String givenName, String surname){
        if(givenName==null || givenName.isEmpty())
            throw new IllegalArgumentException("given commonName MUST not be null nor empty");
        if(surname==null || surname.isEmpty())
            throw new IllegalArgumentException("surname MUST not be null nor empty");
        this.givenName = givenName;
        this.surname = surname;
    }

    public String fullname(){
        return String.format("%s %s", givenName, surname);
    }


    public String initials(){
        StringBuilder builder = new StringBuilder();
        String[] parts = this.fullname().toUpperCase().trim().split(" ");
        for(String part: parts)
            builder.append(part.charAt(0));
        return builder.toString();
    }

    @Override
    public Set<RelativeDistinguishedName> toRdns() {
        return Set.of(
                new RelativeDistinguishedName(SubjectAttributeType.Initials,initials()),
                new RelativeDistinguishedName(SubjectAttributeType.CommonName, fullname()),
                new RelativeDistinguishedName(SubjectAttributeType.GivenName, givenName),
                new RelativeDistinguishedName(SubjectAttributeType.Surname, surname));
    }

}
