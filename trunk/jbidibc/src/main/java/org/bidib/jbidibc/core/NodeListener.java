package org.bidib.jbidibc.core;

/**
 * The node listener interface handles changes in the node list.
 */
public interface NodeListener {

    /**
     * Signals that a node was lost in the system.
     * 
     * @param node
     *            the lost node
     */
    void nodeLost(Node node);

    /**
     * Signals that a new node was found in the system.
     * 
     * @param node
     *            the new node
     */
    void nodeNew(Node node);
}
