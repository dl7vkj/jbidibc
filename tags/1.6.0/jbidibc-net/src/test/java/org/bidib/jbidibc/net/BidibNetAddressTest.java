package org.bidib.jbidibc.net;

import java.net.InetAddress;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BidibNetAddressTest {

    @Test
    public void equals() {
        BidibNetAddress address1 = new BidibNetAddress(InetAddress.getLoopbackAddress(), 1234);
        BidibNetAddress address2 = new BidibNetAddress(InetAddress.getLoopbackAddress(), 1234);

        Assert.assertEquals(address1, address2);

        BidibNetAddress address3 = new BidibNetAddress(InetAddress.getLoopbackAddress(), 2345);
        Assert.assertNotEquals(address1, address3);
    }

    @Test
    public void getAddress() {
        BidibNetAddress address1 = new BidibNetAddress(InetAddress.getLoopbackAddress(), 1234);

        Assert.assertEquals(address1.getAddress(), InetAddress.getLoopbackAddress());
    }

    @Test
    public void getPortNumber() {
        BidibNetAddress address1 = new BidibNetAddress(InetAddress.getLoopbackAddress(), 1234);

        Assert.assertEquals(address1.getPortNumber(), 1234);
    }
}
