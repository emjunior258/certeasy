package org.easycert;


/**
 * Defines the effect of the {@link ExtendedKeyUsage} restrictions.
 */
public enum ExtendedKeyUsageEffect {

    /**
     * The {@link ExtendedKeyUsage} shall be strictly enforced.
     */
    Enforce,

    /**
     * The {@link ExtendedKeyUsage} should be taken as purely informational.
     */
    Info
}
