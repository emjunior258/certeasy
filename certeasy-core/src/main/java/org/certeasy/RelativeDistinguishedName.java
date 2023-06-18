package org.certeasy;

/**
 * A component of a {@link DistinguishedName}. Consists in an attribute type/value pair.
 * @param type the predefined type of attribute
 * @param value the value of the attribute
 * @param position the position of the value in case of multi-value attribute
 */
public record RelativeDistinguishedName(SubjectAttributeType type, String value, int position) implements Comparable<RelativeDistinguishedName> {

    public RelativeDistinguishedName{
        if(type==null)
            throw new IllegalArgumentException("type must not be null");
        if(value==null || value.isEmpty())
            throw new IllegalArgumentException("value must NOT be null nor empty");
    }

    public RelativeDistinguishedName(SubjectAttributeType type, String value){
        this(type, value, 0);
    }

    @Override
    public String toString() {
        return type.getMnemonic() + "=" + value;
    }

    @Override
    public int compareTo(RelativeDistinguishedName o) {
        return Integer.compare(type.getPriority() - position,
                o.type.getPriority() - o.position);
    }

}
