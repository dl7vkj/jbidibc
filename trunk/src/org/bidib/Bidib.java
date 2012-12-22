package org.bidib;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.Semaphore;

import org.bidib.exception.PortNotFoundException;
import org.bidib.exception.ProtocolException;
import org.bidib.node.AccessoryNode;
import org.bidib.node.BidibNode;
import org.bidib.node.NodeFactory;
import org.bidib.node.RootNode;

public class Bidib {
    public static final int DEFAULT_TIMEOUT = 3000;

    private static NodeFactory nodeFactory = new NodeFactory();
    private static SerialPort port = null;
    private static Semaphore portSemaphore = new Semaphore(1);
    private static Semaphore sendSemaphore = new Semaphore(1);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    MessageReceiver.stop();
                    close();
                } catch (IOException e) {
                }
            }
        });
    }

    private static void close() throws IOException {
        if (port != null) {
            port.close();
            port = null;
        }
    }

    private static CommPortIdentifier findPort(String portName) {
        CommPortIdentifier result = null;

        if (portName != null) {
            Enumeration<?> e = CommPortIdentifier.getPortIdentifiers();

            while (e.hasMoreElements()) {
                final CommPortIdentifier id = (CommPortIdentifier) e.nextElement();

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
        result.addEventListener(new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    new MessageReceiver(port, nodeFactory);
                    // try {
                    // new LogFileAnalyzer(new File("/home/schenk/BiDiBWizard.log"));
                    // } catch (IOException e) {
                    // e.printStackTrace();
                    // }
                }
            }
        });
        return result;
    }

    public static void open(String portName) throws PortNotFoundException, PortInUseException,
            UnsupportedCommOperationException, IOException, ProtocolException, InterruptedException,
            TooManyListenersException {
        if (port == null) {
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
