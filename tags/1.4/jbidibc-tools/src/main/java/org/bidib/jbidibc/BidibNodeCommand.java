/**
 * 
 */
package org.bidib.jbidibc;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.node.BidibNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.BaseConverter;

@Parameters(separators = "=")
public abstract class BidibNodeCommand extends BidibCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidibNodeCommand.class);

    @Parameter(names = { "-nodeId" }, description = "Unique id of node, e.g. 0xc0000d68000100", required = true, converter = NodeIdConverter.class)
    private Long nodeId;

    protected Long getNodeId() {
        return nodeId;
    }

    protected String getNodeIdentifier() {
        return String.format("0x%08x", nodeId);
    }

    protected Node findNode() throws IOException, ProtocolException, InterruptedException {
        Node result = null;
        BidibNode rootNode = Bidib.getInstance().getRootNode();
        int count = rootNode.getNodeCount();

        for (int index = 1; index <= count; index++) {
            Node node = rootNode.getNextNode();

            if ((node.getUniqueId() & 0xffffffffffffffL) == nodeId.longValue()) {
                Bidib.getInstance().getNode(node).getMagic();
                result = node;
                break;
            }
        }
        return result;
    }

    protected byte[] getUniqueId(long uniqueId) {
        byte[] result = new byte[7];
        ByteBuffer bb = ByteBuffer.allocate(8);

        bb.putLong(uniqueId);
        System.arraycopy(bb.array(), 1, result, 0, result.length);
        return result;
    }

    public static final class NodeIdConverter extends BaseConverter<Long> {

        public NodeIdConverter(String optionName) {
            super(optionName);
        }

        @Override
        public Long convert(String value) {
            try {
                LOGGER.debug("Parse NodeId: {}", value);
                return Long.parseLong(value.substring(value.indexOf("0x") + 2), 16);
            }
            catch (NumberFormatException ex) {
                throw new ParameterException(getErrorString(value, "a long"));
            }
        }

    }

}
