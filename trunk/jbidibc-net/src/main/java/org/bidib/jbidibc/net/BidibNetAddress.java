package org.bidib.jbidibc.net;

import java.net.InetAddress;

public class BidibNetAddress {

    private final InetAddress address;

    private final int portNumber;

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

    @Override
    public boolean equals(Object other) {
        if (other instanceof BidibNetAddress) {
            BidibNetAddress bidibHost = (BidibNetAddress) other;
            if (bidibHost.getAddress().equals(address) && bidibHost.getPortNumber() == portNumber) {
                return true;
            }
        }
        return false;
    }

}
