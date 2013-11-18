package org.bidib.jbidibc;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.BidibNode;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * This commands reads the value of the specified CV from the specified node.
 *
 */
@Parameters(separators = "=")
public class BulkReadCv extends BidibNodeCommand {
    @Parameter(names = { "-cv" }, description = "The CV numbers, separated with ','", required = true)
    private String cvNumbers;

    public static void main(String[] args) {
        run(new BulkReadCv(), args);
    }

    public int execute() {
        int result = 20;

        if (StringUtils.isBlank(cvNumbers)) {
            System.err.println("No cvNumbers provided!");
            return -1;
        }

        try {
            Bidib.getInstance().open(getPortName());

            Node node = findNode();

            if (node != null) {
                BidibNode bidibNode = Bidib.getInstance().getNode(node);

                if (bidibNode.vendorEnable(getUniqueId(node.getUniqueId()))) {

                    List<VendorData> vendorDatas =
                        bidibNode.vendorGetBulk(StringUtils.splitPreserveAllTokens(cvNumbers, ","));
                    if (vendorDatas != null) {
                        for (VendorData vendorData : vendorDatas) {
                            System.out.println("CV" + vendorData.getName() + "=" + vendorData.getValue());
                        }
                    }

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
