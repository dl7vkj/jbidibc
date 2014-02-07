package org.bidib.jbidibc.enumeration;

public enum CommandStationProgState implements BidibEnum {
    //@formatter:off
    PROG_STATE(1), PROG_DATA(2);
    //@formatter:on

    private final byte type;

    CommandStationProgState(int type) {
        this.type = (byte) type;
    }

    @Override
    public byte getType() {
        return type;
    }

    /**
     * Create a command station prog state.
     * 
     * @param type
     *            numeric value of the command station prog state
     * 
     * @return CommandStationState
     */
    public static CommandStationProgState valueOf(byte type) {
        CommandStationProgState result = null;

        for (CommandStationProgState e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a command station prog state");
        }
        return result;
    }
}
