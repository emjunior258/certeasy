package org.certeasy.backend.common.cert;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.SubjectAlternativeName;
import org.certeasy.SubjectAlternativeNameType;


@RegisterForReflection
public record SubjectAltNameInfo(SubjectAlternativeNameType type, String value) {

    public SubjectAltNameInfo(SubjectAlternativeNameType type, String value){
        this.type = type;
        this.value = value;
    }

    public SubjectAltNameInfo(SubjectAlternativeName name){
        this(name.type(), name.value());
    }

}
