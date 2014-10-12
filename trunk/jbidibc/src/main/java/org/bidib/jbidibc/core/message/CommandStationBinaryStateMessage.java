package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;

/**
 * Command to trigger an action in the mobile decoder.
 */
public class CommandStationBinaryStateMessage extends BidibCommandMessage {
    public CommandStationBinaryStateMessage(int address, int state, boolean value) {
        super(0, BidibLibrary.MSG_CS_BIN_STATE, new byte[] { (byte) (address & 0xFF), (byte) ((address & 0xFF00) >> 8),
            (byte) (state & 0xFF), (byte) ((state & 0xFF00) >> 8), value ? (byte) 1 : (byte) 0 });
    }

    public String getName() {
        return "MSG_CS_BIN_STATE";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { CommandStationDriveAcknowledgeResponse.TYPE };
    }
}
