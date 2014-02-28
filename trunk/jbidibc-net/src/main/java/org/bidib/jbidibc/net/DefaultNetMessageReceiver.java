package org.bidib.jbidibc.net;

import java.net.DatagramPacket;

import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.node.NodeFactory;

public class DefaultNetMessageReceiver extends MessageReceiver implements NetMessageReceiver {

    protected DefaultNetMessageReceiver(NodeFactory nodeFactory) {
        super(nodeFactory);
    }

    @Override
    public void receive(final DatagramPacket packet) {
        // TODO a datagramm packet was received ... process the envelope and extract the message

        // TODO for the first magic response we need special processing because we need to keep the session key
    }

    @Override
    public byte[] getRemainingOutputBuffer() {
        // TODO Auto-generated method stub
        return null;
    }

}
