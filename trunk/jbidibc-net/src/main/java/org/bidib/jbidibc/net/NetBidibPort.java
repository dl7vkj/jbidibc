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

    private final NetMessageReceiver messageReceiver;

    private AtomicBoolean runEnabled = new AtomicBoolean();

    public NetBidibPort(DatagramSocket datagramSocket, NetMessageReceiver messageReceiver) {
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
                datagramSocket.receive(receivePacket);

                // forward processing to handler
                if (messageReceiver != null) {
                    // TODO prepare the context
                    // final Context context = new DefaultContext();
                    messageReceiver.receive(/* context, */receivePacket);
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
        LOGGER.info("Stop the datagram apcket receiver.");
        runEnabled.set(false);

        datagramSocket.close();
    }

    public void send(/* final Context context, */byte[] sendData, InetAddress address, int portNumber)
        throws IOException {

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, portNumber);
        datagramSocket.send(sendPacket);

    }

}
