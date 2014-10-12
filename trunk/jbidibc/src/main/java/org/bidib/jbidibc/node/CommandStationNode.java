package org.bidib.jbidibc.node;

import java.util.Arrays;
import java.util.BitSet;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.enumeration.AccessoryAcknowledge;
import org.bidib.jbidibc.enumeration.ActivateCoilEnum;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.enumeration.CommandStationPom;
import org.bidib.jbidibc.enumeration.CommandStationPt;
import org.bidib.jbidibc.enumeration.CommandStationState;
import org.bidib.jbidibc.enumeration.DirectionEnum;
import org.bidib.jbidibc.enumeration.DriveAcknowledge;
import org.bidib.jbidibc.enumeration.PomAcknowledge;
import org.bidib.jbidibc.enumeration.SpeedStepsEnum;
import org.bidib.jbidibc.enumeration.TimeBaseUnitEnum;
import org.bidib.jbidibc.enumeration.TimingControlEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.helpers.DefaultMessageListener;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.CommandStationBinaryStateMessage;
import org.bidib.jbidibc.message.CommandStationDriveAcknowledgeResponse;
import org.bidib.jbidibc.message.CommandStationDriveMessage;
import org.bidib.jbidibc.message.CommandStationPomAcknowledgeResponse;
import org.bidib.jbidibc.message.CommandStationPomMessage;
import org.bidib.jbidibc.message.CommandStationProgMessage;
import org.bidib.jbidibc.message.CommandStationSetStateMessage;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandStationNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationNode.class);

    private BidibNode delegate;

    CommandStationNode(BidibNode delegate) {
        this.delegate = delegate;
    }

    public DriveAcknowledge setBinaryState(int address, int state, boolean value) throws ProtocolException {

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
        BitSet functions) throws ProtocolException {

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

    public CommandStationState setState(CommandStationState commandStationState) throws ProtocolException {

        LOGGER.debug("set state, commandStationState: {}", commandStationState);

        CommandStationState resultCommandStationState = CommandStationState.OFF;

        final Object commandStationStateFeedbackLock = new Object();
        final CommandStationState[] resultHolder = new CommandStationState[1];

        final byte[] nodeAddr = delegate.getAddr();
        // create a temporary message listener
        MessageListener messageListener = new DefaultMessageListener() {
            @Override
            public void csState(byte[] address, CommandStationState state) {
                LOGGER.info("+++ Command station state was signalled: {}, address: {}", state, address);

                if (Arrays.equals(nodeAddr, address)) {
                    resultHolder[0] = state;

                    synchronized (commandStationStateFeedbackLock) {
                        commandStationStateFeedbackLock.notify();
                    }
                }
                else {
                    LOGGER.info("Received command station state from another node.");
                }
            }
        };

        try {
            // add the message listener to the node
            addMessageListener(messageListener);

            synchronized (commandStationStateFeedbackLock) {
                // send the query command station state command
                delegate.sendNoWait(new CommandStationSetStateMessage(commandStationState));

                LOGGER.info("+++ The command station set state message was sent, wait for response.");
                // wait for the response
                try {
                    commandStationStateFeedbackLock.wait(2000L);
                }
                catch (InterruptedException ie) {
                    LOGGER.warn("Wait for command station state was interrupted.", ie);
                }

                LOGGER.info("+++ After wait for response.");
            }

            resultCommandStationState = resultHolder[0];
            LOGGER.info("+++ Return the current command station state: {}", resultCommandStationState);
        }
        finally {
            // remove the temporary message listener
            removeMessageListener(messageListener);
        }

        return resultCommandStationState;
    }

    public CommandStationState queryState() throws ProtocolException {

        LOGGER.debug("Query the state of the commandStation.");

        // TODO

        CommandStationState commandStationState = CommandStationState.OFF;

        final Object commandStationStateFeedbackLock = new Object();
        final CommandStationState[] resultHolder = new CommandStationState[1];

        final byte[] nodeAddr = delegate.getAddr();
        // create a temporary message listener
        MessageListener messageListener = new DefaultMessageListener() {
            @Override
            public void csState(byte[] address, CommandStationState state) {
                LOGGER.info("+++ Command station state was signalled: {}, address: {}", state, address);

                if (Arrays.equals(nodeAddr, address)) {
                    resultHolder[0] = state;

                    synchronized (commandStationStateFeedbackLock) {
                        commandStationStateFeedbackLock.notify();
                    }
                }
                else {
                    LOGGER.info("Received command station state from another node.");
                }
            }
        };

        try {
            // add the message listener to the node
            addMessageListener(messageListener);

            synchronized (commandStationStateFeedbackLock) {
                // send the query command station state command
                delegate.sendNoWait(new CommandStationSetStateMessage(CommandStationState.QUERY));

                LOGGER.info("+++ The command station state query message was sent, wait for response.");
                // wait for the response
                try {
                    commandStationStateFeedbackLock.wait(2000L);
                }
                catch (InterruptedException ie) {
                    LOGGER.warn("Wait for command station state was interrupted.", ie);
                }

                LOGGER.info("+++ After wait for response.");
            }

            commandStationState = resultHolder[0];
            LOGGER.info("+++ Return the current command station state: {}", commandStationState);
        }
        finally {
            // remove the temporary message listener
            removeMessageListener(messageListener);
        }
        return commandStationState;
    }

    public AccessoryAcknowledge setAccessory(
        int address, AddressTypeEnum addressType, TimingControlEnum timingControl, ActivateCoilEnum activateCoil,
        int aspect, TimeBaseUnitEnum timeBaseUnit, int time) throws ProtocolException {

        LOGGER.debug("Set accessory, address: {}", address);

        final Object commandStationAccessoryFeedbackLock = new Object();
        final AccessoryAcknowledge[] resultHolder = new AccessoryAcknowledge[1];

        final int requestedDecoderAddress = address;
        // create a temporary message listener
        MessageListener messageListener = new DefaultMessageListener() {
            @Override
            public void csAccessoryAcknowledge(byte[] address, int decoderAddress, AccessoryAcknowledge acknowledge) {
                LOGGER.info("+++ CS accessory ackn was signalled, decoderAddress: {}, acknowledge: {}", decoderAddress,
                    acknowledge);

                if (requestedDecoderAddress != decoderAddress) {
                    LOGGER.info("Acknowledge from different decoder: {}, requested: {}", decoderAddress,
                        requestedDecoderAddress);
                }

                resultHolder[0] = acknowledge;

                synchronized (commandStationAccessoryFeedbackLock) {
                    commandStationAccessoryFeedbackLock.notify();
                }
            }
        };

        AccessoryAcknowledge accessoryAcknowledge = null;
        try {
            // add the message listener to the node
            addMessageListener(messageListener);

            synchronized (commandStationAccessoryFeedbackLock) {
                // send the query command station accessory command
                BidibCommand message =
                    delegate.getRequestFactory().createCommandStationAccessory(address, addressType, timingControl,
                        activateCoil, aspect, timeBaseUnit, time);
                delegate.sendNoWait(message);

                LOGGER.info("+++ The command station accessory message was sent, wait for response.");
                // wait for the response
                try {
                    commandStationAccessoryFeedbackLock.wait(2000L);
                }
                catch (InterruptedException ie) {
                    LOGGER.warn("Wait for command station accessory ackn was interrupted.", ie);
                }

                LOGGER.info("+++ After wait for response.");
            }

            accessoryAcknowledge = resultHolder[0];
            LOGGER.info("+++ Return the current command station accessory ackn: {}", accessoryAcknowledge);
        }
        finally {
            // remove the temporary message listener
            removeMessageListener(messageListener);
        }

        return accessoryAcknowledge;
    }

    public DriveAcknowledge releaseLoco(int address) throws ProtocolException {

        LOGGER.debug("Release loco, address: {}", address);

        DriveAcknowledge result = null;
        BidibMessage response =
            delegate.send(new CommandStationDriveMessage(address, SpeedStepsEnum.DCC128, null, DirectionEnum.BACKWARD,
                null, null), true, CommandStationDriveAcknowledgeResponse.TYPE);

        if (response instanceof CommandStationDriveAcknowledgeResponse) {
            result = ((CommandStationDriveAcknowledgeResponse) response).getState();
        }
        return result;
    }

    public PomAcknowledge readPom(AddressData locoAddress, CommandStationPom opCode, int cvNumber)
        throws ProtocolException {
        byte[] data = { 0 };
        BidibMessage response =
            delegate.send(new CommandStationPomMessage(locoAddress, opCode, cvNumber, data), true,
                CommandStationPomAcknowledgeResponse.TYPE);
        PomAcknowledge result = null;
        if (response instanceof CommandStationPomAcknowledgeResponse) {
            result = ((CommandStationPomAcknowledgeResponse) response).getAcknState();
        }
        LOGGER.debug("Return the pomAcknowledge: {}", result);
        return result;
    }

    public PomAcknowledge writePom(AddressData locoAddress, CommandStationPom opCode, int cvNumber, int cvValue)
        throws ProtocolException {
        byte[] data = { ByteUtils.getLowByte(cvValue) };
        BidibMessage response =
            delegate.send(new CommandStationPomMessage(locoAddress, opCode, cvNumber, data), true,
                CommandStationPomAcknowledgeResponse.TYPE);
        PomAcknowledge result = null;
        if (response instanceof CommandStationPomAcknowledgeResponse) {
            result = ((CommandStationPomAcknowledgeResponse) response).getAcknState();
        }
        LOGGER.debug("Return the pomAcknowledge: {}", result);
        return result;
    }

    public void readPt(CommandStationPt opCode, int cvNumber) throws ProtocolException {
        LOGGER.info("Send PT read command, opCode: {}, cvNumber: {}", opCode, cvNumber);
        byte data = 0;
        delegate.sendNoWait(new CommandStationProgMessage(opCode, cvNumber, data));
    }

    public void writePt(CommandStationPt opCode, int cvNumber, int cvValue) throws ProtocolException {
        LOGGER.info("Send PT write command, opCode: {}, cvNumber: {}, cvValue: {}", opCode, cvNumber, cvValue);
        byte data = ByteUtils.getLowByte(cvValue);
        delegate.sendNoWait(new CommandStationProgMessage(opCode, cvNumber, data));
    }

    private void addMessageListener(final MessageListener messageListener) {
        delegate.getMessageReceiver().addMessageListener(messageListener);
    }

    private void removeMessageListener(final MessageListener messageListener) {
        delegate.getMessageReceiver().removeMessageListener(messageListener);
    }
}
