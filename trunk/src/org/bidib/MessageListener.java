package org.bidib;

import java.util.Collection;

public interface MessageListener {
    void address(byte[] address, int detectorNumber, Collection<AddressData> addressData);

    void confidence(byte[] address, int valid, int freeze, int signal);

    void free(byte[] address, int detectorNumber);

    void key(byte[] address, int keyNumber, int keyState);

    void nodeLost(Node node);

    void nodeNew(Node node);

    void occupied(byte[] address, int detectorNumber);

    void timeout(byte[] address, int timeout);
}
