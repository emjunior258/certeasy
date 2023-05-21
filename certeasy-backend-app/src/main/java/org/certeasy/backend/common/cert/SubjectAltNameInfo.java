package org.certeasy.backend.common.cert;

import org.certeasy.SubjectAlternativeName;
import org.certeasy.SubjectAlternativeNameType;
import org.certeasy.SubjectAttributeType;

public record SubjectAltNameInfo(SubjectAlternativeNameType type, String value) {

    public SubjectAltNameInfo(SubjectAlternativeNameType type, String value){
        this.type = type;
        this.value = value;
    }

    public SubjectAltNameInfo(SubjectAlternativeName name){
        this(name.type(), name.value());
    }

}
