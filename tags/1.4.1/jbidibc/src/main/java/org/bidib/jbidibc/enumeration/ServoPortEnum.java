package org.bidib.jbidibc.enumeration;

public enum ServoPortEnum implements BidibEnum {
    START(0);

    private final byte type;

    ServoPortEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a servo port enum.
     * 
     * @param type
     *            numeric value of the servo port enum
     * 
     * @return ServoPortEnum
     */
    public static ServoPortEnum valueOf(byte type) {
        ServoPortEnum result = null;

        for (ServoPortEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a servo port enum");
        }
        return result;
    }
}
