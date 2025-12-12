package assignment.util;

import java.util.logging.Logger;

/**
 * Validation utility for sales module operations.
 * Provides validation constants and methods used across:
 * - Order operations (product ID validation, quantity validation)
 * - Payment processing (member ID validation, payment confirmation)
 * - Transaction handling (transaction number validation)
 * 
 * Similar to ValidationUtil (general validation) and MemberUtil (member validation),
 * but specifically for sales-related validation operations.
 */
public class SalesUtil {

    private static final Logger LOGGER = Logger.getLogger(SalesUtil.class.getName());

    private SalesUtil() {
        // prevent instantiation
    }

    /** Special quantity code that means "re-enter product ID". Used for validation in order flows. */
    public static final int REENTER_PRODUCT_CODE = 999;

    /** Sentinel for invalid numeric input (matches ValidationUtil behavior). Used for validation. */
    public static final int INVALID_INPUT = -9999;

    /** Edit operation: Reduce quantity. Used for validation in quantity edit operations. */
    public static final int REDUCE_QUANTITY = 1;

    /** Edit operation: Add quantity. Used for validation in quantity edit operations. */
    public static final int ADD_QUANTITY = 2;

    /**
     * Validates if the provided quantity is the re-enter sentinel.
     * Used in Order operations to validate if user wants to re-enter product ID.
     * 
     * @param quantity The quantity value to validate
     * @return true if quantity equals REENTER_PRODUCT_CODE, false otherwise
     */
    public static boolean isReenterProduct(int quantity) {
        return quantity == REENTER_PRODUCT_CODE;
    }

    /**
     * Validates product ID input for order operations.
     * Returns 0 if user wants to exit, INVALID_INPUT if invalid, or the product ID.
     * 
     * @return Product ID (>= 1), 0 for exit, or INVALID_INPUT for invalid input
     */
    public static int validateProductId() {
        int productId = ValidationUtil.intValidation(0, 0);
        
        if (productId == INVALID_INPUT) {
            LOGGER.warning("Invalid product ID input");
            return INVALID_INPUT;
        }
        
        // 0 means exit, which is valid
        if (productId == 0) {
            return 0;
        }
        
        // Product ID must be positive
        if (productId < 1) {
            LOGGER.warning("Product ID must be positive, got: " + productId);
            return INVALID_INPUT;
        }
        
        return productId;
    }

    /**
     * Validates quantity input for order operations.
     * Checks if quantity is valid (1 to maxQuantity), or if user wants to re-enter product (999).
     * 
     * @param maxQuantity Maximum allowed quantity
     * @return Valid quantity (1 to maxQuantity), REENTER_PRODUCT_CODE (999), or INVALID_INPUT
     */
    public static int validateQuantity(int maxQuantity) {
        if (maxQuantity < 1) {
            LOGGER.warning("Invalid maxQuantity: " + maxQuantity);
            return INVALID_INPUT;
        }
        
        int quantity = ValidationUtil.intValidation(0, 10000);
        
        if (quantity == INVALID_INPUT) {
            LOGGER.warning("Invalid quantity input");
            return INVALID_INPUT;
        }
        
        // Check for re-enter product code
        if (isReenterProduct(quantity)) {
            return REENTER_PRODUCT_CODE;
        }
        
        // Quantity must be between 1 and maxQuantity
        if (quantity < 1 || quantity > maxQuantity) {
            LOGGER.warning("Quantity out of range: " + quantity + " (max: " + maxQuantity + ")");
            return INVALID_INPUT;
        }
        
        return quantity;
    }

    /**
     * Validates order number input.
     * Used for searching, removing, or editing orders.
     * 
     * @return Valid order number (>= 1) or INVALID_INPUT
     */
    public static int validateOrderNumber() {
        int orderNo = ValidationUtil.intValidation(1, 10000);
        
        if (orderNo == INVALID_INPUT) {
            LOGGER.warning("Invalid order number input");
            return INVALID_INPUT;
        }
        
        if (orderNo < 1) {
            LOGGER.warning("Order number must be positive, got: " + orderNo);
            return INVALID_INPUT;
        }
        
        return orderNo;
    }

    /**
     * Validates quantity change for edit operations.
     * Used when adding or reducing quantity in order editing.
     * 
     * @param maxChange Maximum allowed change (current quantity for reduce, available stock for add)
     * @return Valid quantity change (1 to maxChange) or INVALID_INPUT
     */
    public static int validateQuantityChange(int maxChange) {
        if (maxChange < 1) {
            LOGGER.warning("Invalid maxChange: " + maxChange);
            return INVALID_INPUT;
        }
        
        int quantityChange = ValidationUtil.intValidation(1, maxChange);
        
        if (quantityChange == INVALID_INPUT) {
            LOGGER.warning("Invalid quantity change input");
            return INVALID_INPUT;
        }
        
        if (quantityChange < 1 || quantityChange > maxChange) {
            LOGGER.warning("Quantity change out of range: " + quantityChange + " (max: " + maxChange + ")");
            return INVALID_INPUT;
        }
        
        return quantityChange;
    }

    /**
     * Validates transaction number input.
     * Used for viewing transaction details.
     * 
     * @param maxTransactionNumber Maximum transaction number available
     * @return Valid transaction number (1 to maxTransactionNumber) or INVALID_INPUT
     */
    public static int validateTransactionNumber(int maxTransactionNumber) {
        if (maxTransactionNumber < 1) {
            LOGGER.warning("Invalid maxTransactionNumber: " + maxTransactionNumber);
            return INVALID_INPUT;
        }
        
        int transactionNo = ValidationUtil.intValidation(1, maxTransactionNumber);
        
        if (transactionNo == INVALID_INPUT) {
            LOGGER.warning("Invalid transaction number input");
            return INVALID_INPUT;
        }
        
        if (transactionNo < 1 || transactionNo > maxTransactionNumber) {
            LOGGER.warning("Transaction number out of range: " + transactionNo + " (max: " + maxTransactionNumber + ")");
            return INVALID_INPUT;
        }
        
        return transactionNo;
    }

    /**
     * Validates payment cancellation input.
     * Checks if user entered "X" to cancel payment.
     * 
     * @param input The input string to validate
     * @return true if input is "X" (case-insensitive), false otherwise
     */
    public static boolean isPaymentCancelled(String input) {
        if (input == null) {
            return false;
        }
        return input.trim().equalsIgnoreCase("X");
    }

    /**
     * Validates quantity edit choice (add or reduce).
     * 
     * @param choice The choice value to validate
     * @return true if choice is REDUCE_QUANTITY or ADD_QUANTITY, false otherwise
     */
    public static boolean isValidQuantityEditChoice(int choice) {
        return choice == REDUCE_QUANTITY || choice == ADD_QUANTITY;
    }
}

