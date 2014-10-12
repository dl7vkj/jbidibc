package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.CommandStationState;
import org.bidib.jbidibc.core.exception.ProtocolException;

/**
 * Command to set the state of the command station
 */
public class CommandStationSetStateMessage extends BidibCommandMessage {
    public CommandStationSetStateMessage(CommandStationState operation) {
        super(0, BidibLibrary.MSG_CS_SET_STATE, operation.getType());
    }

    public CommandStationSetStateMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_CS_SET_STATE";
    }

    public CommandStationState getState() {
        return CommandStationState.valueOf(getData()[0]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { CommandStationStateResponse.TYPE };
    }
}
