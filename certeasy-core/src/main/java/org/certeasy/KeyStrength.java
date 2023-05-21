package org.certeasy;

/**
 * The Strength of an RSA key.
 */
public enum KeyStrength {

    LOW(512),

    MEDIUM(1024),

    HIGH(2048),

    VERY_HIGH(4096);

    private final int size;

    private KeyStrength(int size){
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
