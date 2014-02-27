package org.bidib.jbidibc.node;

import java.math.BigInteger;

import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.exception.InvalidConfigurationException;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * NodeFactoryTest
 */
public class NodeFactoryTest {

    private BidibInterface bidib;

    @BeforeTest
    public void prepare() {
        bidib = Mockito.mock(BidibInterface.class);
    }

    @Test
    public void getRootNodeTest() {
        NodeFactory nodeFactory = new NodeFactory();
        nodeFactory.setBidib(bidib);

        RootNode rootNode = nodeFactory.getRootNode();

        Assert.assertNotNull(rootNode);
        Assert.assertEquals(rootNode.getAddr()[0], 0);
    }

    @Test
    public void getCommandStationNodeTest() {
        NodeFactory nodeFactory = new NodeFactory();
        nodeFactory.setBidib(bidib);

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
        nodeFactory.setBidib(bidib);

        RootNode rootNode = nodeFactory.getRootNode();

        Assert.assertNotNull(rootNode);
        Assert.assertEquals(rootNode.getAddr()[0], 0);

        BigInteger bigInt = new BigInteger("c0000d68000100", 16);
        long uniqueId = bigInt.longValue();
        Node node = new Node(1, new byte[] { 0, 0, 0, 0 }, uniqueId);

        // this call fails with the exception
        nodeFactory.getCommandStationNode(node);
    }

    @Test
    public void getBoosterNodeTest() {
        NodeFactory nodeFactory = new NodeFactory();
        nodeFactory.setBidib(bidib);

        RootNode rootNode = nodeFactory.getRootNode();

        Assert.assertNotNull(rootNode);
        Assert.assertEquals(rootNode.getAddr()[0], 0);

        BigInteger bigInt = new BigInteger("d2000d68000100", 16);
        long uniqueId = bigInt.longValue();
        Node node = new Node(1, new byte[] { 0, 0, 0, 0 }, uniqueId);
        BoosterNode boosterNode = nodeFactory.getBoosterNode(node);

        Assert.assertNotNull(boosterNode);
    }

    @Test(expectedExceptions = { InvalidConfigurationException.class })
    public void getBoosterNodeInvalidTest() {
        NodeFactory nodeFactory = new NodeFactory();
        nodeFactory.setBidib(bidib);

        RootNode rootNode = nodeFactory.getRootNode();

        Assert.assertNotNull(rootNode);
        Assert.assertEquals(rootNode.getAddr()[0], 0);

        BigInteger bigInt = new BigInteger("d0000d68000100", 16);
        long uniqueId = bigInt.longValue();
        Node node = new Node(1, new byte[] { 0, 0, 0, 0 }, uniqueId);

        // this call fails with the exception
        nodeFactory.getBoosterNode(node);
    }
}
