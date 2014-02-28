package org.bidib.jbidibc.enumeration;

public enum AddressTypeEnum implements BidibEnum {
    // @formatter:off
    LOCOMOTIVE_FORWARD(0), ACCESSORY(1), LOCOMOTIVE_BACKWARD(2), EXTENDED_ACCESSORY(3);
    // @formatter:on

    private final byte type;

    AddressTypeEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create an address type enum.
     * 
     * @param type
     *            numeric value of the address type enum
     * 
     * @return AddressTypeEnum
     */
    public static AddressTypeEnum valueOf(byte type) {
        AddressTypeEnum result = null;

        for (AddressTypeEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a address type enum");
        }
        return result;
    }
}
