package org.easycert;


/**
 * Represents an Attribute that may be present in a {@link Certificate} subject's {@link DistinguishedName}.
 * Each attribute has a unique identifier denominated OID.
 * Some attributes support multiple values, meaning they can be repeated, meaning they might appear in multiple {@link RelativeDistinguishedName}s of the same
 * {@link DistinguishedName} with different values.
 *
 */
public enum SubjectAttributeType {

    CommonName("commonName", "2.5.4.3"),
    Surname("surname", "2.5.4.4"),
    CountryName("countryName", "2.5.4.6"),
    Locality("localityName", "2.5.4.7"),
    Province("stateOrProvinceName", "2.5.4.8"),
    Street("streetAddress", "2.5.4.9"),
    OrganizationName("organizationName", "2.5.4.10"),
    OrganizationUnit("organizationalUnit", "2.5.4.11", true),
    Title("title", "2.5.4.12"),
    Initials("initials", "2.5.4.43"),
    GivenName("givenName", "2.5.4.42", true),
    TelephoneNumber("telephoneNumber","2.5.4.20"),
    UserID("userID", "0.9.2342.19200300.100.1.1", true),
    DomainComponent("domainComponent", "0.9.2342.19200300.100.1.25", true);

    private String description;

    private String oid;

    private boolean multiValue = false;

    SubjectAttributeType(String description, String oid) {
        this(description,oid,false);
    }

    SubjectAttributeType(String description, String oid, boolean multiValue) {
        this.description = description;
        this.oid = oid;
        this.multiValue = multiValue;
    }

    public String getDescription() {
        return description;
    }

    public String getOid() {
        return oid;
    }

    public boolean isMultiValue() {
        return multiValue;
    }
}
