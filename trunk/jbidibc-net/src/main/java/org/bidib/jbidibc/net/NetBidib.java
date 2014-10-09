package org.bidib.jbidibc.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.ConnectionListener;
import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.NodeListener;
import org.bidib.jbidibc.core.AbstractBidib;
import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.core.Context;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.PortNotOpenedException;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.bidib.jbidibc.node.listener.TransferListener;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetBidib extends AbstractBidib {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetBidib.class);

    public static final int BIDIB_UDP_PORT_NUMBER = 62875;

    private static NetBidib INSTANCE;

    private NetBidibPort port;

    private NetMessageHandler netMessageHandler;

    private Thread portWorker;

    private String connectedPortName;

    private InetAddress address;

    private int portNumber;

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

    protected NetBidib() {

    }

    @Override
    protected BidibMessageProcessor createMessageReceiver(NodeFactory nodeFactory) {
        return new MessageReceiver(nodeFactory);
    }

    private MessageReceiver getNetMessageReceiver() {
        return (MessageReceiver) getMessageReceiver();
    }

    public static synchronized BidibInterface getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetBidib();
            INSTANCE.initialize();
        }
        return INSTANCE;
    }

    @Override
    public void open(
        String portName, ConnectionListener connectionListener, Set<NodeListener> nodeListeners,
        Set<MessageListener> messageListeners, Set<TransferListener> transferListeners, final Context context)
        throws PortNotFoundException, PortNotOpenedException {

        LOGGER.info("Open port: {}", portName);

        setConnectionListener(connectionListener);

        // register the listeners
        registerListeners(nodeListeners, messageListeners, transferListeners);

        if (port == null) {
            LOGGER.info("Open port with name: {}", portName);
            if (portName == null || portName.trim().isEmpty()) {
                throw new PortNotFoundException("");
            }

            if (portName.indexOf(":") < 0) {
                portName += ":" + NetBidib.BIDIB_UDP_PORT_NUMBER;
                LOGGER.info("Added portnumber to portName: {}", portName);
            }

            try {
                close();
                port = internalOpen(portName, context);
                connectedPortName = portName;

                LOGGER.info("Port is opened, send the magic. The connected port is: {}", connectedPortName);
                sendMagic();
            }
            catch (Exception ex) {
                LOGGER.warn("Open port and send magic failed.", ex);

                throw new PortNotOpenedException(portName, PortNotOpenedException.UNKNOWN);
            }
        }
        else {
            LOGGER.warn("Port is already opened.");
        }
    }

    private NetBidibPort internalOpen(String portName, final Context context) throws SocketException,
        UnknownHostException {

        String[] hostAndPort = portName.split(":");

        address = InetAddress.getByName(hostAndPort[0]);
        portNumber = Integer.parseInt(hostAndPort[1]);

        LOGGER.info("Configured address: {}, portNumber: {}", address, portNumber);

        if (context != null) {
            Boolean ignoreWrongMessageNumber = context.get("ignoreWrongMessageNumber", Boolean.class, Boolean.FALSE);
            getNetMessageReceiver().setIgnoreWrongMessageNumber(ignoreWrongMessageNumber);
        }

        // enable the message receiver before the event listener is added
        getNetMessageReceiver().enable();

        netMessageHandler = new DefaultNetMessageHandler(getMessageReceiver(), address, portNumber);

        DatagramSocket datagramSocket = new DatagramSocket();
        // open the port
        NetBidibPort netBidibPort = new NetBidibPort(datagramSocket, netMessageHandler);

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

        if (getConnectionListener() != null) {
            getConnectionListener().closed(connectedPortName);
        }

        // clear the connectedPortName
        connectedPortName = null;

        LOGGER.info("Close the port finished.");
    }

    @Override
    public void send(byte[] bytes) {
        LOGGER.info("Send message to net message handler: {}, port: {}", ByteUtils.bytesToHex(bytes), port);
        if (port != null) {
            // forward the message to the netMessageReceiver
            try {
                netMessageHandler.send(port, bytes);
            }
            catch (Exception ex) {
                LOGGER.warn("Forward message to send with netMessageReceiver failed.", ex);
                throw new RuntimeException("Forward message to send with netMessageReceiver failed.", ex);
            }
        }
        else {
            LOGGER.warn("Send not possible, the port is closed.");
        }
    }

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
        LOGGER.debug("The node returned magic: {}", magic);
        return magic;
    }

    @Override
    public void setResponseTimeout(int timeout) {
        LOGGER.info("Set the response timeout for simulation to: {}", timeout * 1000);
        super.setResponseTimeout(timeout * 1000);
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
