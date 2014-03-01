package org.bidib.jbidibc.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetBidibPort implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetBidibPort.class);

    private final DatagramSocket datagramSocket;

    private final NetMessageHandler messageReceiver;

    private AtomicBoolean runEnabled = new AtomicBoolean();

    public NetBidibPort(DatagramSocket datagramSocket, NetMessageHandler messageReceiver) {
        this.datagramSocket = datagramSocket;
        this.messageReceiver = messageReceiver;
    }

    @Override
    public void run() {
        LOGGER.info("Start the port.");
        runEnabled.set(true);

        byte[] receiveData = new byte[1024];
        try {
            while (runEnabled.get()) {
                // wait for client sending data
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                LOGGER.info("Wait to receive a datagram packet.");

                datagramSocket.receive(receivePacket);

                // TODO and it might be better to have all the handling of the controlling and listen only access here
                // TODO and remove the context in processMessages again

                // forward processing to handler
                if (messageReceiver != null) {
                    messageReceiver.receive(receivePacket);
                }
                else {
                    LOGGER.warn("No message receiver configured, received packet: {}", receivePacket);
                }
            }
        }
        catch (IOException ex) {
            if (runEnabled.get()) {
                LOGGER.warn("--- Interrupt NetBidibPort-run", ex);
            }
            else {
                LOGGER.info("The NetBidibPort worker is terminating.");
            }
        }

        LOGGER.info("Receiver thread has finished.");
    }

    public void stop() {
        LOGGER.info("Stop the datagram packet receiver.");
        runEnabled.set(false);

        datagramSocket.close();
    }

    /**
     * Send the data to the host.
     * 
     * @param sendData
     *            the data to send
     * @param address
     *            the receiving address of the host
     * @param portNumber
     *            the receiving port number of the host
     * @throws IOException
     */
    public void send(byte[] sendData, InetAddress address, int portNumber) throws IOException {

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, portNumber);
        datagramSocket.send(sendPacket);
    }

}
