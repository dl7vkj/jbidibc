package org.bidib.jbidibc.core.enumeration;

public enum MotorPortEnum implements BidibEnum {
    // @formatter:off
    FORWARD(0), BACKWARD(1);
    // @formatter:on

    private final byte type;

    MotorPortEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a motor port enum.
     * 
     * @param type
     *            numeric value of the motor port enum
     * 
     * @return MotorPortEnum
     */
    public static MotorPortEnum valueOf(byte type) {
        MotorPortEnum result = null;

        for (MotorPortEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a motor port enum");
        }
        return result;
    }
}
