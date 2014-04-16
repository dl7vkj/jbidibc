package org.bidib.jbidibc.enumeration;

public enum PomAddressing implements BidibEnum {
    POM(0, "pom"), XPOM(0x80, "xpom");

    private final byte type;

    private final String key;

    private PomAddressing(int type, String key) {
        this.type = (byte) type;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public byte getType() {
        return type;
    }
}
