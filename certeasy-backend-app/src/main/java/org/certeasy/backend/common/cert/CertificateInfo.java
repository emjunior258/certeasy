package org.certeasy.backend.common.cert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.certeasy.ExtendedKeyUsages;
import org.certeasy.KeyUsage;

import java.util.Set;

@RegisterForReflection
public record CertificateInfo (

        String name,

        @JsonProperty("key_size")
        int keySize,

        @JsonProperty("distinguished_name")
        String distinguishedName,

        @JsonProperty("basic_constraints")
        BasicConstraintsInfo basicConstraints,

        @JsonProperty("subject_alt_names")
        Set<SubjectAltNameInfo> subjectAltNameInfos,

        @JsonProperty("key_usages")
        Set<KeyUsage> keyUsages,

        @JsonProperty("extended_key_usages")
        ExtendedKeyUsages extendedKeyUsages){

}
