import java.io.IOException;

import org.bidib.Bidib;
import org.bidib.Node;
import org.bidib.exception.ProtocolException;
import org.bidib.node.BidibNode;


public class BidibCommand {
    protected static Node findNode(long uniqueId) throws IOException, ProtocolException, InterruptedException {
        Node result = null;
        BidibNode rootNode = Bidib.getRootNode();
        int count = rootNode.getNodeCount();

        for (int index = 1; index <= count; index++) {
            Node node = rootNode.getNextNode();

            if ((node.getUniqueId() & 0xffffffffffffffL) == uniqueId) {
                Bidib.getNode(node).getMagic();
                result = node;
                break;
            }
        }
        return result;
    }
}
