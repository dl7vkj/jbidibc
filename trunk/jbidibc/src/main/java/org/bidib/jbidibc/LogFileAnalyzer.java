package org.bidib.jbidibc;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.BoostStatResponse;
import org.bidib.jbidibc.message.FeedbackAddressResponse;
import org.bidib.jbidibc.message.FeedbackConfidenceResponse;
import org.bidib.jbidibc.message.FeedbackSpeedResponse;
import org.bidib.jbidibc.message.ResponseFactory;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFileAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogFileAnalyzer.class);

    // private static final DateFormat dateFormat = new
    // SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);

    private final Collection<Message> messages = new LinkedList<Message>();

    public LogFileAnalyzer(final File file, final MessageReceiver messageReceiver) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;

        LOGGER.info("Loading data from logfile: {}", file);

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(": ");

            // @formatter:off
            if (parts.length == 3
                && (parts[1].startsWith("receive BoostStatResponse")
                    || parts[1].startsWith("receive FeedbackAddressResponse")
                    || parts[1].startsWith("receive FeedbackConfidenceResponse") || parts[1]
                        .startsWith("receive FeedbackSpeedResponse"))) {
                try {
                    LOGGER.debug("Parsing message, date: {}, raw: {}", parts[0], parts[2]);
                    messages.add(new Message(dateFormat.parse(parts[0].trim()).getTime(), ResponseFactory
                        .create(getBytes(parts[2].trim()))));

                }
                catch (ProtocolException e) {
                    LOGGER.warn(
                        "unknown message " + ByteUtils.toString(getBytes(parts[2].trim())) + " in line " + line, e);
                }
                catch (Exception e) {
                    LOGGER.error("Add new message to messages failed.");
                }
            }
            // @formatter:on
        }
        reader.close();
        new Thread(
            "LogAnalyzer-Thread") {
            public void run() {
                try {
                    Message previousMessage = null;

                    for (Message message : messages) {
                        if (previousMessage != null) {
                            Thread.sleep(message.time - previousMessage.time);
                        }
                        else {
                            Thread.sleep(5000);
                        }

                        LOGGER.info("message: " + message.message);

                        if (message.message instanceof BoostStatResponse) {
                            messageReceiver.fireBoosterState(message.message.getAddr(),
                                ((BoostStatResponse) message.message).getState());
                        }
                        else if (message.message instanceof FeedbackAddressResponse) {
                            messageReceiver.fireAddress(message.message.getAddr(),
                                ((FeedbackAddressResponse) message.message).getDetectorNumber(),
                                ((FeedbackAddressResponse) message.message).getAddresses());
                        }
                        else if (message.message instanceof FeedbackConfidenceResponse) {
                            messageReceiver.fireConfidence(message.message.getAddr(),
                                ((FeedbackConfidenceResponse) message.message).getValid(),
                                ((FeedbackConfidenceResponse) message.message).getFreeze(),
                                ((FeedbackConfidenceResponse) message.message).getSignal());
                        }
                        else if (message.message instanceof FeedbackSpeedResponse) {
                            messageReceiver.fireSpeed(message.message.getAddr(),
                                ((FeedbackSpeedResponse) message.message).getAddress(),
                                ((FeedbackSpeedResponse) message.message).getSpeed());
                        }
                        previousMessage = message;
                    }
                    LOGGER.info("no more messages to fire");
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    private static byte[] getBytes(String bytes) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        String[] parts = bytes.split(" ");
        boolean escapeHot = false;

        for (String part : parts) {
            byte b = (byte) Integer.parseInt(part, 16);

            if (b == (byte) BidibLibrary.BIDIB_PKT_ESCAPE) {
                escapeHot = true;
            }
            else if (b != (byte) BidibLibrary.BIDIB_PKT_MAGIC) {
                if (escapeHot) {
                    b ^= 0x20;
                    escapeHot = false;
                }
                result.write(b);
            }
        }
        return result.toByteArray();
    }

    private class Message {
        private final long time;

        private final BidibMessage message;

        public Message(long time, BidibMessage message) {
            LOGGER.info("Create new message, time: {}, message: {}", time, message);
            this.time = time;
            this.message = message;
        }
    }
}
