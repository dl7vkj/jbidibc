package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.CommandStationState;
import org.bidib.jbidibc.core.exception.ProtocolException;

/**
 * Response from command station with the current state
 */
public class CommandStationStateResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_STATE;

    CommandStationStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no command station state received");
        }
    }

    public CommandStationStateResponse(byte[] addr, int num, byte state) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_CS_STATE, state);
    }

    public String getName() {
        return "MSG_CS_STATE";
    }

    public CommandStationState getState() {
        return CommandStationState.valueOf(getData()[0]);
    }
}
