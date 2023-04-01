package org.certeasy;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Represents a distinguished commonName for an X.509 certificate.
 *
 * Consists of a sequence of {@link RelativeDistinguishedName}s (RDNs) where each RDN is expressed as an attribute type/value pair.
 * A distinguished name is specified as a string consisting of a sequence of attribute type/value pairs separated by a semicolon (';' U+003B). The general format is
 *
 * <type>=<value>(;<type>=<value>)*[;]
 *
 * @param relativeDistinguishedNames
 */
public record DistinguishedName(Set<RelativeDistinguishedName> relativeDistinguishedNames) {

    public DistinguishedName(Set<RelativeDistinguishedName> relativeDistinguishedNames){
        if(relativeDistinguishedNames==null || relativeDistinguishedNames.isEmpty())
            throw new IllegalArgumentException("relative distinguished names set MUST not be null nor empty");
        if(relativeDistinguishedNames.stream().filter(it -> it.type() == SubjectAttributeType.CommonName)
                .findAny().isEmpty())
            throw new IllegalArgumentException("Missing RDN with " + SubjectAttributeType.CommonName.name()+ " attribute type");
        this.relativeDistinguishedNames = Collections.unmodifiableSet(relativeDistinguishedNames);
        Set<SubjectAttributeType> singleAttributes = Set.of(SubjectAttributeType.values())
                .stream().filter(it -> !it.isMultiValue())
                .collect(Collectors.toSet());
        for(SubjectAttributeType attributeType: singleAttributes) {
            if (findAll(attributeType).size() > 1)
                throw new IllegalArgumentException(attributeType.name()+ " attribute NOT multiValue but Multiple RDNs found");
        }
    }

    public String getCommonName(){
        return this.findFirst(SubjectAttributeType.CommonName).get().value();
    }

    public Optional<RelativeDistinguishedName> findFirst(SubjectAttributeType attributeType){
        if(attributeType==null)
            throw new IllegalArgumentException("attributeType MUST not be null nor empty");
        return this.relativeDistinguishedNames
                .stream().filter(it -> it.type() == attributeType)
                .findFirst();
    }

    public Set<RelativeDistinguishedName> findAll(SubjectAttributeType attributeType){
        if(attributeType==null)
            throw new IllegalArgumentException("attributeType MUST not be null nor empty");
        return this.relativeDistinguishedNames
                .stream().filter(it -> it.type() == attributeType)
                .collect(Collectors.toSet());
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        this.relativeDistinguishedNames.forEach(rdn -> {
            if(!builder.isEmpty()){
                builder.append(',');
            }
            builder.append(rdn.toString());
        });
        return builder.toString();
    }


    public static Builder builder(){

        return new Builder();

    }

    public static final class Builder {

        private Set<RelativeDistinguishedName> distinguishedNameSet = new HashSet<>();

        private Builder(){

        }

        public Builder append(RelativeDistinguishedName rdn){
            if(rdn==null)
                throw new IllegalArgumentException("rdn MUST not be null");
            this.distinguishedNameSet.add(rdn);
            return this;
        }

        public Builder append(Collection<RelativeDistinguishedName> collection){
            if(collection==null)
                throw new IllegalArgumentException("collection MUST not be null");
            this.distinguishedNameSet.addAll(collection);
            return this;
        }

        public Builder append(RDNConvertible convertible){
            if(convertible==null)
                throw new IllegalArgumentException("convertible MUST not be null");
            this.distinguishedNameSet.addAll(convertible.toRdns());
            return this;
        }

        public DistinguishedName build() {
            if(distinguishedNameSet.isEmpty())
                throw new IllegalArgumentException("MUST have at least one RDN");
            return new DistinguishedName(distinguishedNameSet);
        }

    }

}
