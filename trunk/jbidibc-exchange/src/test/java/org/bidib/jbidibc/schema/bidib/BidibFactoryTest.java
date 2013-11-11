package org.bidib.jbidibc.schema.bidib;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.bidib.schema.bidib.Product;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class BidibFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidibFactoryTest.class);

    private static final long UUID_HUB = 0x81000d72006aeaL;

    private static final long UUID_LC = 0x05340d6B901238L;

    @Test
    public void loadProductForHub() throws JAXBException {

        File file = new File("");
        file = new File(file.getAbsoluteFile(), "target/generated-resources/xml/BiDiBProducts");
        LOGGER.info("Prepared file: {}", file.getAbsolutePath());

        Product product = new BidibFactory().loadProductForNode(UUID_HUB, file.getAbsolutePath());
        Assert.assertNotNull(product);
        LOGGER.debug("Returned product: {}", product);
        Assert.assertNotNull(product.getDocumentation());
        Assert.assertEquals(product.getDocumentation().size(), 2);
    }

    @Test
    public void loadProductForLC() throws JAXBException {

        File file = new File("");
        file = new File(file.getAbsoluteFile(), "target/generated-resources/xml/BiDiBProducts");
        LOGGER.info("Prepared file: {}", file.getAbsolutePath());

        Product product = new BidibFactory().loadProductForNode(UUID_LC, file.getAbsolutePath());
        Assert.assertNotNull(product);
        LOGGER.debug("Returned product: {}", product);
        Assert.assertNotNull(product.getDocumentation());
        Assert.assertEquals(product.getDocumentation().size(), 2);
    }
}
