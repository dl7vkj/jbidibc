package org.bidib.jbidibc.message;

import org.bidib.jbidibc.enumeration.AccessoryAcknowledge;
import org.bidib.jbidibc.exception.ProtocolException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CommandStationAccessoryAcknowledgeResponseTest {

    @Test
    public void getAddress() throws ProtocolException {
        byte[] addr = { 1, 2 };
        int num = 1;
        CommandStationAccessoryAcknowledgeResponse response =
            new CommandStationAccessoryAcknowledgeResponse(addr, num, CommandStationAccessoryAcknowledgeResponse.TYPE,
                (byte) 0x10, (byte) 0x20, (byte) 0);

        Assert.assertEquals(0x2010, response.getAddress());

        response =
            new CommandStationAccessoryAcknowledgeResponse(addr, num, CommandStationAccessoryAcknowledgeResponse.TYPE,
                (byte) 0x83, (byte) 0x84, (byte) 0);

        Assert.assertEquals(0x8483, response.getAddress());
    }

    @Test
    public void getAcknState() throws ProtocolException {
        byte[] addr = { 1, 2 };
        int num = 1;
        CommandStationAccessoryAcknowledgeResponse response =
            new CommandStationAccessoryAcknowledgeResponse(addr, num, CommandStationAccessoryAcknowledgeResponse.TYPE,
                (byte) 0x10, (byte) 0x20, (byte) 0);

        Assert.assertEquals(0x2010, response.getAddress());
        Assert.assertEquals(AccessoryAcknowledge.NOT_ACKNOWLEDGED, response.getAcknState());

        response =
            new CommandStationAccessoryAcknowledgeResponse(addr, num, CommandStationAccessoryAcknowledgeResponse.TYPE,
                (byte) 0x83, (byte) 0x84, (byte) 1);

        Assert.assertEquals(0x8483, response.getAddress());
        Assert.assertEquals(AccessoryAcknowledge.ACKNOWLEDGED, response.getAcknState());

        response =
            new CommandStationAccessoryAcknowledgeResponse(addr, num, CommandStationAccessoryAcknowledgeResponse.TYPE,
                (byte) 0x83, (byte) 0x84, (byte) 2);

        Assert.assertEquals(0x8483, response.getAddress());
        Assert.assertEquals(AccessoryAcknowledge.DELAYED, response.getAcknState());
    }
}
