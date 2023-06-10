package org.certeasy;


/**
 * Represents an Attribute that may be present in a {@link Certificate} subject's {@link DistinguishedName}.
 * Each attribute has a unique identifier denominated OID.
 * Some attributes support multiple values, meaning they can be repeated, meaning they might appear in multiple {@link RelativeDistinguishedName}s of the same
 * {@link DistinguishedName} with different values.
 *
 */
public enum SubjectAttributeType {

    CommonName("CN", "commonName", "2.5.4.3", Integer.MAX_VALUE),
    Title("T", "title", "2.5.4.12", Integer.MAX_VALUE - 1),
    Initials("initials", "initials", "2.5.4.43", Integer.MAX_VALUE - 2),
    GivenName("G", "givenName", "2.5.4.42",  Integer.MAX_VALUE - 3, true),
    Surname("SN", "surname", "2.5.4.4", Integer.MAX_VALUE - 4),
    TelephoneNumber("telephoneNumber", "telephoneNumber","2.5.4.20", Integer.MAX_VALUE - 5),
    UserID( "UID", "userID", "0.9.2342.19200300.100.1.1", Integer.MAX_VALUE - 6, true),
    OrganizationName("O",  "organizationName", "2.5.4.10", Integer.MAX_VALUE - 7),
    OrganizationUnit("OU", "organizationalUnit", "2.5.4.11", Integer.MAX_VALUE - 8, true),
    CountryName("C", "countryName", "2.5.4.6", Integer.MAX_VALUE - 9),
    Province("ST", "stateOrProvinceName", "2.5.4.8", Integer.MAX_VALUE - 10),
    Locality("L", "localityName", "2.5.4.7", Integer.MAX_VALUE - 11),
    Street("STREET", "streetAddress", "2.5.4.9", Integer.MAX_VALUE - 12),
    DomainComponent("DC", "domainComponent", "0.9.2342.19200300.100.1.25",  Integer.MAX_VALUE - 13, true);

    private String mnemonic;

    private String description;

    private String oid;

    private boolean multiValue = false;

    private int priority = 0;

    SubjectAttributeType(String mnemonic, String description, String oid, int priority) {
        this( mnemonic, description,oid, priority, false);
    }

    SubjectAttributeType(String mnemonic, String description, String oid, int priority, boolean multiValue) {
        this.mnemonic = mnemonic;
        this.description = description;
        this.oid = oid;
        this.priority = priority;
        this.multiValue = multiValue;
    }

    public String getMnemonic() {
        return mnemonic;
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

    public int getPriority() {
        return this.priority;
    }

    public static SubjectAttributeType fromOID(String oid){
        for(SubjectAttributeType attributeType: SubjectAttributeType.values()){
            if(attributeType.oid.equals(oid)){
                return attributeType;
            }
        }
        throw new IllegalArgumentException("Unknown attribute type");
    }

    public static SubjectAttributeType ofKey(String key){
        for(SubjectAttributeType attributeType: SubjectAttributeType.values()){
            if(attributeType.mnemonic.equalsIgnoreCase(key) ||
                    attributeType.description.equalsIgnoreCase(key)){
                return attributeType;
            }
        }
        throw new IllegalArgumentException("Unknown attribute type: "+key);
    }

}
