package org.bidib.jbidibc;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.Semaphore;

import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.node.AccessoryNode;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.CommandStationNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.bidib.jbidibc.node.RootNode;
import org.bidib.jbidibc.utils.LibraryPathManipulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bidib {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bidib.class);

    public static final int DEFAULT_TIMEOUT = 3000;

    private NodeFactory nodeFactory;

    private SerialPort port = null;

    private Semaphore portSemaphore = new Semaphore(1);

    private Semaphore sendSemaphore = new Semaphore(1);

    private String logFile;

    private boolean librariesLoaded;

    private static Bidib INSTANCE;

    private MessageReceiver messageReceiver;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    LOGGER.debug("Disable the message receiver.");
                    MessageReceiver.disable();
                    getInstance().close();
                }
                catch (IOException e) {
                }
            }
        });
    }

    private Bidib() {
        nodeFactory = new NodeFactory();
        // create the message receiver
        messageReceiver = new MessageReceiver(nodeFactory);
    }

    public static synchronized Bidib getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Bidib();
        }
        return INSTANCE;
    }

    public void close() throws IOException {
        if (port != null) {
            LOGGER.debug("Close the port.");
            long start = System.currentTimeMillis();

            // this makes the close operation faster ...
            try {
                port.removeEventListener();
                port.enableReceiveTimeout(200);
            }
            catch (UnsupportedCommOperationException e) {
                // ignore
            }
            port.close();

            long end = System.currentTimeMillis();
            LOGGER.debug("Closed the port. duration: {}", end - start);

            port = null;

            if (nodeFactory != null) {
                // remove all stored nodes from the node factory
                nodeFactory.reset();
            }
        }
    }

    private boolean isValidPortName(String portName) {
        if (portName != null && !portName.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public List<CommPortIdentifier> getPortIdentifiers() {
        List<CommPortIdentifier> portIdentifiers = new ArrayList<CommPortIdentifier>();

        // make sure the libraries are loaded
        loadLibraries();

        // get the comm port identifiers
        Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();
        while (e.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) e.nextElement();

            if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portIdentifiers.add(id);
            }
        }
        return portIdentifiers;
    }

    public AccessoryNode getAccessoryNode(Node node) {
        return nodeFactory.getAccessoryNode(node);
    }

    public CommandStationNode getCommandStationNode(Node node) {
        return nodeFactory.getCommandStationNode(node);
    }

    public MessageReceiver getMessageReceiver() {
        return messageReceiver;
    }

    /**
     * returns the cached node or creates a new instance
     * 
     * @param node
     *            the node
     * @return the BidibNode instance
     */
    public BidibNode getNode(Node node) {
        return nodeFactory.getNode(node);
    }

    public RootNode getRootNode() {
        return nodeFactory.getRootNode();
    }

    private SerialPort internalOpen(CommPortIdentifier commPort, int baudRate) throws PortInUseException,
        UnsupportedCommOperationException, TooManyListenersException {
        SerialPort serialPort = (SerialPort) commPort.open(Bidib.class.getName(), 2000);

        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
        serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        serialPort.enableReceiveThreshold(1);
        serialPort.enableReceiveTimeout(DEFAULT_TIMEOUT);
        serialPort.notifyOnDataAvailable(true);
        MessageReceiver.enable();

        serialPort.addEventListener(new SerialPortEventListener() {
            {
                if (logFile != null) {
                    LOGGER.info("Logfile is set");
                    try {
                        new LogFileAnalyzer(new File(logFile), messageReceiver);
                    }
                    catch (IOException e) {
                        LOGGER.warn("Create LogFileAnalyzer failed.", e);
                    }
                }
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                // this callback is called every time data is available
                LOGGER.trace("serialEvent received: {}", event);
                if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    try {
                        // new MessageReceiver(port, nodeFactory);
                        messageReceiver.receive(port);
                    }
                    catch (Exception ex) {
                        LOGGER.error("Message receiver has terminated with an exception!", ex);
                    }
                }
            }
        });
        return serialPort;
    }

    private void loadLibraries() {
        if (!librariesLoaded) {
            new LibraryPathManipulator().manipulateLibraryPath(null);
            librariesLoaded = true;
        }
    }

    public void open(String portName) throws PortNotFoundException, PortInUseException,
        UnsupportedCommOperationException, IOException, ProtocolException, InterruptedException,
        TooManyListenersException, NoSuchPortException {
        if (port == null) {
            if (portName == null) {
                throw new PortNotFoundException("");
            }
            loadLibraries();
            LOGGER.info("Open port with name: {}", portName);

            File file = new File(portName);

            if (file.exists()) {
                portName = file.getCanonicalPath();
            }

            CommPortIdentifier commPort = CommPortIdentifier.getPortIdentifier(portName);

            try {
                portSemaphore.acquire();
                try {
                    // 115200 Baud
                    close();
                    port = internalOpen(commPort, 115200);
                    sendMagic();
                }
                catch (Exception e2) {
                    try {
                        // 19200 Baud
                        close();
                        port = internalOpen(commPort, 19200);
                        sendMagic();
                    }
                    catch (Exception e3) {
                        close();
                    }
                }
            }
            finally {
                portSemaphore.release();
            }
        }
        else {
            LOGGER.warn("Port is already opened.");
        }
    }

    public void send(final byte[] bytes) {
        if (port != null) {
            try {
                sendSemaphore.acquire();

                OutputStream output = port.getOutputStream();

                port.setRTS(true);
                output.write(bytes);
                output.flush();
                port.setRTS(false);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                sendSemaphore.release();
            }
        }
    }

    private int sendMagic() throws IOException, ProtocolException, InterruptedException {
        BidibNode rootNode = getRootNode();

        // Ignore the first exception ...
        try {
            rootNode.getMagic();
        }
        catch (Exception e) {
        }
        return rootNode.getMagic();
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public void setTimeout(int timeout) {
        if (port != null) {
            try {
                port.enableReceiveTimeout(timeout);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
