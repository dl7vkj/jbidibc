package org.bidib.jbidibc.net;

import java.net.InetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BidibNetAddress {

    private final InetAddress address;

    private final int portNumber;

    private int sessionKey;

    private int sequence;

    public BidibNetAddress(final InetAddress address, final int portNumber) {
        this.address = address;
        this.portNumber = portNumber;
    }

    /**
     * @return the address
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * @return the portNumber
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * @return the sessionKey
     */
    public int getSessionKey() {
        return sessionKey;
    }

    /**
     * @param sessionKey
     *            the sessionKey to set
     */
    public void setSessionKey(int sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * @return the sequence
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * @param sequence
     *            the sequence to set
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * Increment the sequence.
     */
    public void nextSequence() {
        sequence++;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof BidibNetAddress) {
            BidibNetAddress bidibHost = (BidibNetAddress) other;
            // TODO don't compare the session key?
            if (bidibHost.getAddress().equals(address) && bidibHost.getPortNumber() == portNumber) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
