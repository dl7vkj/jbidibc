package org.bidib;

public interface MessageListener {
    void key(byte[] address, int keyNumber, int keyState);

    void nodeLost(Node node);

    void nodeNew(Node node);
}
