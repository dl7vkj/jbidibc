package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.CommandStationState;

public class CommandStationSetStateMessage extends BidibMessage {
    public CommandStationSetStateMessage(CommandStationState operation) {
        super(0, BidibLibrary.MSG_CS_SET_STATE, operation.getType());
    }
}
