package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

/**
 * Command to trigger an action in the mobile decoder.
 */
public class CommandStationBinaryStateMessage extends BidibMessage implements BidibCommand {
    public CommandStationBinaryStateMessage(int address, int state, boolean value) {
        super(0, BidibLibrary.MSG_CS_BIN_STATE, new byte[] { (byte) (address & 0xFF), (byte) ((address & 0xFF00) >> 8),
            (byte) (state & 0xFF), (byte) ((state & 0xFF00) >> 8), value ? (byte) 1 : (byte) 0 });
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { CommandStationDriveAcknowledgeResponse.TYPE };
    }
}
