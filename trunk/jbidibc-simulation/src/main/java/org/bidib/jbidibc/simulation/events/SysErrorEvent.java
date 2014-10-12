package org.bidib.jbidibc.simulation.events;

import org.bidib.jbidibc.core.enumeration.SysErrorEnum;

public class SysErrorEvent {

    private final String nodeAddr;

    private final SysErrorEnum sysError;

    private byte[] reason;

    public SysErrorEvent(final String nodeAddr, final SysErrorEnum sysError, final byte... reason) {
        this.nodeAddr = nodeAddr;
        this.sysError = sysError;
        this.reason = reason;
    }

    public String getNodeAddr() {
        return nodeAddr;
    }

    public SysErrorEnum getSysError() {
        return sysError;
    }

    public byte[] getReason() {
        return reason;
    }
}
