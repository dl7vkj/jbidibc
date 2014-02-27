package org.bidib.jbidibc.net;

import java.net.DatagramPacket;

import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.node.NodeFactory;

public class NetMessageReceiver extends MessageReceiver {

    protected NetMessageReceiver(NodeFactory nodeFactory) {
        super(nodeFactory);
    }

    /**
     * Receive messages from the configured port
     */
    public void receive(final DatagramPacket packet) {
    }

    @Override
    public byte[] getRemainingOutputBuffer() {
        // TODO Auto-generated method stub
        return null;
    }

}
