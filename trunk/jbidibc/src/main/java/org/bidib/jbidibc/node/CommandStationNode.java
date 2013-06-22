package org.bidib.jbidibc.node;

import java.io.IOException;
import java.util.BitSet;

import org.bidib.jbidibc.enumeration.CommandStationState;
import org.bidib.jbidibc.enumeration.DirectionEnum;
import org.bidib.jbidibc.enumeration.DriveAcknowledge;
import org.bidib.jbidibc.enumeration.SpeedStepsEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.CommandStationBinaryStateMessage;
import org.bidib.jbidibc.message.CommandStationDriveAcknowledgeResponse;
import org.bidib.jbidibc.message.CommandStationDriveMessage;
import org.bidib.jbidibc.message.CommandStationSetStateMessage;
import org.bidib.jbidibc.message.CommandStationStateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandStationNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationNode.class);

    private BidibNode delegate;

    CommandStationNode(BidibNode delegate) {
        this.delegate = delegate;
    }

    public DriveAcknowledge setBinaryState(int address, int state, boolean value) throws IOException,
        ProtocolException, InterruptedException {

        LOGGER.debug("set binary state, address: {}, state: {}, value: {}", address, state, value);

        DriveAcknowledge result = null;
        BidibMessage response =
            delegate.send(new CommandStationBinaryStateMessage(address, state, value), true,
                CommandStationDriveAcknowledgeResponse.TYPE);

        if (response instanceof CommandStationDriveAcknowledgeResponse) {
            result = ((CommandStationDriveAcknowledgeResponse) response).getState();
        }
        return result;
    }

    public DriveAcknowledge setDrive(
        int address, SpeedStepsEnum speedSteps, Integer speed, DirectionEnum direction, BitSet activeFunctions,
        BitSet functions) throws IOException, ProtocolException, InterruptedException {

        LOGGER.debug("set drive, address: {}, speed: {}", address, speed);

        DriveAcknowledge result = null;
        BidibMessage response =
            delegate.send(new CommandStationDriveMessage(address, speedSteps, speed, direction, activeFunctions,
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
        BidibMessage response =
            delegate.send(new CommandStationSetStateMessage(commandStationState), true,
                CommandStationStateResponse.TYPE);

        if (response instanceof CommandStationStateResponse) {
            result = ((CommandStationStateResponse) response).getState();
        }
        return result;
    }

    public DriveAcknowledge releaseLoco(
        int address) throws IOException, ProtocolException, InterruptedException {

        LOGGER.debug("Release loco, address: {}", address);

        DriveAcknowledge result = null;
        BidibMessage response =
            delegate.send(new CommandStationDriveMessage(address, SpeedStepsEnum.DCC128, null, DirectionEnum.BACKWARD, null,
                null), true, CommandStationDriveAcknowledgeResponse.TYPE);

        if (response instanceof CommandStationDriveAcknowledgeResponse) {
            result = ((CommandStationDriveAcknowledgeResponse) response).getState();
        }
        return result;
    }
}
