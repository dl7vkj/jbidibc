package org.bidib.jbidibc;

import org.bidib.jbidibc.node.BidibNode;

public class WriteCv extends BidibCommand {
    public static void main(String[] args) {
        int result = 20;

        if (args.length == 4) {
            try {
                Bidib.open(args[0]);

                Node node = findNode(Long.decode(args[1]));

                if (node != null) {
                    BidibNode bidibNode = Bidib.getNode(node);

                    if (bidibNode.vendorEnable(getUniqueId(node.getUniqueId()))) {
                        bidibNode.vendorSet(args[2], args[3]);
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
            System.err.println("usage: " + WriteCv.class.getSimpleName()
                    + " <COM port> <unique id> <CV number> <CV value>");
        }
        System.exit(result);
    }
}
