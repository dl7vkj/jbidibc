package org.bidib.jbidibc.node.listener;

public interface TransferListener {
    void receiveStarted();

    void receiveStopped();

    void sendStarted();

    void sendStopped();
}
