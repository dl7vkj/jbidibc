package org.bidib.jbidibc.net;

import java.net.DatagramPacket;

public interface NetMessageHandler {

    /**
     * Receive messages from the configured port
     */
    void receive(final DatagramPacket packet);

    void send(NetBidibPort port, byte[] bytes);
}
