package org.certeasy;


/**
 * Defines the effect of the {@link ExtendedKeyUsage} restrictions.
 */
public enum ExtendedKeyUsageEffect {

    /**
     * The {@link ExtendedKeyUsage} shall be strictly enforced.
     */
    ENFORCE,

    /**
     * The {@link ExtendedKeyUsage} should be taken as purely informational.
     */
    INFO
}
