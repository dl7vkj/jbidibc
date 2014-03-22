package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

public enum CommandStationProgState implements BidibEnum {
    // @formatter:off
    PROG_START(BidibLibrary.BIDIB_CS_PROG_START), PROG_RUNNING(BidibLibrary.BIDIB_CS_PROG_RUNNING), PROG_OKAY(
        BidibLibrary.BIDIB_CS_PROG_OKAY), PROG_STOPPED(BidibLibrary.BIDIB_CS_PROG_STOPPED), PROG_NO_LOCO(
        BidibLibrary.BIDIB_CS_PROG_NO_LOCO), PROG_NO_ANSWER(BidibLibrary.BIDIB_CS_PROG_NO_ANSWER), PROG_SHORT(
        BidibLibrary.BIDIB_CS_PROG_SHORT);
    // @formatter:on

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
