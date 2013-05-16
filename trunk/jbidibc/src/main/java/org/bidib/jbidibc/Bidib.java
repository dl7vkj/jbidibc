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
import java.io.InputStream;
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

public final class Bidib {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bidib.class);

    public static final int DEFAULT_TIMEOUT = 3000;

    private NodeFactory nodeFactory;

    private SerialPort port;

    private Semaphore portSemaphore = new Semaphore(1);

    private Semaphore sendSemaphore = new Semaphore(1);

    private String logFile;

    private boolean librariesLoaded;

    private static final Bidib instance = new Bidib();

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
        return instance;
    }

    public void close() throws IOException {
        if (port != null) {
            LOGGER.debug("Close the port.");
            long start = System.currentTimeMillis();

            // no longer process received messages
            MessageReceiver.disable();

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

    private void clearInputStream(SerialPort serialPort) {

        // get and clear stream

        try {
            InputStream serialStream = serialPort.getInputStream();
            // purge contents, if any
            int count = serialStream.available();
            LOGGER.debug("input stream shows {} bytes available", count);
            while (count > 0) {
                serialStream.skip(count);
                count = serialStream.available();
            }
        }
        catch (IOException e) {
            LOGGER.warn("Clear input stream failed.", e);
        }

    }

    private SerialPort internalOpen(CommPortIdentifier commPort, int baudRate) throws PortInUseException,
        UnsupportedCommOperationException, TooManyListenersException {
        SerialPort serialPort = (SerialPort) commPort.open(Bidib.class.getName(), 2000);

        serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        // set RTS high, DTR high - done early, so flow control can be configured after
        serialPort.setRTS(true); // not connected in some serial ports and adapters
        serialPort.setDTR(true); // pin 1 in DIN8; on main connector, this is DTR

        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

        serialPort.enableReceiveThreshold(1);
        serialPort.enableReceiveTimeout(DEFAULT_TIMEOUT);

        clearInputStream(serialPort);

        try {
            serialPort.notifyOnFramingError(true);
        }
        catch (Exception e) {
            LOGGER.debug("Could not notifyOnFramingError: " + e);
        }

        try {
            serialPort.notifyOnBreakInterrupt(true);
        }
        catch (Exception e) {
            LOGGER.debug("Could not notifyOnBreakInterrupt: " + e);
        }

        try {
            serialPort.notifyOnParityError(true);
        }
        catch (Exception e) {
            LOGGER.debug("Could not notifyOnParityError: " + e);
        }

        try {
            serialPort.notifyOnOutputEmpty(true);
        }
        catch (Exception e) {
            LOGGER.debug("Could not notifyOnOutputEmpty: " + e);
        }

        try {
            serialPort.notifyOnOverrunError(true);
        }
        catch (Exception e) {
            LOGGER.debug("Could not notifyOnOverrunError.", e);
        }

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
                switch (event.getEventType()) {
                    case SerialPortEvent.DATA_AVAILABLE:
                        try {
                            messageReceiver.receive(port);
                        }
                        catch (Exception ex) {
                            LOGGER.error("Message receiver has terminated with an exception!", ex);
                        }
                        break;
                    case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                        LOGGER.trace("The output buffer is empty.");
                        break;
                    default:
                        LOGGER.warn("SerialPortEvent was triggered, type: {}, old value: {}, new value: {}",
                            new Object[] { event.getEventType(), event.getOldValue(), event.getNewValue() });
                        break;
                }
            }
        });
        serialPort.notifyOnDataAvailable(true);

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
            if (portName == null || portName.trim().isEmpty()) {
                throw new PortNotFoundException("");
            }
            loadLibraries();
            LOGGER.debug("Open port with name: {}", portName);

            File file = new File(portName);

            if (file.exists()) {
                portName = file.getCanonicalPath();
                LOGGER.info("Changed port name to: {}", portName);
            }

            CommPortIdentifier commPort = CommPortIdentifier.getPortIdentifier(portName);

            try {
                portSemaphore.acquire();
                // // 1000000 Baud
                // close();
                // port = open(commPort, 1000000);
                // sendMagic();
                // } catch (Exception e) {
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
                throw new RuntimeException("Send message to output stream failed.", e);
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
