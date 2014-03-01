package org.bidib.jbidibc.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.ConnectionListener;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.core.AbstractBidib;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.PortNotOpenedException;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetBidib extends AbstractBidib {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetBidib.class);

    public static final int BIDIB_UDP_PORT_NUMBER = 62875;

    private static NetBidib INSTANCE;

    private NetBidibPort port;

    private NetMessageHandler netMessageReceiver;

    private Thread portWorker;

    private String connectedPortName;

    private InetAddress address;

    private int portNumber;

    // private int sessionKey;
    //
    // private int sequence;

    // ////////////////////////////////////////////////////////////////////////////
    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    LOGGER.debug("Close the communication ports and perform cleanup.");
                    getInstance().close();
                }
                catch (Exception e) {
                }
            }
        });
    }

    private NetBidib() {

    }

    @Override
    protected MessageReceiver createMessageReceiver(NodeFactory nodeFactory) {
        return new MessageReceiver(nodeFactory);
    }

    public static synchronized BidibInterface getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetBidib();
            INSTANCE.initialize();
        }
        return INSTANCE;
    }

    @Override
    public void open(String portName, ConnectionListener connectionListener) throws PortNotFoundException,
        PortNotOpenedException {
        LOGGER.info("Open port: {}", portName);

        setConnectionListener(connectionListener);

        if (port == null) {
            if (portName == null || portName.trim().isEmpty()) {
                throw new PortNotFoundException("");
            }

            connectedPortName = portName;

            try {
                close();
                port = internalOpen(connectedPortName);
                LOGGER.info("Port is opened, send the magic. The connected port is: {}", connectedPortName);
                sendMagic();
            }
            catch (Exception ex) {
                LOGGER.warn("Open port and send magic failed.", ex);
            }
        }
        else {
            LOGGER.warn("Port is already opened.");
        }
    }

    private NetBidibPort internalOpen(String portName) throws SocketException, UnknownHostException {

        String[] hostAndPort = portName.split(":");

        address = InetAddress.getByName(hostAndPort[0]);
        portNumber = Integer.parseInt(hostAndPort[1]);

        LOGGER.info("Configured address: {}, portNumber: {}", address, portNumber);

        // enable the message receiver before the event listener is added
        getMessageReceiver().enable();

        netMessageReceiver = new DefaultNetMessageHandler(getMessageReceiver());
        netMessageReceiver.addRemoteAddress(address, portNumber);

        DatagramSocket datagramSocket = new DatagramSocket();
        // open the port
        NetBidibPort netBidibPort = new NetBidibPort(datagramSocket, netMessageReceiver);

        LOGGER.info("Prepare and start the port worker.");

        portWorker = new Thread(netBidibPort);
        portWorker.start();

        return netBidibPort;
    }

    @Override
    public boolean isOpened() {
        return port != null;
    }

    @Override
    public void close() {
        LOGGER.info("Close the port.");

        if (port != null) {
            LOGGER.info("Stop the port.");
            port.stop();

            if (portWorker != null) {
                synchronized (portWorker) {
                    try {
                        portWorker.join(5000L);
                    }
                    catch (InterruptedException ex) {
                        LOGGER.warn("Wait for termination of port worker failed.", ex);
                    }
                    portWorker = null;
                }
            }

            port = null;
        }
        LOGGER.info("Close the port finished.");
    }

    @Override
    public void send(byte[] bytes) {
        LOGGER.info("Send message: {}", ByteUtils.bytesToHex(bytes));
        if (port != null) {
            // forward the message to the netMessageReceiver
            try {
                netMessageReceiver.send(port, bytes);
            }
            catch (Exception ex) {
                LOGGER.warn("Forward message to send with netMessageReceiver failed.", ex);
                throw new RuntimeException("Forward message to send with netMessageReceiver failed.", ex);
            }
            // try {
            // // TODO add the UDP packet wrapper ...
            // ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // bos.write(ByteUtils.getHighByte(sessionKey));
            // bos.write(ByteUtils.getLowByte(sessionKey));
            // bos.write(ByteUtils.getHighByte(sequence));
            // bos.write(ByteUtils.getLowByte(sequence));
            // bos.write(bytes);
            //
            // port.send(bos.toByteArray(), address, portNumber);
            //
            // // increment the sequence counter after sending the message sucessfully
            // prepareNextSequence();
            // }
            // catch (IOException ex) {
            // LOGGER.warn("Send message to port failed.", ex);
            // throw new RuntimeException("Send message to datagram socket failed.", ex);
            // }
        }
        else {
            LOGGER.warn("Send not possible, the port is closed.");
        }
    }

    // private void prepareNextSequence() {
    // sequence++;
    // if (sequence > 65535) {
    // sequence = 0;
    // }
    // }
    //
    /**
     * Get the magic from the root node
     * 
     * @return the magic provided by the root node
     * @throws ProtocolException
     */
    private int sendMagic() throws ProtocolException {
        BidibNode rootNode = getRootNode();

        // Ignore the first exception ...
        int magic = -1;
        try {
            magic = rootNode.getMagic();
        }
        catch (Exception e) {
            magic = rootNode.getMagic();
        }
        // int magic = rootNode.getMagic();
        LOGGER.debug("The node returned magic: {}", magic);
        return magic;
    }

    @Override
    public void setReceiveTimeout(int timeout) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLogFile(String logFile) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<String> getPortIdentifiers() {
        // TODO Auto-generated method stub
        return null;
    }
}
