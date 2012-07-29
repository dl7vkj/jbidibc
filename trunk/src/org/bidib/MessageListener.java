package org.bidib;


public interface MessageListener {
    void nodeLost(Node node);

    void nodeNew(Node node);
}
