package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.CommandStationState;

/**
 * Command to set the state of the command station
 */
public class CommandStationSetStateMessage extends BidibMessage {
    public CommandStationSetStateMessage(CommandStationState operation) {
        super(0, BidibLibrary.MSG_CS_SET_STATE, operation.getType());
    }
}
