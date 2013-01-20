package org.bidib.jbidibc;

import org.bidib.jbidibc.node.BidibNode;

public class ReadCv extends BidibCommand {
    public static void main(String[] args) {
        int result = 20;

        if (args.length == 3) {
            try {
                Bidib.open(args[0]);

                Node node = findNode(Long.decode(args[1]));

                if (node != null) {
                    BidibNode bidibNode = Bidib.getNode(node);

                    if (bidibNode.vendorEnable(getUniqueId(node.getUniqueId()))) {

                        VendorData vendorData = bidibNode.vendorGet(args[2]);

                        System.out.println("CV" + vendorData.getName() + "=" + vendorData.getValue());
                        bidibNode.vendorDisable();
                        result = 0;
                    }
                } else {
                    System.err.println("node with unique id \"" + args[1] + "\" not found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("usage: " + ReadCv.class.getSimpleName() + " <COM port> <unique id> <CV number>");
        }
        System.exit(result);
    }
}
