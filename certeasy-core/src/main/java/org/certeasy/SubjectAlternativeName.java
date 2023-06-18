package org.certeasy;

/**
 * Represents an alternative name that can be used to reference the subject of a {@link Certificate}.
 * @param type the type of alternative name
 * @param value the value of the alternative name
 */
public record SubjectAlternativeName(SubjectAlternativeNameType type, String value) {

    public SubjectAlternativeName{
        if(type==null)
            throw new IllegalArgumentException("type must NOT be null");
        if(value==null || value.isEmpty())
            throw new IllegalArgumentException("value MUST NOT be null nor empty");
    }

}
