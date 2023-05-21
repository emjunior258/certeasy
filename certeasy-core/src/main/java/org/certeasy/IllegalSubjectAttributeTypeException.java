package org.certeasy;

public class IllegalSubjectAttributeTypeException extends IllegalArgumentException {

    private String attributeType;

    public IllegalSubjectAttributeTypeException(String attributeType) {
        super(String.format("The subject attribute type %s is unknown", attributeType));
        this.attributeType = attributeType;
    }

    public String getAttributeType() {
        return attributeType;
    }
}
