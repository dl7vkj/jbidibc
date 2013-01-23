package org.bidib.jbidibc.node;

import java.io.IOException;

import org.bidib.jbidibc.enumeration.CommandStationState;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.CommandStationSetStateMessage;
import org.bidib.jbidibc.message.CommandStationStateResponse;

public class CommandStationNode extends DeviceNode {
    CommandStationNode(byte[] addr) {
        super(addr);
    }

    public int setCommandStationState(CommandStationState commandStationState) throws IOException, ProtocolException,
            InterruptedException {
        int result = 0;
        BidibMessage response = send(new CommandStationSetStateMessage(commandStationState));

        if (response instanceof CommandStationStateResponse) {
            result = ((CommandStationStateResponse) response).getState();
        }
        return result;
    }
}
