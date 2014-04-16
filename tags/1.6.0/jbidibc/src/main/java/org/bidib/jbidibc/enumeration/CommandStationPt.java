package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

public enum CommandStationPt implements BidibEnum {
    // @formatter:off
    BIDIB_CS_PROG_BREAK(BidibLibrary.BIDIB_CS_PROG_BREAK), BIDIB_CS_PROG_QUERY(BidibLibrary.BIDIB_CS_PROG_QUERY), BIDIB_CS_PROG_RD_BYTE(
        BidibLibrary.BIDIB_CS_PROG_RD_BYTE), BIDIB_CS_PROG_RDWR_BIT(BidibLibrary.BIDIB_CS_PROG_RDWR_BIT), BIDIB_CS_PROG_WR_BYTE(
        BidibLibrary.BIDIB_CS_PROG_WR_BYTE);
    // @formatter:on

    private final byte type;

    CommandStationPt(int type) {
        this.type = (byte) type;
    }

    @Override
    public byte getType() {
        return type;
    }

    /**
     * Create a command station PT state.
     * 
     * @param type
     *            numeric value of the command station PT state
     * 
     * @return CommandStationPt
     */
    public static CommandStationPt valueOf(byte type) {
        CommandStationPt result = null;

        for (CommandStationPt e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a command station pt");
        }
        return result;
    }
}
