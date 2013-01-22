package org.bidib.jbidibc;

import java.util.Arrays;

public class Node {
    private final byte[] addr;
    private final long uniqueId;
    private final int version;

    public Node(byte[] addr) {
        this.addr = addr;
        this.uniqueId = 0;
        this.version = 0;
    }

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

    public boolean hasAccessoryFunctions() {
        return ((uniqueId >> 48) & 4) == 4;
    }

    public boolean hasBoosterFunctions() {
        return ((uniqueId >> 48) & 2) == 2;
    }

    public boolean hasFeedbackFunctions() {
        return ((uniqueId >> 48) & 64) == 64;
    }

    public boolean hasSubNodesFunctions() {
        return ((uniqueId >> 48) & 128) == 128;
    }

    public boolean hasSwitchFunctions() {
        return ((uniqueId >> 48) & 1) == 1;
    }

    public String toString() {
        return getClass().getSimpleName() + "[version=" + version + ",addr=" + Arrays.toString(addr) + ",uniqueId="
                + getUniqueIdAsString() + "]";
    }
}
