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
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.Semaphore;

import org.bidib.jbidibc.enumeration.RxTxCommPortType;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.node.AccessoryNode;
import org.bidib.jbidibc.node.BidibNode;
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

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                	LOGGER.debug("Disable the message receiver.");
                    MessageReceiver.disable();
                    close();
                } catch (IOException e) {
                }
            }
        });
    }

    private static void close() throws IOException {
        if (port != null) {
        	LOGGER.debug("Close the port.");
        	long start = System.currentTimeMillis();
        	
        	// this makes the close operation faster ...
        	try {
				port.enableReceiveTimeout(200);
			} 
        	catch (UnsupportedCommOperationException e) {
				// ignore
			}
            port.close();
        	
            long end = System.currentTimeMillis();
            LOGGER.debug("Closed the port. duration: {}", end-start);

        	port = null;
        }
    }

    private static CommPortIdentifier findPort(String portName) {
        CommPortIdentifier result = null;
        LOGGER.info("Searching for port with name: {}", portName);

        if (portName != null) {
            Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();

            while (e.hasMoreElements()) {
                final CommPortIdentifier id = (CommPortIdentifier) e.nextElement();
                LOGGER.info("Found port with name: {}, type: {}", id.getName(), RxTxCommPortType.valueOf(id.getPortType()));
                if ((id.getPortType() == CommPortIdentifier.PORT_SERIAL) && (portName.equals(id.getName()))) {
                    result = id;
                    break;
                }
            }
        }
        return result;
    }

    public static AccessoryNode getAccessoryNode(Node node) {
        return nodeFactory.getAccessoryNode(node);
    }

    public static BidibNode getNode(Node node) {
        return nodeFactory.getNode(node);
    }

    public static RootNode getRootNode() {
        return nodeFactory.getRootNode();
    }

    private static SerialPort internalOpen(CommPortIdentifier commPort, int baudRate) throws PortInUseException,
            UnsupportedCommOperationException, TooManyListenersException {
        SerialPort result = (SerialPort) commPort.open(Bidib.class.getName(), 2000);

        result.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
        result.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        result.enableReceiveThreshold(1);
        result.enableReceiveTimeout(DEFAULT_TIMEOUT);
        result.notifyOnDataAvailable(true);
        MessageReceiver.enable();
        result.addEventListener(new SerialPortEventListener() {
            {
                if (logFile != null) {
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
                if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    new MessageReceiver(port, nodeFactory);
                }
            }
        });
        return result;
    }

    public static void open(String portName) throws PortNotFoundException, PortInUseException,
            UnsupportedCommOperationException, IOException, ProtocolException, InterruptedException,
            TooManyListenersException {
        if (port == null) {
        	new LibraryPathManipulator().manipulateLibraryPath(null);
        	
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
                } catch (Exception e2) {
                    try {
                        // 19200 Baud
                        close();
                        port = internalOpen(commPort, 19200);
                        sendMagic();
                    } catch (Exception e3) {
                        close();
                    }
                }
            } finally {
                portSemaphore.release();
            }
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                sendSemaphore.release();
            }
        }
    }

    private static int sendMagic() throws IOException, ProtocolException, InterruptedException {
        BidibNode rootNode = getRootNode();

        try {
            rootNode.getMagic();
        } catch (Exception e) {
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
