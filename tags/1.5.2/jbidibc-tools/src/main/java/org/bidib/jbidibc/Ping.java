package org.bidib.jbidibc;

import java.util.Arrays;

import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.BidibNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * This commands sends the Ping message to the specified node.
 *
 */
@Parameters(separators = "=")
public class Ping extends BidibNodeCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Ping.class);

    @Parameter(names = { "-maxPings" }, description = "maximum number of pings, e.g. 10 to stop after 10 pings")
    private int maxPings = -1;

    public static void main(String[] args) {
        run(new Ping(), args);
    }

    public int execute() {
        int result = 20;

        try {
            Bidib.getInstance().open(getPortName());

            Node node = findNode();

            if (node != null) {
                BidibNode bidibNode = Bidib.getInstance().getNode(node);
                String nodeId = String.format("0x%014x", node.getUniqueId() & 0xffffffffffffffL);

                System.out.println("PING " + nodeId + " (" + Arrays.toString(node.getAddr()) + ").");

                while (true) {
                    final long now = System.currentTimeMillis();
                    final int num = bidibNode.ping();

                    System.out.println("got response from " + nodeId + " (" + Arrays.toString(node.getAddr())
                        + "): seq=" + num + " time=" + (System.currentTimeMillis() - now) + "ms");

                    if (maxPings > -1) {
                        maxPings--;
                        if (maxPings <= 0) {
                            LOGGER.info("finished pings.");
                            break;
                        }
                    }
                }
                result = 0;
            }
            else {
                System.err.println("node with unique id \"" + getNodeIdentifier() + "\" not found");
            }

            Bidib.getInstance().close();

        }
        catch (PortNotFoundException ex) {
            System.err.println("The provided port was not found: " + ex.getMessage()
                + ". Verify that the BiDiB device is connected.");
        }
        catch (Exception e) {
            System.err.println("Ping node failed: " + e);
        }
        return result;
    }
}
