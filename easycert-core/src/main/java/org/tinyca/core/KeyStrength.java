package org.tinyca.core;

/**
 * The Strength of an RSA key.
 */
public enum KeyStrength {

    LOW_STRENGTH(512),

    MEDIUM_STRENGTH(1024),

    HIGH_STRENGTH(2048),

    VERY_HIGH_STRENGTH(4096);

    private final int size;

    private KeyStrength(int size){
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
