package org.bidib.jbidibc.enumeration;

public enum AddressMode {
    SHORT("short", 0), LONG("long", 1);
    private final String key;

    private final int value;

    private AddressMode(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public static AddressMode valueOf(int value) {
        AddressMode addressMode = null;
        for (AddressMode e : values()) {
            if (e.getValue() == value) {
                addressMode = e;
                break;
            }
        }
        if (addressMode == null) {
            throw new IllegalArgumentException("Cannot map " + value + " to a address mode.");
        }
        return addressMode;
    }
}