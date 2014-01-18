package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

public enum CommandStationPom implements BidibEnum {
    //@formatter:off
    RD_BLOCK(BidibLibrary.BIDIB_CS_POM_RD_BLOCK), RD_BYTE(BidibLibrary.BIDIB_CS_POM_RD_BYTE), WR_BIT(
        BidibLibrary.BIDIB_CS_POM_WR_BIT), WR_BYTE(BidibLibrary.BIDIB_CS_POM_WR_BYTE), X_RD_BLOCK(
        BidibLibrary.BIDIB_CS_xPOM_RD_BLOCK), X_RD_BYTE(BidibLibrary.BIDIB_CS_xPOM_RD_BYTE), X_WR_BIT(
        BidibLibrary.BIDIB_CS_xPOM_WR_BIT), X_WR_BYTE(BidibLibrary.BIDIB_CS_xPOM_WR_BYTE);
    //@formatter:on

    private final byte type;

    CommandStationPom(int type) {
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
    public static CommandStationPom valueOf(byte type) {
        CommandStationPom result = null;

        for (CommandStationPom e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a command station pom");
        }
        return result;
    }
}
