package org.bidib.node;

import java.io.IOException;
import java.util.Calendar;

import org.bidib.exception.ProtocolException;
import org.bidib.message.NodeChangedAckMessage;
import org.bidib.message.SysClockMessage;
import org.bidib.message.SysDisableMessage;
import org.bidib.message.SysEnableMessage;

public class RootNode extends BidibNode {

    RootNode() {
        super(new byte[] { 0 });
    }

    public void acknowledge(int versionNumber) throws IOException, ProtocolException, InterruptedException {
        send(new NodeChangedAckMessage(versionNumber), false);
    }

    public void clock(Calendar date, int factor) throws IOException, ProtocolException, InterruptedException {
        send(new SysClockMessage(date, factor));
    }

    public void sysDisable() throws IOException, ProtocolException, InterruptedException {
        send(new SysDisableMessage(), false);
    }

    public void sysEnable() throws IOException, ProtocolException, InterruptedException {
        send(new SysEnableMessage(), false);
    }
}
