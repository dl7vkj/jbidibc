package org.bidib.jbidibc.utils;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {

    /**
     * Check if the provided collection is null or empty.
     * 
     * @param c
     *            the collection
     * @return false if collection is null or empty, true otherwise
     */
    public static boolean hasElements(Collection<?> c) {
        return c != null && !c.isEmpty();
    }

    /**
     * Check if the provided map is null or empty.
     * 
     * @param c
     *            the map
     * @return false if map is null or empty, true otherwise
     */
    public static boolean hasElements(Map<?, ?> c) {
        return c != null && !c.isEmpty();
    }

}
