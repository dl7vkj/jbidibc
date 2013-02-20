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
        this.addr = addr;
        this.uniqueId = 0;
        this.version = 0;
    }

    public Node(int version, byte[] addr, long uniqueId) {
        this.addr = addr;
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

    /**
     * @return returns the uniqueId of the node as formatted hex string
     */
    public String getUniqueIdAsString() {
        return String.format("0x%014x", (uniqueId & 0xffffffffffffffL));
    }

    /**
     * @return returns if node supports accessory functions
     */
    public boolean hasAccessoryFunctions() {
        return ((uniqueId >> 48) & 4) == 4;
    }

    /**
     * @return returns if node supports booster functions
     */
    public boolean hasBoosterFunctions() {
        return ((uniqueId >> 48) & 2) == 2;
    }

    /**
     * @return returns if node supports feedback functions
     */
    public boolean hasFeedbackFunctions() {
        return ((uniqueId >> 48) & 64) == 64;
    }

    /**
     * @return returns if node supports subnodes functions
     */
    public boolean hasSubNodesFunctions() {
        return ((uniqueId >> 48) & 128) == 128;
    }

    /**
     * @return returns if node supports switch functions
     */
    public boolean hasSwitchFunctions() {
        return ((uniqueId >> 48) & 1) == 1;
    }

    /**
     * @return returns if node supports command station programming functions
     */
    public boolean hasCommandStationProgrammingFunctions() {
        return ((uniqueId >> 48) & 8) == 8;
    }

    /**
     * @return returns if node supports command station functions
     */
    public boolean hasCommandStationFunctions() {
        return ((uniqueId >> 48) & 16) == 16;
    }

    public String toString() {
        return getClass().getSimpleName() + "[version=" + version + ",addr=" + Arrays.toString(addr) + ",uniqueId="
            + getUniqueIdAsString() + "]";
    }
}
