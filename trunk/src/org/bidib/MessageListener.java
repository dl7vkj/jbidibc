package org.bidib;

public interface MessageListener {
    void free(byte[] address, int detectorNumber);

    void key(byte[] address, int keyNumber, int keyState);

    void nodeLost(Node node);

    void nodeNew(Node node);

    void occupied(byte[] address, int detectorNumber);
}
