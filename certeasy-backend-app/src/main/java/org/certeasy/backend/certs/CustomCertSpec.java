package org.certeasy.backend.certs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.certeasy.*;
import org.certeasy.backend.common.BaseCertSpec;
import org.certeasy.backend.common.cert.BasicConstraintsInfo;
import org.certeasy.backend.common.validation.ValidationPath;
import org.certeasy.backend.common.validation.Violation;
import org.certeasy.backend.common.validation.ViolationType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomCertSpec extends BaseCertSpec {

    private static final String DISTINGUISHED_NAME_ATTR = "subject_distinguished_name";
    private static final String ALTERNATIVE_NAMES_ATTR = "subject_alt_names";

    @JsonProperty(DISTINGUISHED_NAME_ATTR)
    private String distinguishedName;

    @JsonProperty("key_usage")
    private String[] keyUsage;

    @JsonProperty("extended_key_usage")
    private ExtendedKeyUsagesInfo extendedKeyUsage;

    @JsonProperty("subject_alt_names")
    private AlternativeNameInfo[] alternativeNameInfos;

    @JsonProperty("basic_constraints")
    private BasicConstraintsInfo basicConstraints;


    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String[] getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(String[] keyUsage) {
        this.keyUsage = keyUsage;
    }

    public ExtendedKeyUsagesInfo getExtendedKeyUsage() {
        return extendedKeyUsage;
    }

    public void setExtendedKeyUsage(ExtendedKeyUsagesInfo extendedKeyUsage) {
        this.extendedKeyUsage = extendedKeyUsage;
    }

    public AlternativeNameInfo[] getAlternativeNameInfos() {
        return alternativeNameInfos;
    }

    public void setAlternativeNameInfos(AlternativeNameInfo[] alternativeNameInfos) {
        this.alternativeNameInfos = alternativeNameInfos;
    }

    public BasicConstraintsInfo getBasicConstraints() {
        return basicConstraints;
    }

    public void setBasicConstraints(BasicConstraintsInfo basicConstraints) {
        this.basicConstraints = basicConstraints;
    }

    public Set<SubjectAlternativeName> getSubjectAlternativeNameSet() {
        return Stream.of(alternativeNameInfos).map(AlternativeNameInfo::toAlternativeName
            ).collect(Collectors.toSet());
    }

    public Set<KeyUsage> getKeyUsageSet(){
        return Stream.of(keyUsage).map(KeyUsage::valueOf)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Violation> validate(ValidationPath path) {
        Set<Violation> violations = new HashSet<>(super.validate(path));
        if(basicConstraints != null)
            basicConstraints.validate(path.append("basic_constraints"));
        if(distinguishedName==null || distinguishedName.isBlank())
            violations.add(new Violation(path, DISTINGUISHED_NAME_ATTR, ViolationType.REQUIRED,
                    "subject distinguished name must not be null nor empty"));
        try {
            DistinguishedName.builder().parse(distinguishedName, true);
        }catch (IllegalArgumentException ex){
            violations.add(new Violation(path, DISTINGUISHED_NAME_ATTR, ViolationType.PATTERN,
                    "subject distinguished name does not match pattern"));
        }

        if(keyUsage != null){
            for(byte i=0; i < keyUsage.length; i++){
                String item = keyUsage[i];
                try{
                    KeyUsage.valueOf(item);
                }catch (IllegalArgumentException ex){
                    violations.add(new Violation(path.append("key_usage"),String.format("[%d]",i),ViolationType.ENUM, String.format("key usage name type MUST be one of %s",
                            Arrays.toString(KeyUsage
                                    .values()))));
                }
            }
        }

        if(extendedKeyUsage != null)
            violations.addAll(extendedKeyUsage.validate(path.append("extended_key_usage")));

        if(alternativeNameInfos != null && alternativeNameInfos.length > 0){
            for(byte i = 0; i < alternativeNameInfos.length; i++){
                AlternativeNameInfo nameInfo = alternativeNameInfos[i];
                String nameType = nameInfo.type();
                try{
                    SubjectAlternativeNameType.valueOf(nameType);
                }catch (IllegalArgumentException exception){
                    violations.add(new Violation(path.append(ALTERNATIVE_NAMES_ATTR),
                            String.format("[%d]", i),
                            ViolationType.ENUM, String.format("alternative name type MUST be one of %s",
                                Arrays.toString(SubjectAlternativeNameType
                                        .values()))));
                }
                String nameValue = nameInfo.value();
                if(nameValue == null || nameValue.isBlank())
                    violations.add(new Violation(path.append(ALTERNATIVE_NAMES_ATTR),
                            String.format("[%d]", i),
                            ViolationType.REQUIRED, "alternative name value MUST not be null nor empty"));
            }
        }
        return violations;
    }

    public CertificateSpec toCertificateSpec() {
        DistinguishedName distinguishedName = DistinguishedName.builder().parse(this.getDistinguishedName()).build();
        CertificateSubject subject = new CertificateSubject(distinguishedName,
                this.getSubjectAlternativeNameSet());

        ExtendedKeyUsages extendedKeyUsages = null;
        ExtendedKeyUsagesInfo extendedKeyUsagesInfo = this.getExtendedKeyUsage();
        if(extendedKeyUsagesInfo != null)
            extendedKeyUsages = extendedKeyUsagesInfo.toExtendedKeyUsages();

        return new CertificateSpec(subject, KeyStrength.valueOf(this.getKeyStrength()),
                this.getValidity().toDateRange(),
                basicConstraints != null ? basicConstraints.toBasicConstraints() : new BasicConstraints(false, 0),
                this.getKeyUsageSet(), extendedKeyUsages);
    }

}
