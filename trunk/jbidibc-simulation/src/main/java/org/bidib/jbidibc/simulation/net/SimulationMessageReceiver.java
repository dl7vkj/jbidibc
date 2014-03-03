package org.bidib.jbidibc.simulation.net;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.NodeListener;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.RequestFactory;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.SimulatorRegistry;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SimulationMessageReceiver implements SimulationBidibMessageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationMessageReceiver.class);

    private SimulatorRegistry simulatorRegistry;

    public SimulationMessageReceiver() {

    }

    public void setSimulatorRegistry(SimulatorRegistry simulatorRegistry) {
        this.simulatorRegistry = simulatorRegistry;
    }

    @Override
    public void processMessages(ByteArrayOutputStream output) throws ProtocolException {

        // if a CRC error is detected in splitMessages the reading loop will terminate ...
        try {
            List<BidibCommand> commands = new RequestFactory().create(output.toByteArray());

            if (commands != null) {

                for (BidibCommand bidibCommand : commands) {
                    LOGGER.info("Process the current bidibCommand: {}", bidibCommand);

                    String nodeAddress = ByteUtils.bytesToHex(bidibCommand.getAddr());

                    SimulatorNode simulatorNode = simulatorRegistry.getSimulator(nodeAddress);
                    if (simulatorNode != null) {
                        simulatorNode.processRequest(bidibCommand);
                    }
                    else {
                        LOGGER.error("No simulator available for address: {}", nodeAddress);
                    }
                }
            }
            else {
                LOGGER.warn("No commands in packet received.");
            }
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create BiDiB message failed.", ex);
        }

    }

    @Override
    public abstract void publishResponse(ByteArrayOutputStream output) throws ProtocolException;

    @Override
    public String getErrorInformation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addMessageListener(MessageListener messageListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeMessageListener(MessageListener messageListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addNodeListener(NodeListener nodeListener) {
        // TODO Auto-generated method stub

    }

}
