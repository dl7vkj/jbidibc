package org.bidib.jbidibc.schema.bidib;

import org.bidib.jbidibc.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductUtils.class);

    public static boolean isOneControl(long uniqueId) {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Check if node is a OneControl for uniqueId: {}, pid: {}, vid: {}", NodeUtils
            .getUniqueIdAsString(uniqueId), pid, vid);

        if (vid == 13 && pid == 117) {
            return true;
        }
        return false;
    }

    public static boolean isOneHub(long uniqueId) {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Check if node is a OneHub for uniqueId: {}, pid: {}, vid: {}", NodeUtils
            .getUniqueIdAsString(uniqueId), pid, vid);

        if (vid == 13 && pid == 114) {
            return true;
        }
        return false;
    }

    public static boolean isLightControl(long uniqueId) {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Check if node is a LightControl for uniqueId: {}, pid: {}, vid: {}", NodeUtils
            .getUniqueIdAsString(uniqueId), pid, vid);

        if (vid == 13 && pid == 107) {
            return true;
        }
        return false;
    }

    public static boolean isMobaList(long uniqueId) {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Check if node is a MobaList for uniqueId: {}, pid: {}, vid: {}", NodeUtils
            .getUniqueIdAsString(uniqueId), pid, vid);

        if (vid == 13 && pid == 108) {
            return true;
        }
        return false;
    }
}
