package org.certeasy;

import java.util.Set;

public record ExtendedKeyUsages(Set<ExtendedKeyUsage> usages, ExtendedKeyUsageEffect effect) {

    public ExtendedKeyUsages(Set<ExtendedKeyUsage> extendedKeyUsages){
        this(extendedKeyUsages, ExtendedKeyUsageEffect.ENFORCE);
    }
    public ExtendedKeyUsages {
        if(usages ==null || usages.isEmpty())
            throw new IllegalArgumentException("usages MUST not be null nor empty");
        if(effect==null)
            throw new IllegalArgumentException("effect MUST not be null");
    }
}
