package org.bidib.jbidibc.node;

import org.bidib.jbidibc.MessageReceiver;

public abstract class DeviceNode extends BidibNode {

    DeviceNode(byte[] addr, MessageReceiver messageReceiver) {
        super(addr, messageReceiver);
    }
}
