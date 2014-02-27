package org.bidib.jbidibc;

import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.serial.Bidib;

import com.beust.jcommander.Parameters;

/**
 * This commands reads the value of the specified CV from the specified node.
 *
 */
@Parameters(separators = "=")
public class FeaturesQuery extends BidibNodeCommand {
    public static void main(String[] args) {
        run(new FeaturesQuery(), args);
    }

    public int execute() {
        int result = 20;

        try {
            Bidib.getInstance().open(getPortName(), new ConnectionListener() {
                @Override
                public void opened(String port) {
                }

                @Override
                public void closed(String port) {
                }
            });

            Node node = findNode();

            if (node != null) {
                BidibNode bidibNode = Bidib.getInstance().getNode(node);

                int featureCount = bidibNode.getFeatureCount();

                System.out.println("featureCount: " + featureCount);
                Feature feature = null;
                while ((feature = bidibNode.getNextFeature()) != null) {
                    System.out.println("feature.type: " + feature.getType() + ", value: " + feature.getValue());

                    // TODO use an enum for the feature type to display named values
                }
                System.out.println("Finished query features.");
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
        catch (Exception ex) {
            System.err.println("Execute command failed: " + ex);
        }
        return result;
    }
}
