package org.bidib.jbidibc;

import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.BidibNode;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * This commands writes the value into the specified CV of the specified node.
 *
 */
@Parameters(separators = "=")
public class WriteCv extends BidibNodeCommand {
    @Parameter(names = { "-cv" }, description = "The CV number", required = true)
    private String cvNumber;

    @Parameter(names = { "-value" }, description = "The CV value", required = true)
    private String cvValue;

    public static void main(String[] args) {
        run(new WriteCv(), args);
    }

    public int execute() {
        int result = 20;

        try {
            Bidib.getInstance().open(getPortName());

            Node node = findNode();

            if (node != null) {
                BidibNode bidibNode = Bidib.getInstance().getNode(node);

                if (bidibNode.vendorEnable(getUniqueId(node.getUniqueId()))) {
                    bidibNode.vendorSet(cvNumber, cvValue);
                    bidibNode.vendorDisable();
                    result = 0;
                }
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
        catch (Exception ex) {
            System.err.println("Execute command failed: " + ex);
        }
        return result;
    }
}
