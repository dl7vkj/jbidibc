package org.bidib.jbidibc.enumeration;

public enum IdentifyState implements BidibEnum {
    //@formatter:off
    STOP(0), START(1);
    //@formatter:on

    private final byte type;

    IdentifyState(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create an identify state.
     * 
     * @param type
     *            numeric value of the identify state
     * 
     * @return IdentifyState
     */
    public static IdentifyState valueOf(byte type) {
        IdentifyState result = null;

        for (IdentifyState e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to an identify state");
        }
        return result;
    }
}
