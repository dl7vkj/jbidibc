package org.bidib.jbidibc;

import gnu.io.SerialPort;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.BoostCurrentResponse;
import org.bidib.jbidibc.message.BoostStatResponse;
import org.bidib.jbidibc.message.FeedbackAddressResponse;
import org.bidib.jbidibc.message.FeedbackConfidenceResponse;
import org.bidib.jbidibc.message.FeedbackFreeResponse;
import org.bidib.jbidibc.message.FeedbackMultipleResponse;
import org.bidib.jbidibc.message.FeedbackOccupiedResponse;
import org.bidib.jbidibc.message.FeedbackSpeedResponse;
import org.bidib.jbidibc.message.LcKeyResponse;
import org.bidib.jbidibc.message.LcWaitResponse;
import org.bidib.jbidibc.message.LogonResponse;
import org.bidib.jbidibc.message.NodeLostResponse;
import org.bidib.jbidibc.message.NodeNewResponse;
import org.bidib.jbidibc.message.ResponseFactory;
import org.bidib.jbidibc.message.SysErrorResponse;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class.getName());

    private static final BlockingQueue<BidibMessage> receiveQueue = new LinkedBlockingQueue<BidibMessage>();

    private static final Collection<MessageListener> listeners = new LinkedList<MessageListener>();

    private static int TIMEOUT = Bidib.DEFAULT_TIMEOUT;

    private static boolean RUNNING = true;

    public MessageReceiver(SerialPort port, NodeFactory nodeFactory) {
        synchronized (this) {
        	LOG.debug("Starting message receiver.");
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                InputStream input = null;

                if (port != null) {
                    input = port.getInputStream();
                }
                if (input != null) {
                    int data = 0;
                    boolean escape_hot = false;
                    StringBuilder logRecord = new StringBuilder();

                    while (RUNNING && (data = input.read()) != -1) {
                    	// append data to log record
                        logRecord.append(String.format("%02x ", data));
                        
                        // check if the current is the end of a packet
                        if (data == BidibLibrary.BIDIB_PKT_MAGIC && output.size() > 0) {
                        	
                        	// if a CRC error is detected in splitMessages the reading loop will terminate ...
                            for (byte[] messageArray : splitMessages(output.toByteArray())) {
                                BidibMessage message = null;

                                try {
                                    message = ResponseFactory.create(messageArray);
                                    LOG.debug("Received message: {} : {}", message, logRecord);
                                    logRecord.setLength(0);
                                    
                                    if (message instanceof BoostCurrentResponse) {
                                        fireBoosterCurrent(message.getAddr(),
                                                ((BoostCurrentResponse) message).getCurrent());
                                    } 
                                    else if (message instanceof BoostStatResponse) {
                                        fireBoosterState(message.getAddr(), ((BoostStatResponse) message).getState());
                                    } 
                                    else if (message instanceof FeedbackAddressResponse) {
                                        fireAddress(message.getAddr(),
                                                ((FeedbackAddressResponse) message).getDetectorNumber(),
                                                ((FeedbackAddressResponse) message).getAddresses());
                                    } 
                                    else if (message instanceof FeedbackConfidenceResponse) {
                                        fireConfidence(message.getAddr(),
                                                ((FeedbackConfidenceResponse) message).getValid(),
                                                ((FeedbackConfidenceResponse) message).getFreeze(),
                                                ((FeedbackConfidenceResponse) message).getSignal());
                                    } 
                                    else if (message instanceof FeedbackFreeResponse) {
                                        // acknowledge message
                                        nodeFactory.getNode(new Node(message.getAddr())).acknowledgeFree(
                                                ((FeedbackFreeResponse) message).getDetectorNumber());

                                        fireFree(message.getAddr(),
                                                ((FeedbackFreeResponse) message).getDetectorNumber());
                                    } 
                                    else if (message instanceof FeedbackMultipleResponse) {
                                        int baseAddress = ((FeedbackMultipleResponse) message).getBaseAddress();
                                        int size = ((FeedbackMultipleResponse) message).getSize();
                                        byte[] detectorData = ((FeedbackMultipleResponse) message).getDetectorData();

                                        // acknowledge message
                                        nodeFactory.getNode(new Node(message.getAddr())).acknowledgeMultiple(
                                                baseAddress, size, detectorData);

                                        int offset = 0;

                                        for (byte detectorByte : detectorData) {
                                            if (offset >= size) {
                                                break;
                                            }
                                            for (int bit = 0; bit <= 7; bit++) {
                                                if (((detectorByte >> bit) & 1) == 1) {
                                                    fireOccupied(message.getAddr(), baseAddress + offset);
                                                } else {
                                                    fireFree(message.getAddr(), baseAddress + offset);
                                                }
                                                offset++;
                                            }
                                        }
                                    } else if (message instanceof FeedbackOccupiedResponse) {
                                        // acknowledge message
                                        nodeFactory.getNode(new Node(message.getAddr())).acknowledgeOccupied(
                                                ((FeedbackOccupiedResponse) message).getDetectorNumber());

                                        fireOccupied(message.getAddr(),
                                                ((FeedbackOccupiedResponse) message).getDetectorNumber());
                                    } else if (message instanceof FeedbackSpeedResponse) {
                                        fireSpeed(message.getAddr(), ((FeedbackSpeedResponse) message).getAddress(),
                                                ((FeedbackSpeedResponse) message).getSpeed());
                                    } else if (message instanceof LcKeyResponse) {
                                        fireKey(message.getAddr(), ((LcKeyResponse) message).getKeyNumber(),
                                                ((LcKeyResponse) message).getKeyState());
                                    } else if (message instanceof LcWaitResponse) {
                                        setTimeout(((LcWaitResponse) message).getTimeout());
                                    } else if (message instanceof LogonResponse) {
                                    } else if (message instanceof NodeNewResponse) {
                                        Node node = ((NodeNewResponse) message).getNode(message.getAddr());

                                        nodeFactory.getRootNode().acknowledge(node.getVersion());
                                        fireNodeNew(node);
                                    } else if (message instanceof NodeLostResponse) {
                                        Node node = ((NodeLostResponse) message).getNode(message.getAddr());

                                        nodeFactory.getRootNode().acknowledge(node.getVersion());
                                        fireNodeLost(node);
                                    } else if (message instanceof SysErrorResponse
                                            && ((SysErrorResponse) message).getErrorCode() == 18) {
                                    } else {
                                        receiveQueue.offer(message);
                                    }
                                } finally {
                                    if (message != null) {
                                        int num = BidibNode.getNextReceiveMsgNum(message);

                                        if (message.getNum() != num) {
                                            throw new ProtocolException("wrong message number: expected " + num
                                                    + " but got " + message.getNum());
                                        }
                                    }
                                }
                                output.reset();
                            }
                        } else {
                            if (data == BidibLibrary.BIDIB_PKT_ESCAPE) {
                                escape_hot = true;
                            } else if (data != BidibLibrary.BIDIB_PKT_MAGIC) {
                                if (escape_hot) {
                                    data ^= 0x20;
                                    escape_hot = false;
                                }
                                output.write((byte) data);
                            }
                        }
                    }
                    LOG.debug("Leaving receive loop, RUNNING: {}", RUNNING);
                }
                else {
                	LOG.error("No input available.");
                }
                
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void addMessageListener(MessageListener l) {
        listeners.add(l);
    }

    public static void disable() {
    	LOG.debug("disable is called.");
        RUNNING = false;
    }

    public static void enable() {
    	LOG.debug("enable is called.");
        RUNNING = true;
    }

    static void fireAddress(byte[] address, int detectorNumber, Collection<AddressData> addresses) {
        for (MessageListener l : listeners) {
            l.address(address, detectorNumber, addresses);
        }
    }

    static void fireBoosterCurrent(byte[] address, int current) {
        for (MessageListener l : listeners) {
            l.boosterCurrent(address, current);
        }
    }

    static void fireBoosterState(byte[] address, BoosterState state) {
        for (MessageListener l : listeners) {
            l.boosterState(address, state);
        }
    }

    static void fireConfidence(byte[] address, int valid, int freeze, int signal) {
        for (MessageListener l : listeners) {
            l.confidence(address, valid, freeze, signal);
        }
    }

    private static void fireFree(byte[] address, int detectorNumber) {
        for (MessageListener l : listeners) {
            l.free(address, detectorNumber);
        }
    }

    private static void fireKey(byte[] address, int keyNumber, int keyState) {
        for (MessageListener l : listeners) {
            l.key(address, keyNumber, keyState);
        }
    }

    private static void fireNodeLost(Node node) {
        for (MessageListener l : listeners) {
            l.nodeLost(node);
        }
    }

    private static void fireNodeNew(Node node) {
        for (MessageListener l : listeners) {
            l.nodeNew(node);
        }
    }

    private static void fireOccupied(byte[] address, int detectorNumber) {
        for (MessageListener l : listeners) {
            l.occupied(address, detectorNumber);
        }
    }

    static void fireSpeed(byte[] address, AddressData addressData, int speed) {
        for (MessageListener l : listeners) {
            l.speed(address, addressData, speed);
        }
    }

    /**
     * Get the message from the receiveQueue.
     * @return the received message or null if no message was received during the defined period.
     * @throws InterruptedException
     */
    public static BidibMessage getMessage() throws InterruptedException {
        BidibMessage result = receiveQueue.poll(TIMEOUT, TimeUnit.MILLISECONDS);

        if (result == null && TIMEOUT > Bidib.DEFAULT_TIMEOUT) {
            result = receiveQueue.poll(TIMEOUT, TimeUnit.MILLISECONDS);
        }
        return result;
    }

    public static void removeMessageListener(MessageListener l) {
        listeners.remove(l);
    }

    public static void setTimeout(int timeout) {
        Bidib.setTimeout(timeout);
        TIMEOUT = timeout;
    }

    /**
     * Split the byte array into separate messages. The CRC value at the end is calculated over the whole array.
     * 
     * @param output
     *            array containing at least one message
     * 
     * @return list of the separated messages
     * 
     * @throws ProtocolException
     *             Thrown if the CRC failed.
     */
    private static Collection<byte[]> splitMessages(byte[] output) throws ProtocolException {
        Collection<byte[]> result = new LinkedList<byte[]>();
        int index = 0;

        while (index < output.length) {
            int size = output[index] + 1;
            byte[] message = new byte[size];

            System.arraycopy(output, index, message, 0, message.length);
            result.add(message);
            index += size;

            // CRC
            if (index == output.length - 1) {
                int crc = 0;

                for (index = 0; index < output.length - 1; index++) {
                    crc = CRC8.getCrcValue((output[index] ^ crc) & 0xFF);
                }
                if (crc != (output[index] & 0xFF)) {
                    throw new ProtocolException("CRC failed: should be " + crc + " but was " + (output[index] & 0xFF));
                }
                break;
            }
        }
        return result;
    }
}
