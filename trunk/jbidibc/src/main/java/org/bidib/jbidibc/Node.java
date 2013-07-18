package org.bidib.jbidibc;

import java.util.Arrays;

/**
 * This class represents a Node in BiDiB
 */
public class Node {
    private final byte[] addr;

    private final long uniqueId;

    private final int version;

    public Node(byte[] addr) {
        this.addr = addr != null ? addr.clone() : null;
        this.uniqueId = 0;
        this.version = 0;
    }

    public Node(int version, byte[] addr, long uniqueId) {
        this.addr = addr != null ? addr.clone() : null;
        this.uniqueId = uniqueId;
        this.version = version;
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

    public String toString() {
        return getClass().getSimpleName() + "[version=" + version + ",addr=" + Arrays.toString(addr) + ",uniqueId="
            + String.format("0x%014x", uniqueId & 0xffffffffffffffL) + "]";
    }
}
