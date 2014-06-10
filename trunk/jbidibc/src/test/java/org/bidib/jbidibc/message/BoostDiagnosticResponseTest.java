package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BoostDiagnosticResponseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoostDiagnosticResponseTest.class);

    @Test
    public void BoostDiagnosticResponse() throws ProtocolException {
        byte[] nodeAddress = { 0 };
        BoostDiagnosticResponse bidibMessage = new BoostDiagnosticResponse(nodeAddress, 1, 0x44, 0xA9, 0x13);

        Assert.assertNotNull(bidibMessage);

        LOGGER.info("prepared message: {}", bidibMessage);

        Assert.assertTrue(bidibMessage instanceof BoostDiagnosticResponse,
            "Expected a BoostDiagnosticResponse message.");
        BoostDiagnosticResponse response = (BoostDiagnosticResponse) bidibMessage;
        LOGGER.info("Received current: {}, voltage: {}, temp: {}", response.getCurrent(), response.getVoltage(),
            response.getTemperature());
        Assert.assertEquals(response.getCurrent(), 272);
        Assert.assertEquals(response.getVoltage(), 169);
        Assert.assertEquals(response.getTemperature(), 19);
    }
}
