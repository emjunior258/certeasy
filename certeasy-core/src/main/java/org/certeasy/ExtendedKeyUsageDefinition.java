package org.certeasy;

import java.util.Set;

public record ExtendedKeyUsageDefinition(Set<ExtendedKeyUsage> extendedKeyUsages, ExtendedKeyUsageEffect effect) {

    public ExtendedKeyUsageDefinition(Set<ExtendedKeyUsage> extendedKeyUsages){
        this(extendedKeyUsages, ExtendedKeyUsageEffect.Enforce);
    }

    public ExtendedKeyUsageDefinition(Set<ExtendedKeyUsage> extendedKeyUsages, ExtendedKeyUsageEffect effect){
        if(extendedKeyUsages==null || extendedKeyUsages.isEmpty())
            throw new IllegalArgumentException("extendedKeyUsages MUST not be null nor empty");
        if(effect==null)
            throw new IllegalArgumentException("effect MUST not be null");
        this.extendedKeyUsages = extendedKeyUsages;
        this.effect = effect;
    }
}
