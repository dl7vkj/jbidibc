package org.bidib.jbidibc;

import gnu.io.CommPortIdentifier;
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

import org.bidib.jbidibc.enumeration.RxTxCommPortType;
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

    private static NodeFactory nodeFactory = new NodeFactory();

    private static SerialPort port = null;

    private static Semaphore portSemaphore = new Semaphore(1);

    private static Semaphore sendSemaphore = new Semaphore(1);

    private static String logFile = null;

    private static boolean librariesLoaded = false;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    LOGGER.debug("Disable the message receiver.");
                    MessageReceiver.disable();
                    close();
                }
                catch (IOException e) {
                }
            }
        });
    }

    public static void close() throws IOException {
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
        }
    }

    private static CommPortIdentifier findPort(String portName) {
        CommPortIdentifier result = null;
        LOGGER.info("Searching for port with name: {}", portName);

        if (portName != null) {
            // make sure the libraries are loaded
            loadLibraries();

            Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();

            while (e.hasMoreElements()) {
                final CommPortIdentifier id = (CommPortIdentifier) e.nextElement();
                LOGGER.info("Found port with name: {}, type: {}", id.getName(), RxTxCommPortType.valueOf(id
                    .getPortType()));
                if ((id.getPortType() == CommPortIdentifier.PORT_SERIAL) && (portName.equals(id.getName()))) {
                    result = id;
                    break;
                }
            }
        }
        return result;
    }

    public static List<CommPortIdentifier> getPortIdentifiers() {
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

    public static AccessoryNode getAccessoryNode(Node node) {
        return nodeFactory.getAccessoryNode(node);
    }

    public static CommandStationNode getCommandStationNode(Node node) {
        return nodeFactory.getCommandStationNode(node);
    }

    /**
     * returns the cached node or creates a new instance
     * 
     * @param node
     *            the node
     * @return the BidibNode instance
     */
    public static BidibNode getNode(Node node) {
        return nodeFactory.getNode(node);
    }

    public static RootNode getRootNode() {
        return nodeFactory.getRootNode();
    }

    private static SerialPort internalOpen(CommPortIdentifier commPort, int baudRate) throws PortInUseException,
        UnsupportedCommOperationException, TooManyListenersException {
        SerialPort serialPort = (SerialPort) commPort.open(Bidib.class.getName(), 2000);

        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
        serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        serialPort.enableReceiveThreshold(1);
        // result.setInputBufferSize(100);
        serialPort.enableReceiveTimeout(DEFAULT_TIMEOUT);
        serialPort.notifyOnDataAvailable(true);
        MessageReceiver.enable();
        serialPort.addEventListener(new SerialPortEventListener() {
            {
                if (logFile != null) {
                    LOGGER.info("Logfile is set");
                    try {
                        new LogFileAnalyzer(new File(logFile));
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
                        new MessageReceiver(port, nodeFactory);
                    }
                    catch (Exception ex) {
                        LOGGER.error("Message receiver has terminated with an exception!", ex);
                    }
                }
            }
        });
        return serialPort;
    }

    private static void loadLibraries() {
        if (!librariesLoaded) {
            new LibraryPathManipulator().manipulateLibraryPath(null);
            librariesLoaded = true;
        }
    }

    public static void open(String portName) throws PortNotFoundException, PortInUseException,
        UnsupportedCommOperationException, IOException, ProtocolException, InterruptedException,
        TooManyListenersException {
        if (port == null) {
            loadLibraries();

            LOGGER.info("Open port with name: {}", portName);
            CommPortIdentifier commPort = findPort(portName);

            if (commPort == null) {
                throw new PortNotFoundException(portName);
            }
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

    public static void send(final byte[] bytes) {
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

    private static int sendMagic() throws IOException, ProtocolException, InterruptedException {
        BidibNode rootNode = getRootNode();

        // Ignore the first exception ...
        try {
            rootNode.getMagic();
        }
        catch (Exception e) {
        }
        return rootNode.getMagic();
    }

    public static void setLogFile(String logFile) {
        Bidib.logFile = logFile;
    }

    static void setTimeout(int timeout) {
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
