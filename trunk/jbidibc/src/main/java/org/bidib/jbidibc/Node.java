package org.bidib.jbidibc;

import java.util.Arrays;

import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a Node in BiDiB
 */
public class Node {
    private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);

    private final byte[] addr;

    private final long uniqueId;

    private final int version;

    protected Node(byte[] addr) {
        this.addr = addr != null ? addr.clone() : null;
        this.uniqueId = 0;
        this.version = 0;
    }

    /**
     * This constructor is used when the NodeTabResponse is evaluated.
     * @param version the version of the node
     * @param addr the address of the node 
     * @param uniqueId the uniqueId of the node
     */
    public Node(int version, byte[] addr, long uniqueId) {
        this.addr = addr != null ? addr.clone() : null;
        this.uniqueId = uniqueId;
        this.version = version;

        LOGGER.debug("Created new node, addr: {}, version: {}, {}", addr, version, NodeUtils
            .getUniqueIdAsString(uniqueId));
    }

    /**
     * @return returns address of node
     */
    public byte[] getAddr() {
        return addr;
    }

    /**
     * @return returns the version of the node
     */
    public int getVersion() {
        return version;
    }

    /**
     * @return returns the uniqueId of the node
     */
    public long getUniqueId() {
        return uniqueId;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Node) {
            Node node = (Node) other;
            if (ByteUtils.arrayEquals(node.getAddr(), getAddr()) && node.getVersion() == version
                && node.getUniqueId() == uniqueId) {
                return true;
            }
        }
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return (int) (NodeUtils.convertAddress(addr) + getUniqueId() + version);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb
            .append("[version=").append(version).append(",addr=").append(Arrays.toString(addr)).append(",uniqueId=")
            .append(String.format("0x%014x", uniqueId & 0xffffffffffffffL)).append("]");
        return sb.toString();
    }
}
