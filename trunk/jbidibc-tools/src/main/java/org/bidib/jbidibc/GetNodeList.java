package org.bidib.jbidibc;

import org.bidib.jbidibc.node.BidibNode;

public class GetNodeList extends BidibCommand {
    public static void main(String[] args) {
        int result = 20;

        if (args.length == 1) {
            try {
                Bidib.open(args[0]);

                BidibNode rootNode = Bidib.getRootNode();
                int count = rootNode.getNodeCount();

                for (int index = 1; index <= count; index++) {
                    System.out.println(rootNode.getNextNode());
                }
                result = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("usage: " + GetNodeList.class.getSimpleName() + " <COM port>");
        }
        System.exit(result);
    }
}
