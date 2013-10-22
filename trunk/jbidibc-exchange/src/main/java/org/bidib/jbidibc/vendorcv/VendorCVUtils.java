package org.bidib.jbidibc.vendorcv;

import java.util.LinkedList;
import java.util.List;

public class VendorCVUtils {

    public static NodetextType getNodetextOfLanguage(NodeType nodeType, String language) {
        if (nodeType.getNodetextAndNodeAndCV() != null) {
            for (Object obj : nodeType.getNodetextAndNodeAndCV()) {
                if (obj instanceof NodetextType) {
                    NodetextType nodetext = (NodetextType) obj;
                    if (language.equalsIgnoreCase(nodetext.getLang())) {
                        return nodetext;
                    }
                }
            }
        }
        return null;
    }

    public static List<CVType> getCVs(NodeType nodeType) {
        List<CVType> cvTypes = new LinkedList<CVType>();
        if (nodeType.getNodetextAndNodeAndCV() != null) {
            for (Object obj : nodeType.getNodetextAndNodeAndCV()) {
                if (obj instanceof CVType) {
                    CVType cv = (CVType) obj;
                    cvTypes.add(cv);
                }
            }
        }
        return cvTypes;
    }

    public static List<NodeType> getSubNodes(NodeType nodeType) {
        List<NodeType> subNodes = new LinkedList<NodeType>();
        if (nodeType.getNodetextAndNodeAndCV() != null) {
            for (Object obj : nodeType.getNodetextAndNodeAndCV()) {
                if (obj instanceof NodeType) {
                    NodeType node = (NodeType) obj;
                    subNodes.add(node);
                }
            }
        }
        return subNodes;
    }

}
