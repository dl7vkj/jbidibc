package org.bidib.jbidibc.node.listener;

public interface TransferListener {
    /**
     * Signal that receive message was started
     */
    void receiveStarted();

    /**
     * Signal that receive message was stopped
     */
    void receiveStopped();

    /**
     * Signal that send message was started
     */
    void sendStarted();

    /**
     * Signal that send message was stopped
     */
    void sendStopped();
}
