package org.easycert;


import java.util.Set;

/**
 * Represents an object that can be turned into a set of {@link  RelativeDistinguishedName}s.
 */
public interface RDNConvertible {

    Set<RelativeDistinguishedName> toRdns();

}
