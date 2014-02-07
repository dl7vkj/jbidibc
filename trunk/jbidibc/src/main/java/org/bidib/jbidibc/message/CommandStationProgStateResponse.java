package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.CommandStationProgState;
import org.bidib.jbidibc.exception.ProtocolException;

/**
 * Response from command station with the current state
 */
public class CommandStationProgStateResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_PROG_STATE;

    CommandStationProgStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no command station prog state received");
        }
    }

    public CommandStationProgStateResponse(byte[] addr, int num, byte state) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_CS_PROG_STATE, state);
    }

    public CommandStationProgState getState() {
        return CommandStationProgState.valueOf(getData()[0]);
    }
}
