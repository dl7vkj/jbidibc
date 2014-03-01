package org.bidib.jbidibc.simulation.net;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.RequestFactory;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationMessageReceiver implements BidibMessageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationMessageReceiver.class);

    private SimulatorNode simulatorNode;

    public void setSimulatorNode(SimulatorNode simulatorNode) {
        this.simulatorNode = simulatorNode;
    }

    @Override
    public void processMessages(ByteArrayOutputStream output) throws ProtocolException {
        // TODO Auto-generated method stub

        // if a CRC error is detected in splitMessages the reading loop will terminate ...
        try {
            List<BidibCommand> commands = new RequestFactory().create(output.toByteArray());

            if (commands != null) {

                for (BidibCommand bidibCommand : commands) {
                    LOGGER.info("Process the current bidibCommand: {}", bidibCommand);

                    simulatorNode.processRequest(bidibCommand);
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
    public String getErrorInformation() {
        // TODO Auto-generated method stub
        return null;
    }

}
