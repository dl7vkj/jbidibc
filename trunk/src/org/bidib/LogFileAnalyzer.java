package org.bidib;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.bidib.message.BidibMessage;
import org.bidib.message.FeedbackAddressResponse;
import org.bidib.message.ResponseFactory;

public class LogFileAnalyzer {
    private static final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

    private final Map<Date, BidibMessage> messages = new LinkedHashMap<Date, BidibMessage>();

    public LogFileAnalyzer(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(": ");

            if (parts.length == 3 && parts[1].startsWith("receive FeedbackAddressResponse")) {
                try {
                    messages.put(dateFormat.parse(parts[0].trim()), ResponseFactory.create(getBytes(parts[2].trim())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        new Thread() {
            public void run() {
                try {
                    Map.Entry<Date, BidibMessage> previousEntry = null;

                    for (Map.Entry<Date, BidibMessage> entry : messages.entrySet()) {
                        if (previousEntry != null) {
                            Thread.sleep(entry.getKey().getTime() - previousEntry.getKey().getTime());
                        } else {
                            Thread.sleep(5000);
                        }

                        BidibMessage message = entry.getValue();

                        System.out.println("message: " + message);

                        if (message instanceof FeedbackAddressResponse) {
                            MessageReceiver.fireAddress(message.getAddr(),
                                    ((FeedbackAddressResponse) message).getDetectorNumber(),
                                    ((FeedbackAddressResponse) message).getAddresses());
                        }
                        previousEntry = entry;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private byte[] getBytes(String bytes) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        String[] parts = bytes.split(" ");

        for (String part : parts) {
            result.write((byte) Integer.parseInt(part, 16));
        }
        return result.toByteArray();
    }
}
