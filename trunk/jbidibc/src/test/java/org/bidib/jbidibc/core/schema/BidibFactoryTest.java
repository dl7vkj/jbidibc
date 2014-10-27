package org.bidib.jbidibc.core.schema;

import java.util.List;

import org.bidib.jbidibc.core.schema.bidib.MessageType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BidibFactoryTest {

    @Test
    public void loadMessageTypesTest() {
        List<MessageType> messageTypes = BidibFactory.getMessageTypes();
        Assert.assertNotNull(messageTypes);
        Assert.assertEquals(messageTypes.size(), 121);
    }
}
