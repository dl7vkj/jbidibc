package org.bidib.jbidibc;

import java.util.Collection;

import org.bidib.jbidibc.enumeration.BoosterState;

public interface MessageListener {
    void address(byte[] address, int detectorNumber, Collection<AddressData> addressData);

    void boosterCurrent(byte[] address, int current);

    void boosterState(byte[] address, BoosterState state);

    void confidence(byte[] address, int valid, int freeze, int signal);

    void free(byte[] address, int detectorNumber);

    void key(byte[] address, int keyNumber, int keyState);

    void nodeLost(Node node);

    void nodeNew(Node node);

    void occupied(byte[] address, int detectorNumber);

    void speed(byte[] address, AddressData addressData, int speed);
}
