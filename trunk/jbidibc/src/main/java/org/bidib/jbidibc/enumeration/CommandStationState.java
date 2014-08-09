package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CommandStationState implements BidibEnum {
    // @formatter:off
    OFF(BidibLibrary.BIDIB_CS_STATE_OFF), STOP(BidibLibrary.BIDIB_CS_STATE_STOP), SOFTSTOP(
        BidibLibrary.BIDIB_CS_STATE_SOFTSTOP), GO(BidibLibrary.BIDIB_CS_STATE_GO), PROG(
        BidibLibrary.BIDIB_CS_STATE_PROG), PROGBUSY(BidibLibrary.BIDIB_CS_STATE_PROGBUSY), BUSY(
        BidibLibrary.BIDIB_CS_STATE_BUSY), QUERY(BidibLibrary.BIDIB_CS_STATE_QUERY), GO_IGN_WD(
        BidibLibrary.BIDIB_CS_STATE_GO_IGN_WD);
    // @formatter:on

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationState.class);

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

    public static boolean isOnState(CommandStationState commandStationState) {
        return !isPtProgState(commandStationState) && !isOffState(commandStationState);
    }

    public static boolean isOffState(CommandStationState commandStationState) {
        if (commandStationState == null) {
            LOGGER.warn("Cannot check if command station if off because the commandStationState is null.");
            throw new IllegalArgumentException(
                "Cannot check if command station if off because the commandStationState is null.");
        }

        boolean isOffState = false;
        switch (commandStationState) {
            case OFF:
            case STOP:
            case SOFTSTOP:
                isOffState = true;
                break;
            default:
                break;
        }
        return isOffState;
    }

    public static boolean isPtProgState(CommandStationState commandStationState) {
        boolean isPtProgState = false;
        switch (commandStationState) {
            case PROG:
            case PROGBUSY:
                isPtProgState = true;
                break;
            default:
                break;
        }
        return isPtProgState;
    }
}
