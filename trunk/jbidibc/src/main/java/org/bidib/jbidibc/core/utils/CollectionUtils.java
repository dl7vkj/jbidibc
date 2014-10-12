package org.bidib.jbidibc.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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

    public static <K> List<K> newArrayList() {
        return new ArrayList<K>();
    }

    public static <K> List<K> newArrayList(Collection<K> c) {
        return new ArrayList<K>(c);
    }

    public static <K> List<K> newArrayList(K... c) {
        return new ArrayList<K>(Arrays.asList(c));
    }

    public static <K> List<K> newArrayList(int size) {
        return new ArrayList<K>(size);
    }

    public static <K> LinkedList<K> newLinkedList() {
        return new LinkedList<K>();
    }

    public static <K> LinkedList<K> newLinkedList(Collection<K> c) {
        return new LinkedList<K>(c);
    }

}
