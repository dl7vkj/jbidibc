package org.bidib.jbidibc.node;

import java.io.IOException;

import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.enumeration.CommandStationState;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.CommandStationSetStateMessage;
import org.bidib.jbidibc.message.CommandStationStateResponse;

public class CommandStationNode extends DeviceNode {
    CommandStationNode(byte[] addr) {
        super(addr);
    }

    public BoosterState setCommandStationState(CommandStationState commandStationState) throws IOException, ProtocolException,
            InterruptedException {
    	BoosterState result = BoosterState.OFF;
        BidibMessage response = send(new CommandStationSetStateMessage(commandStationState));

        if (response instanceof CommandStationStateResponse) {
            result = ((CommandStationStateResponse) response).getState();
        }
        return result;
    }
}
