package org.bidib.jbidibc;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CRC8Test {

    private byte[] bytes = new byte[] {
        // @formatter:off
        0x00, 0x5e, 0x1c, 0x12, 0x61, 0x3f, 0x1d, 0x23, 0x02, 0x0c, 0x7e, 0x20, 0x03, 0x0d, 0x1f, 0x41
    // @formatter:on
        };

    @Test(description = "Get valid values of CRC8")
    public void getCrc() {
        Assert.assertEquals(CRC8.getCrc(bytes, 0, 3), 15);
        Assert.assertEquals(CRC8.getCrc(bytes, 0, 0), 0);
    }

    @Test(expectedExceptions = { ArrayIndexOutOfBoundsException.class })
    public void getCrcOutOfBounds() {

        Assert.assertEquals(CRC8.getCrc(bytes, 0, 20), 0);
    }

    @Test
    public void getCrcValue() {
        Assert.assertEquals(CRC8.getCrcValue(0), 0);
    }

    @Test(expectedExceptions = { ArrayIndexOutOfBoundsException.class })
    public void getCrcValueOutOfBounds() {
        CRC8.getCrcValue(300);
    }
}
