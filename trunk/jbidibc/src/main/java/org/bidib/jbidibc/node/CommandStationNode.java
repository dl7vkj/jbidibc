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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandStationNode /*extends DeviceNode*/ {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationNode.class);
//    CommandStationNode(byte[] addr) {
//        super(addr);
//    }
	
	private BidibNode delegate;
	
    CommandStationNode(BidibNode delegate) {
        this.delegate = delegate;
    }

    public DriveAcknowledge setDrive(int address, SpeedSteps speedSteps, Integer speed, BitSet activeFunctions,
            BitSet functions) throws IOException, ProtocolException, InterruptedException {
    	
    	LOGGER.debug("set drive, address: {}, speed: {}", address, speed);
    	
        DriveAcknowledge result = null;
        BidibMessage response = delegate.send(new CommandStationDriveMessage(address, speedSteps, speed, activeFunctions,
                functions), true, CommandStationDriveAcknowledgeResponse.TYPE);

        if (response instanceof CommandStationDriveAcknowledgeResponse) {
            result = ((CommandStationDriveAcknowledgeResponse) response).getState();
        }
        return result;
    }

    public CommandStationState setState(CommandStationState commandStationState) throws IOException, ProtocolException,
            InterruptedException {
    	
    	LOGGER.debug("set state, commandStationState: {}", commandStationState);
    	
        CommandStationState result = CommandStationState.OFF;
        BidibMessage response = delegate.send(new CommandStationSetStateMessage(commandStationState), true, CommandStationStateResponse.TYPE);

        if (response instanceof CommandStationStateResponse) {
            result = ((CommandStationStateResponse) response).getState();
        }
        return result;
    }
}
