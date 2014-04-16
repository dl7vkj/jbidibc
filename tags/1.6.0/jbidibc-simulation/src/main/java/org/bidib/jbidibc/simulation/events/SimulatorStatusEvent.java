package org.bidib.jbidibc.simulation.events;

public class SimulatorStatusEvent {

    private final String nodeAddr;

    private final Status status;

    public enum Status {
        started, stopped;
    }

    public SimulatorStatusEvent(final String nodeAddr, final Status status) {
        this.nodeAddr = nodeAddr;
        this.status = status;
    }

    /**
     * @return the nodeAddr
     */
    public String getNodeAddr() {
        return nodeAddr;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

}
