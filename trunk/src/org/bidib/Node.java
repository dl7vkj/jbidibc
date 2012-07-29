package org.bidib;

import java.util.Arrays;

public class Node {
    private byte[] addr;
    private long uniqueId;
    private int version;

    public Node(int version, byte[] addr, long uniqueId) {
        this.addr = addr;
        this.uniqueId = uniqueId;
        this.version = version;
    }

    public byte[] getAddr() {
        return addr;
    }

    public int getVersion() {
        return version;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public String getUniqueIdAsString() {
        return String.format("0x%014x", (uniqueId & 0xffffffffffffffL));
    }

    public String toString() {
        return getClass().getSimpleName() + "[version=" + version + ",addr=" + Arrays.toString(addr) + ",uniqueId="
                + getUniqueIdAsString() + "]";
    }
}
