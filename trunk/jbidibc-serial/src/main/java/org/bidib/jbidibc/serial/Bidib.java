package org.bidib.jbidibc.serial;

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
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.concurrent.Semaphore;

import org.bidib.jbidibc.core.AbstractBidib;
import org.bidib.jbidibc.core.BidibInterface;
import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.core.ConnectionListener;
import org.bidib.jbidibc.core.MessageListener;
import org.bidib.jbidibc.core.NodeListener;
import org.bidib.jbidibc.core.exception.NoAnswerException;
import org.bidib.jbidibc.core.exception.PortNotFoundException;
import org.bidib.jbidibc.core.exception.PortNotOpenedException;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.helpers.Context;
import org.bidib.jbidibc.core.node.BidibNode;
import org.bidib.jbidibc.core.node.NodeFactory;
import org.bidib.jbidibc.core.node.listener.TransferListener;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.LogFileAnalyzer;
import org.bidib.jbidibc.serial.exception.InvalidLibraryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the default bidib implementation. It creates and initializes the MessageReceiver and the NodeFactory that is
 * used in the system.
 * 
 */
public final class Bidib extends AbstractBidib {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bidib.class);

    private static final Logger MSG_RAW_LOGGER = LoggerFactory.getLogger("RAW");

    private SerialPort port;

    private Semaphore portSemaphore = new Semaphore(1);

    private Semaphore sendSemaphore = new Semaphore(1);

    private String logFile;

    // private boolean librariesLoaded;

    private static Bidib INSTANCE;

    private String requestedPortName;

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

    private Bidib() {
    }

    @Override
    protected BidibMessageProcessor createMessageReceiver(NodeFactory nodeFactory) {
        return new SerialMessageReceiver(nodeFactory);
    }

    private SerialMessageReceiver getSerialMessageReceiver() {
        return (SerialMessageReceiver) getMessageReceiver();
    }

    public static synchronized BidibInterface getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Bidib();
            INSTANCE.initialize();
        }
        return INSTANCE;
    }

    @Override
    public void close() {
        if (port != null) {
            LOGGER.debug("Close the port.");
            long start = System.currentTimeMillis();

            // no longer process received messages
            getSerialMessageReceiver().disable();

            // this makes the close operation faster ...
            try {
                port.removeEventListener();
                port.enableReceiveTimeout(200);
            }
            catch (UnsupportedCommOperationException e) {
                // ignore
            }
            catch (Exception e) {
                LOGGER.warn("Remove event listener and set receive timeout failed.", e);
            }
            try {
                port.close();
            }
            catch (Exception e) {
                LOGGER.warn("Close port failed.", e);
            }

            long end = System.currentTimeMillis();
            LOGGER.debug("Closed the port. duration: {}", end - start);

            port = null;

            if (getNodeFactory() != null) {
                // remove all stored nodes from the node factory
                getNodeFactory().reset();
            }

            if (getMessageReceiver() != null) {
                getSerialMessageReceiver().clearMessageListeners();
                getSerialMessageReceiver().clearNodeListeners();
            }

            if (getConnectionListener() != null) {
                getConnectionListener().closed(requestedPortName);
            }

            requestedPortName = null;
        }
    }

    @Override
    public List<String> getPortIdentifiers() {
        List<String> portIdentifiers = new ArrayList<String>();

        try {
            // get the comm port identifiers
            Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();
            while (e.hasMoreElements()) {
                CommPortIdentifier id = (CommPortIdentifier) e.nextElement();
                LOGGER.debug("Process current CommPortIdentifier, name: {}, portType: {}", id.getName(),
                    id.getPortType());

                if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    portIdentifiers.add(id.getName());
                }
                else {
                    LOGGER.debug("Skip port because no serial port, name: {}, portType: {}", id.getName(),
                        id.getPortType());
                }
            }
        }
        catch (UnsatisfiedLinkError ule) {
            LOGGER.warn("Get comm port identifiers failed.", ule);
            throw new InvalidLibraryException(ule.getMessage(), ule.getCause());
        }
        catch (Error error) {
            LOGGER.warn("Get comm port identifiers failed.", error);
            throw new RuntimeException(error.getMessage(), error.getCause());
        }
        return portIdentifiers;
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
            LOGGER.debug("input stream shows {} bytes available after purge.", count);
        }
        catch (Exception e) {
            LOGGER.warn("Clear input stream failed.", e);
        }

    }

    private SerialPort internalOpen(CommPortIdentifier commPort, int baudRate, Context context)
        throws PortInUseException, UnsupportedCommOperationException, TooManyListenersException {

        // open the port
        SerialPort serialPort = (SerialPort) commPort.open(Bidib.class.getName(), 2000);

        // set RTS high, DTR high - done early, so flow control can be configured after
        // try {
        // serialPort.setRTS(true); // not connected in some serial ports and adapters
        //
        // // TODO verify if this causes problems on windows with new serial library
        // // serialPort.setDTR(true); // pin 1 in DIN8; on main connector, this is DTR
        // }
        // catch (Exception e) {
        // LOGGER.warn("Set RTS and DTR true failed.", e);
        // }
        //
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
        serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        // serialPort.enableReceiveThreshold(1);
        serialPort.enableReceiveTimeout(DEFAULT_TIMEOUT);

        clearInputStream(serialPort);

        // react on port removed ...
        serialPort.notifyOnCTS(true);

        if (context != null) {
            Boolean ignoreWrongMessageNumber =
                context.get("ignoreWrongReceiveMessageNumber", Boolean.class, Boolean.FALSE);
            getSerialMessageReceiver().setIgnoreWrongMessageNumber(ignoreWrongMessageNumber);
        }

        // enable the message receiver before the event listener is added
        getSerialMessageReceiver().enable();
        serialPort.addEventListener(new SerialPortEventListener() {
            {
                if (logFile != null) {
                    LOGGER.warn("Logfile is set: {}", logFile);
                    try {
                        new LogFileAnalyzer(new File(logFile), getSerialMessageReceiver());
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
                            ((SerialMessageReceiver) getMessageReceiver()).receive(port);
                        }
                        catch (Exception ex) {
                            LOGGER.error("Message receiver has terminated with an exception!", ex);
                        }
                        break;
                    case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                        LOGGER.trace("The output buffer is empty.");
                        break;
                    case SerialPortEvent.CTS:
                        LOGGER.warn("The CTS value has changed, old value: {}, new value: {}",
                            new Object[] { event.getOldValue(), event.getNewValue() });

                        if (event.getNewValue() == false) {
                            LOGGER.warn("Close the port.");
                            Thread worker = new Thread(new Runnable() {
                                public void run() {
                                    LOGGER.info("Start close port because error was detected.");
                                    try {
                                        close();

                                        // TODO notify the listeners ...

                                    }
                                    catch (Exception ex) {
                                        LOGGER.warn("Close after error failed.", ex);
                                    }
                                    LOGGER.warn("The port was closed.");
                                }
                            });
                            worker.start();
                        }
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

    @Override
    public void open(
        String portName, ConnectionListener connectionListener, Set<NodeListener> nodeListeners,
        Set<MessageListener> messageListeners, Set<TransferListener> transferListeners, Context context)
        throws PortNotFoundException, PortNotOpenedException {

        setConnectionListener(connectionListener);

        // register the listeners
        registerListeners(nodeListeners, messageListeners, transferListeners);

        if (port == null) {
            if (portName == null || portName.trim().isEmpty()) {
                throw new PortNotFoundException("");
            }
            // loadLibraries();
            LOGGER.info("Open port with name: {}", portName);

            File file = new File(portName);

            if (file.exists()) {
                try {
                    portName = file.getCanonicalPath();
                    LOGGER.info("Changed port name to: {}", portName);
                }
                catch (IOException ex) {
                    throw new PortNotFoundException(portName);
                }
            }

            CommPortIdentifier commPort = null;
            try {
                commPort = CommPortIdentifier.getPortIdentifier(portName);
            }
            catch (NoSuchPortException ex) {
                LOGGER.warn("Requested port is not available: {}", portName, ex);
                throw new PortNotFoundException(portName);
            }

            requestedPortName = portName;

            try {
                portSemaphore.acquire();

                try {
                    // 115200 Baud
                    close();
                    port = internalOpen(commPort, 115200, context);
                    LOGGER.info("The port was opened internally, get the magic.");
                    int magic = sendMagic();
                    LOGGER.info("The root node returned the magic: {}", ByteUtils.magicToHex(magic));
                }
                catch (PortInUseException ex) {
                    LOGGER.warn("Open communication failed  because port is in use.", ex);
                    try {
                        close();
                    }
                    catch (Exception e4) {
                        // ignore
                    }
                    throw new PortNotOpenedException(portName, PortNotOpenedException.PORT_IN_USE);
                }
                catch (NoAnswerException naex) {
                    LOGGER.warn("Open communication failed.", naex);
                    try {
                        close();
                    }
                    catch (Exception e4) {
                        // ignore
                    }
                    throw naex;
                }
                catch (Exception e2) {
                    try {
                        // 19200 Baud
                        close();
                        port = internalOpen(commPort, 19200, context);
                        sendMagic();
                    }
                    catch (Exception e3) {
                        try {
                            close();
                        }
                        catch (Exception e4) {
                            // ignore
                        }
                    }
                }
            }
            catch (InterruptedException ex) {
                LOGGER.warn("Wait for portSemaphore was interrupted.", ex);
                throw new PortNotOpenedException(portName, PortNotOpenedException.UNKNOWN);
            }
            finally {
                portSemaphore.release();
            }
        }
        else {
            LOGGER.warn("Port is already opened.");
        }
    }

    @Override
    public boolean isOpened() {
        boolean isOpened = false;
        try {
            portSemaphore.acquire();

            LOGGER.debug("Check if port is opened: {}", port);
            isOpened = (port != null && port.getOutputStream() != null);
        }
        catch (InterruptedException ex) {
            LOGGER.warn("Wait for portSemaphore was interrupted.", ex);
        }
        catch (IOException ex) {
            LOGGER.warn("OutputStream is not available.", ex);
        }
        finally {
            portSemaphore.release();
        }
        return isOpened;
    }

    /**
     * Send the bytes to the outputstream.
     * 
     * @param bytes
     *            the bytes to send
     */
    @Override
    public void send(final byte[] bytes) {
        if (port != null) {
            try {
                sendSemaphore.acquire();

                if (MSG_RAW_LOGGER.isInfoEnabled()) {
                    MSG_RAW_LOGGER.info(">> {}", ByteUtils.bytesToHex(bytes));
                }

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

    /**
     * Get the magic from the root node
     * 
     * @return the magic provided by the root node
     * @throws ProtocolException
     */
    private int sendMagic() throws ProtocolException {
        BidibNode rootNode = getRootNode();
        LOGGER.info("Get the magic from the rootNode.");

        int magic = rootNode.getMagic();

        LOGGER.debug("The node returned magic: {}", magic);
        return magic;
    }

    @Override
    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    /**
     * Set the response timeout for the port.
     * 
     * @param timeout
     *            the receive timeout to set
     */
    @Override
    public void setResponseTimeout(int timeout) {
        if (port != null) {
            LOGGER.info("Set the response timeout for the serial port: {}", timeout);
            try {
                port.enableReceiveTimeout(timeout);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
