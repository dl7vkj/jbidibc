package org.bidib.jbidibc.simulation.events;

import org.apache.commons.lang.builder.ToStringBuilder;

public class NodeAvailableEvent {

    private final byte[] nodeAddr;

    private final long uniqueId;

    public NodeAvailableEvent(byte[] nodeAddr, long uniqueId) {
        this.nodeAddr = nodeAddr;
        this.uniqueId = uniqueId;
    }

    public byte[] getNodeAddr() {
        return nodeAddr;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
