package org.bidib.jbidibc.net;

import java.net.DatagramPacket;
import java.net.InetAddress;

public interface NetMessageHandler {

    /**
     * Receive messages from the configured port
     */
    void receive(final DatagramPacket packet);

    void send(NetBidibPort port, byte[] bytes);

    void addRemoteAddress(InetAddress address, int port);
}
