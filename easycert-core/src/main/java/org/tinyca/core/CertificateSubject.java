package org.tinyca.core;

import org.tinyca.core.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CertificateSubject {

    private DistinguishedName distinguishedName;
    private Set<SubjectAlternativeName> alternativeNames = new HashSet<>();


    public CertificateSubject(){
    }

    public CertificateSubject(DistinguishedName distinguishedName){
        this(distinguishedName, null);
    }

    public CertificateSubject(DistinguishedName distinguishedName, Set<SubjectAlternativeName> alternativeNames){
        if(distinguishedName==null)
            throw new IllegalArgumentException("distinguished commonName MUST not be null");
        this.distinguishedName = distinguishedName;
        if(alternativeNames!=null)
            this.alternativeNames = Collections.unmodifiableSet(alternativeNames);
    }


    protected void setDistinguishedName(DistinguishedName distinguishedName){
        this.distinguishedName = distinguishedName;
    }

    protected void setAlternativeNames(Set<SubjectAlternativeName> alternativeNames){
        this.alternativeNames = Collections.unmodifiableSet(alternativeNames);
    }

    public Set<SubjectAlternativeName> getAlternativeNames(){
        return this.alternativeNames;
    }

    public DistinguishedName getDistinguishedName(){
        return this.distinguishedName;
    }


    public String getCommonName(){
        return this.distinguishedName.getCommonName();
    }

    public boolean hasAlternativeNames(){

        return this.alternativeNames!=null && !alternativeNames.isEmpty();

    }



}
