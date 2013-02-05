package org.bidib.jbidibc.node;

import java.math.BigInteger;

import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.exception.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * NodeFactoryTest
 */
public class NodeFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeFactoryTest.class);

    @Test
    public void getClassIdTest() {
        BigInteger bigInt = new BigInteger("d2000d68000100", 16);
        long uniqueId = bigInt.longValue();
        boolean hasCS = ((uniqueId >> 48) & 16) == 16;

        LOGGER.info("hasCS: {}", hasCS);

        Assert.assertTrue(hasCS);
    }

    @Test
    public void getRootNodeTest() {
        NodeFactory nodeFactory = new NodeFactory();
        RootNode rootNode = nodeFactory.getRootNode();

        Assert.assertNotNull(rootNode);
        Assert.assertEquals(rootNode.getAddr()[0], 0);
    }

    @Test
    public void getCommandStationNodeTest() {
        NodeFactory nodeFactory = new NodeFactory();
        RootNode rootNode = nodeFactory.getRootNode();

        Assert.assertNotNull(rootNode);
        Assert.assertEquals(rootNode.getAddr()[0], 0);

        BigInteger bigInt = new BigInteger("d2000d68000100", 16);
        long uniqueId = bigInt.longValue();
        Node node = new Node(1, new byte[] { 0, 0, 0, 0 }, uniqueId);
        CommandStationNode commandStationNode = nodeFactory.getCommandStationNode(node);

        Assert.assertNotNull(commandStationNode);
    }

    @Test(expectedExceptions = { InvalidConfigurationException.class })
    public void getCommandStationNodeInvalidTest() {
        NodeFactory nodeFactory = new NodeFactory();
        RootNode rootNode = nodeFactory.getRootNode();

        Assert.assertNotNull(rootNode);
        Assert.assertEquals(rootNode.getAddr()[0], 0);

        BigInteger bigInt = new BigInteger("c0000d68000100", 16);
        long uniqueId = bigInt.longValue();
        Node node = new Node(1, new byte[] { 0, 0, 0, 0 }, uniqueId);
        CommandStationNode commandStationNode = nodeFactory.getCommandStationNode(node);

        Assert.assertNotNull(commandStationNode);
    }
}
