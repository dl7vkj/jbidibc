package org.bidib.jbidibc.simulation.net;

import java.net.DatagramPacket;
import java.util.List;

import org.bidib.jbidibc.core.Context;
import org.bidib.jbidibc.core.DefaultContext;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.RequestFactory;
import org.bidib.jbidibc.net.NetMessageReceiver;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationNetMessageReceiver implements NetMessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationNetMessageReceiver.class);

    private SimulatorNode simulatorNode;

    public SimulationNetMessageReceiver(SimulatorNode simulatorNode) {
        this.simulatorNode = simulatorNode;
    }

    @Override
    public void receive(final DatagramPacket packet) {
        LOGGER.info("Received packet, foreign address: {}, foreign port: {}, data: {}", packet.getAddress(),
            packet.getPort(), ByteUtils.bytesToHex(packet.getData(), packet.getLength()));

        // remove the UDP paket wrapper data and forward to the MessageReceiver
        byte[] messages = new byte[packet.getLength() - 4];

        System.arraycopy(packet.getData(), 4, messages, 0, messages.length);

        // if a CRC error is detected in splitMessages the reading loop will terminate ...
        try {
            final Context context = new DefaultContext();
            context.addParam("foreignAddress", packet.getAddress());
            context.addParam("foreignPort", packet.getPort());

            LOGGER.info("Received packet from port, prepared local context: {}", context);

            List<BidibCommand> commands = new RequestFactory().create(messages);

            if (commands != null) {

                for (BidibCommand bidibCommand : commands) {
                    LOGGER.info("Process the current bidibCommand: {}", bidibCommand);

                    simulatorNode.processRequest(context, bidibCommand);
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
}
