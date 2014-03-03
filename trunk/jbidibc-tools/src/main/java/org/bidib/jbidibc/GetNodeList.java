package org.bidib.jbidibc;

import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.serial.Bidib;

import com.beust.jcommander.Parameters;

/**
 * This commands gets the list of nodes from the specified port.
 * 
 */
@Parameters(separators = "=")
public class GetNodeList extends BidibCommand {
    public static void main(String[] args) {

        run(new GetNodeList(), args);
    }

    public int execute() {
        int result = 20;

        try {
            openPort(getPortName());

            BidibNode rootNode = Bidib.getInstance().getRootNode();
            int count = rootNode.getNodeCount();

            for (int index = 1; index <= count; index++) {
                Node node = rootNode.getNextNode();
                System.out.println("Found node: " + node);
            }
            result = 0;

            Bidib.getInstance().close();
        }
        catch (PortNotFoundException ex) {
            System.err.println("The provided port was not found: " + ex.getMessage()
                + ". Verify that the BiDiB device is connected.");
        }
        catch (Exception ex) {
            System.err.println("Get list of nodes failed: " + ex);
        }
        return result;
    }
}
