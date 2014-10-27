package org.bidib.jbidibc.exchange.bidib;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.bidib.jbidibc.core.schema.BidibFactory;
import org.bidib.jbidibc.core.schema.bidib.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BidibFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidibFactoryTest.class);

    private static final long UUID_HUB = 0x81000d72006aeaL;

    private static final long UUID_LC = 0x05340d6B901238L;

    private static final long UUID_MULTIDEC = 0x05343E97901238L;

    private static final long UUID_INVALID_VID = 0x0534016B901238L;

    private static final long UUID_INVALID_PID = 0x05340d00901238L;

    @Test
    public void loadProductForHub() throws JAXBException {

        File file = new File("");
        file = new File(file.getAbsoluteFile(), "target/generated-resources/xml/BiDiBProducts");
        LOGGER.info("Prepared file: {}", file.getAbsolutePath());

        Product product = new BidibFactory().loadProductForNode(UUID_HUB, file.getAbsolutePath());
        Assert.assertNotNull(product);
        LOGGER.debug("Returned product: {}", product);
        Assert.assertEquals("OneHub", product.getName());
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
        Assert.assertEquals("LightControl 1", product.getName());
        Assert.assertNotNull(product.getDocumentation());
        Assert.assertEquals(product.getDocumentation().size(), 2);
    }

    @Test
    public void loadProductForMultiDecoder() throws JAXBException {

        File file = new File("");
        file = new File(file.getAbsoluteFile(), "target/generated-resources/xml/BiDiBProducts");
        LOGGER.info("Prepared file: {}", file.getAbsolutePath());

        Product product = new BidibFactory().loadProductForNode(UUID_MULTIDEC, file.getAbsolutePath());
        Assert.assertNotNull(product);
        LOGGER.debug("Returned product: {}", product);
        Assert.assertEquals("Multi-Decoder", product.getName());
        Assert.assertNotNull(product.getDocumentation());
        Assert.assertEquals(product.getDocumentation().size(), 2);
    }

    @Test
    public void loadProductForInvalidVID() throws JAXBException {

        File file = new File("");
        file = new File(file.getAbsoluteFile(), "target/generated-resources/xml/BiDiBProducts");
        LOGGER.info("Prepared file: {}", file.getAbsolutePath());

        Product product =
            new BidibFactory().loadProductForNode(UUID_INVALID_VID, file.getAbsolutePath(), "/BiDiBProducts");
        Assert.assertNull(product);
    }

    @Test
    public void loadProductForInvalidPID() throws JAXBException {

        File file = new File("");
        file = new File(file.getAbsoluteFile(), "target/generated-resources/xml/BiDiBProducts");
        LOGGER.info("Prepared file: {}", file.getAbsolutePath());

        Product product = new BidibFactory().loadProductForNode(UUID_INVALID_PID, file.getAbsolutePath());
        Assert.assertNull(product);
    }

}
