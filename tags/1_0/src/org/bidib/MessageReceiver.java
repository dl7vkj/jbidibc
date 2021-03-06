package org.bidib;

import gnu.io.SerialPort;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bidib.exception.ProtocolException;
import org.bidib.message.BidibMessage;
import org.bidib.message.LcKeyResponse;
import org.bidib.message.LogonResponse;
import org.bidib.message.NodeLostResponse;
import org.bidib.message.NodeNewResponse;
import org.bidib.message.ResponseFactory;
import org.bidib.message.SysErrorResponse;
import org.bidib.node.BidibNode;
import org.bidib.node.RootNode;

import logging.LogFactory;


public class MessageReceiver {
    private static final Logger LOG = LogFactory.getLogger(MessageReceiver.class.getName());

    private static final BlockingQueue<BidibMessage> receiveQueue = new LinkedBlockingQueue<BidibMessage>();

    private static final Collection<MessageListener> listeners = new LinkedList<MessageListener>();

    public MessageReceiver(SerialPort port, RootNode rootNode) {
        synchronized (this) {
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

                    while ((data = input.read()) != -1) {
                        logRecord.append(String.format("%02x", data) + " ");
                        if (data == BidibLibrary.BIDIB_PKT_MAGIC && output.size() > 0) {
                            for (byte[] messageArray : splitMessages(output.toByteArray())) {
                                BidibMessage message = null;

                                try {
                                    message = ResponseFactory.create(messageArray);
                                    LOG.fine("receive " + message + " : " + logRecord);
                                    logRecord.setLength(0);
                                    if (message instanceof LcKeyResponse) {
                                    } else if (message instanceof LogonResponse) {
                                    } else if (message instanceof NodeNewResponse) {
                                        Node node = ((NodeNewResponse) message).getNode(message.getAddr());

                                        rootNode.acknowledge(node.getVersion());
                                        fireNodeNew(node);
                                    } else if (message instanceof NodeLostResponse) {
                                        Node node = ((NodeLostResponse) message).getNode(message.getAddr());

                                        rootNode.acknowledge(node.getVersion());
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
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void addMessageListener(MessageListener l) {
        listeners.add(l);
    }

    private void fireNodeLost(Node node) {
        for (MessageListener l : listeners) {
            l.nodeLost(node);
        }
    }

    private void fireNodeNew(Node node) {
        for (MessageListener l : listeners) {
            l.nodeNew(node);
        }
    }

    public static BidibMessage getMessage() throws InterruptedException {
        return receiveQueue.poll(3, TimeUnit.SECONDS);
    }

    /**
     * Split the byte array into separate messages. The CRC value at the end is
     * calculated over the whole array.
     * 
     * @param output
     *            array containing at least one message
     * 
     * @return list of the separated messages
     * 
     * @throws ProtocolException
     *             Thrown if the CRC failed.
     */
    private Collection<byte[]> splitMessages(byte[] output) throws ProtocolException {
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
