package org.bidib.jbidibc.node;

import java.io.IOException;
import java.util.BitSet;

import org.bidib.jbidibc.enumeration.CommandStationState;
import org.bidib.jbidibc.enumeration.DriveAcknowledge;
import org.bidib.jbidibc.enumeration.SpeedSteps;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.CommandStationDriveAcknowledgeResponse;
import org.bidib.jbidibc.message.CommandStationDriveMessage;
import org.bidib.jbidibc.message.CommandStationSetStateMessage;
import org.bidib.jbidibc.message.CommandStationStateResponse;

public class CommandStationNode extends DeviceNode {
    CommandStationNode(byte[] addr) {
        super(addr);
    }

    public DriveAcknowledge setDrive(int address, SpeedSteps speedSteps, Integer speed, BitSet activeFunctions,
            BitSet functions) throws IOException, ProtocolException, InterruptedException {
        DriveAcknowledge result = null;
        BidibMessage response = send(new CommandStationDriveMessage(address, speedSteps, speed, activeFunctions,
                functions));

        if (response instanceof CommandStationDriveAcknowledgeResponse) {
            result = ((CommandStationDriveAcknowledgeResponse) response).getState();
        }
        return result;
    }

    public CommandStationState setState(CommandStationState commandStationState) throws IOException, ProtocolException,
            InterruptedException {
        CommandStationState result = CommandStationState.OFF;
        BidibMessage response = send(new CommandStationSetStateMessage(commandStationState));

        if (response instanceof CommandStationStateResponse) {
            result = ((CommandStationStateResponse) response).getState();
        }
        return result;
    }
}
