package org.bidib.jbidibc;

import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.BidibNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameters;

/**
 * This commands reads the value of the specified CV from the specified node.
 *
 */
@Parameters(separators = "=")
public class FeaturesQuery extends BidibNodeCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeaturesQuery.class);

    public static void main(String[] args) {
        run(new FeaturesQuery(), args);
    }

    public int execute() {
        int result = 20;

        try {
            Bidib.open(getPortName());

            Node node = findNode();

            if (node != null) {
                BidibNode bidibNode = Bidib.getNode(node);

                int featureCount = bidibNode.getFeatureCount();

                LOGGER.info("featureCount: {}", featureCount);
                Feature feature = null;
                while ((feature = bidibNode.getNextFeature()) != null) {
                    LOGGER.info("feature.type: {}, value: {}", feature.getType(), feature.getValue());

                    // TODO use an enum for the feature type to display named values
                }
                LOGGER.info("Finished query features.");
                result = 0;
            }
            else {
                LOGGER.warn("node with unique id \"" + getNodeIdentifier() + "\" not found");
            }
        }
        catch (PortNotFoundException ex) {
            LOGGER.error("The provided port was not found: " + ex.getMessage()
                + ". Verify that the BiDiB device is connected.", ex);
        }
        catch (Exception ex) {
            LOGGER.error("Execute command failed.", ex);
        }
        return result;
    }
}
