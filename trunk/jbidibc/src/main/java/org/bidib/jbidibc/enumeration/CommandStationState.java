package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

public enum CommandStationState implements BidibEnum {
    //@formatter:off
    OFF(BidibLibrary.BIDIB_CS_STATE_OFF),
    STOP(BidibLibrary.BIDIB_CS_STATE_STOP),
    SOFTSTOP(BidibLibrary.BIDIB_CS_STATE_SOFTSTOP),
    GO(BidibLibrary.BIDIB_CS_STATE_GO),
    PROG(BidibLibrary.BIDIB_CS_STATE_PROG),
    PROGBUSY(BidibLibrary.BIDIB_CS_STATE_PROGBUSY),
    BUSY(BidibLibrary.BIDIB_CS_STATE_BUSY),
    QUERY(BidibLibrary.BIDIB_CS_STATE_QUERY);
    //@formatter:on

    private final byte type;

    CommandStationState(int type) {
        this.type = (byte) type;
    }

    @Override
    public byte getType() {
        return type;
    }

    /**
     * Create a command station state.
     * 
     * @param type
     *            numeric value of the command station state
     * 
     * @return CommandStationState
     */
    public static CommandStationState valueOf(byte type) {
        CommandStationState result = null;

        for (CommandStationState e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a command station state");
        }
        return result;
    }
}
