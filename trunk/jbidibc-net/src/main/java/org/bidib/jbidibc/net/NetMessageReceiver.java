package org.bidib.jbidibc.net;

import java.net.DatagramPacket;

public interface NetMessageReceiver {

    /**
     * Receive messages from the configured port
     */
    void receive(final DatagramPacket packet);
}
