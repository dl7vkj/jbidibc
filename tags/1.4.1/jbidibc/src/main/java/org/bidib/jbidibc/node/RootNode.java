package org.bidib.jbidibc.node;

import java.io.IOException;
import java.util.Calendar;

import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.NodeChangedAckMessage;
import org.bidib.jbidibc.message.SysClockMessage;

public class RootNode extends BidibNode {

    RootNode(MessageReceiver messageReceiver) {
        super(new byte[] { 0 }, messageReceiver);
    }

    public void acknowledge(int versionNumber) throws IOException, ProtocolException, InterruptedException {
        send(new NodeChangedAckMessage(versionNumber), false, null);
    }

    public void clock(Calendar date, int factor) throws IOException, ProtocolException, InterruptedException {
        send(new SysClockMessage(date, factor), false, null);
    }
}
