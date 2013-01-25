package org.bidib.jbidibc;

import java.util.Arrays;

import org.bidib.jbidibc.node.BidibNode;

public class Ping extends BidibCommand {
    public static void main(String[] args) {
        int result = 20;

        if (args.length == 2) {
            try {
                Bidib.open(args[0]);

                Node node = findNode(Long.decode(args[1]));

                if (node != null) {
                    BidibNode bidibNode = Bidib.getNode(node);

                    System.out.println("PING " + node.getUniqueIdAsString() + " (" + Arrays.toString(node.getAddr())
                            + ").");
                    while (true) {
                        final long now = System.currentTimeMillis();
                        final int num = bidibNode.ping();

                        System.out.println("got response from " + node.getUniqueIdAsString() + " ("
                                + Arrays.toString(node.getAddr()) + "): seq=" + num + " time="
                                + (System.currentTimeMillis() - now) + "ms");
                    }
                } else {
                    System.err.println("node with unique id \"" + args[1] + "\" not found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("usage: " + Ping.class.getSimpleName() + " <COM port> <unique id>");
        }
        System.exit(result);
    }
}
