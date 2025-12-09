package assignment.util;

import assignment.util.config.SalesConfig;

/**
 * Utility helpers for sales/order flows.
 * Mirrors the style of {@link MemberUtil} for consistency.
 */
public class SalesUtil {

    private SalesUtil() {
        // prevent instantiation
    }

    /** Special quantity code that means "re-enter product ID". */
    public static final int REENTER_PRODUCT_CODE = 999;

    /** Sentinel for invalid numeric input (matches ValidationUtil behavior). */
    public static final int INVALID_INPUT = -9999;

    /** Edit operation: Reduce quantity */
    public static final int REDUCE_QUANTITY = 1;

    /** Edit operation: Add quantity */
    public static final int ADD_QUANTITY = 2;

    /**
     * Returns true when the provided quantity is the re-enter sentinel.
     */
    public static boolean isReenterProduct(int quantity) {
        return quantity == REENTER_PRODUCT_CODE;
    }


}

