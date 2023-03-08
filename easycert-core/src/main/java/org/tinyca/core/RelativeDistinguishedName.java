package org.tinyca.core;

/**
 * A component of a {@link DistinguishedName}. Consists in an attribute type/value pair.
 * @param type the predefined type of attribute
 * @param value the value of the attribute
 */
public record RelativeDistinguishedName(SubjectAttributeType type, String value) {

    public RelativeDistinguishedName(SubjectAttributeType type, String value){
        if(type==null)
            throw new IllegalArgumentException("type must not be null");
        if(value==null || value.isEmpty())
            throw new IllegalArgumentException("value must NOT be null nor empty");
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return type + "=" + value;
    }

}
