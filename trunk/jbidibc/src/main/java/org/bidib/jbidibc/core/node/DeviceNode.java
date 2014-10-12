package org.bidib.jbidibc.core.node;

import org.bidib.jbidibc.core.MessageReceiver;

public abstract class DeviceNode extends BidibNode {

    DeviceNode(byte[] addr, MessageReceiver messageReceiver, boolean ignoreWaitTimeout) {
        super(addr, messageReceiver, ignoreWaitTimeout);
    }
}
