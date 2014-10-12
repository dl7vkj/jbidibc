package org.bidib.jbidibc.core.enumeration;

public enum AccessoryAcknowledge implements BidibEnum {
    // @formatter:off
    NOT_ACKNOWLEDGED(0), ACKNOWLEDGED(1), DELAYED(2);
    // @formatter:on

    private final byte type;

    AccessoryAcknowledge(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a accessory acknowledge.
     * 
     * @param type
     *            numeric value of the accessory acknowledge
     * 
     * @return AccessoryAcknowledge
     */
    public static AccessoryAcknowledge valueOf(byte type) {
        AccessoryAcknowledge result = null;

        for (AccessoryAcknowledge e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a accessory acknowledge");
        }
        return result;
    }
}
