package org.bidib;

public interface MessageListener {
    void confidence(byte[] address, int valid, int freeze, int signal);

    void free(byte[] address, int detectorNumber);

    void key(byte[] address, int keyNumber, int keyState);

    void nodeLost(Node node);

    void nodeNew(Node node);

    void occupied(byte[] address, int detectorNumber);
}
