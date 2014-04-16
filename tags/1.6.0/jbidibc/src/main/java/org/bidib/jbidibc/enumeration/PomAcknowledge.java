package org.bidib.jbidibc.enumeration;

public enum PomAcknowledge implements BidibEnum {
    // @formatter:off
    NOT_ACKNOWLEDGED(0), ACKNOWLEDGED(1), DELAYED(2);
    // @formatter:on

    private final byte type;

    PomAcknowledge(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a POM acknowledge.
     * 
     * @param type
     *            numeric value of the POM acknowledge
     * 
     * @return PomAcknowledge
     */
    public static PomAcknowledge valueOf(byte type) {
        PomAcknowledge result = null;

        for (PomAcknowledge e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a POM acknowledge");
        }
        return result;
    }
}
