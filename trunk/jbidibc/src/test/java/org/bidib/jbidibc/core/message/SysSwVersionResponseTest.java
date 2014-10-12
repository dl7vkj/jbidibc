package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.SoftwareVersion;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.message.SysSwVersionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SysSwVersionResponseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysSwVersionResponseTest.class);

    @Test
    public void getVersion() throws ProtocolException {
        SysSwVersionResponse swVersionResponse =
            new SysSwVersionResponse(new byte[] { 2 }, 1, SysSwVersionResponse.TYPE, new byte[] { (byte) 255, 2, 1 });
        SoftwareVersion softwareVersion = swVersionResponse.getVersion();

        Assert.assertNotNull(softwareVersion);
        LOGGER.info("Returned softwareVersion: {}", softwareVersion);
        Assert.assertEquals("1.2.255", softwareVersion.toString());
    }
}
