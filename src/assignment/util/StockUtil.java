package assignment.util;

import assignment.util.config.StockConfig;
import java.util.logging.Logger;

/**
 * Validation utility for stock module operations.
 * Provides validation constants and methods used across:
 * - Stock addition (product name validation, quantity validation, price validation)
 * - Stock deletion (product ID validation, deletion confirmation)
 * - Stock editing (product ID validation, name validation, quantity change validation, price validation)
 * 
 * Similar to ValidationUtil (general validation), SalesUtil (sales validation), and MemberUtil (member validation),
 * but specifically for stock-related validation operations.
 */
public class StockUtil {

    private static final Logger LOGGER = Logger.getLogger(StockUtil.class.getName());

    private StockUtil() {
        // prevent instantiation
    }

    /** Sentinel for invalid numeric input (matches ValidationUtil behavior). Used for validation. */
    public static final int INVALID_INPUT = -9999;

    /** Edit operation: Product name. Used for validation in edit operations. */
    public static final int EDIT_PRODUCT_NAME = 1;

    /** Edit operation: Quantity. Used for validation in edit operations. */
    public static final int EDIT_QUANTITY = 2;

    /** Edit operation: Price. Used for validation in edit operations. */
    public static final int EDIT_PRICE = 3;

    /** Edit operation: Back/Cancel. Used for validation in edit operations. */
    public static final int EDIT_BACK = 0;

    /**
     * Validates product ID input for stock operations.
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
     * Validates product name input.
     * Checks if name is empty or contains only whitespace.
     * 
     * @param productName The product name to validate
     * @return true if product name is valid (not empty), false otherwise
     */
    public static boolean isValidProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            LOGGER.warning("Invalid product name: empty or null");
            return false;
        }
        return true;
    }

    /**
     * Validates if user wants to exit/cancel operation.
     * Checks if input is "E" (case-insensitive).
     * 
     * @param input The input string to validate
     * @return true if input is "E" (case-insensitive), false otherwise
     */
    public static boolean isExitOperation(String input) {
        if (input == null) {
            return false;
        }
        return input.trim().equalsIgnoreCase("E");
    }

    /**
     * Validates quantity input for stock operations.
     * Checks if quantity is within valid range (MIN_QUANTITY to MAX_QUANTITY).
     * 
     * @return Valid quantity (MIN_QUANTITY to MAX_QUANTITY) or INVALID_INPUT
     */
    public static int validateQuantity() {
        int quantity = ValidationUtil.intValidation(StockConfig.MIN_QUANTITY, StockConfig.MAX_QUANTITY);
        
        if (quantity == INVALID_INPUT) {
            LOGGER.warning("Invalid quantity input");
            return INVALID_INPUT;
        }
        
        if (quantity < StockConfig.MIN_QUANTITY || quantity > StockConfig.MAX_QUANTITY) {
            LOGGER.warning("Quantity out of range: " + quantity + " (min: " + StockConfig.MIN_QUANTITY + ", max: " + StockConfig.MAX_QUANTITY + ")");
            return INVALID_INPUT;
        }
        
        return quantity;
    }

    /**
     * Validates price input for stock operations.
     * Checks if price meets minimum requirement (MIN_PRICE).
     * 
     * @return Valid price (>= MIN_PRICE) or INVALID_INPUT
     */
    public static double validatePrice() {
        double price = ValidationUtil.doubleValidation();
        
        if (price == INVALID_INPUT) {
            LOGGER.warning("Invalid price input");
            return INVALID_INPUT;
        }
        
        if (price < StockConfig.MIN_PRICE) {
            LOGGER.warning("Price below minimum: " + price + " (min: " + StockConfig.MIN_PRICE + ")");
            return INVALID_INPUT;
        }
        
        return price;
    }

    /**
     * Validates quantity to add for stock operations.
     * Checks if quantity is within valid range and doesn't exceed MAX_QUANTITY when added to current quantity.
     * 
     * @param currentQuantity Current stock quantity
     * @return Valid quantity to add or INVALID_INPUT
     */
    public static int validateQuantityToAdd(int currentQuantity) {
        if (currentQuantity < 0) {
            LOGGER.warning("Invalid currentQuantity: " + currentQuantity);
            return INVALID_INPUT;
        }
        
        if (currentQuantity >= StockConfig.MAX_QUANTITY) {
            LOGGER.warning("Cannot add quantity: already at maximum (" + StockConfig.MAX_QUANTITY + ")");
            return INVALID_INPUT;
        }
        
        int maxAddable = StockConfig.MAX_QUANTITY - currentQuantity;
        int quantityToAdd = ValidationUtil.intValidation(1, maxAddable);
        
        if (quantityToAdd == INVALID_INPUT) {
            LOGGER.warning("Invalid quantity to add");
            return INVALID_INPUT;
        }
        
        if (quantityToAdd < 1 || quantityToAdd > maxAddable) {
            LOGGER.warning("Quantity to add out of range: " + quantityToAdd + " (max addable: " + maxAddable + ")");
            return INVALID_INPUT;
        }
        
        return quantityToAdd;
    }

    /**
     * Validates quantity to reduce for stock operations.
     * Checks if quantity is valid and doesn't reduce below MIN_QUANTITY.
     * 
     * @param currentQuantity Current stock quantity
     * @return Valid quantity to reduce or INVALID_INPUT
     */
    public static int validateQuantityToReduce(int currentQuantity) {
        if (currentQuantity < StockConfig.MIN_QUANTITY) {
            LOGGER.warning("Invalid currentQuantity: " + currentQuantity + " (min: " + StockConfig.MIN_QUANTITY + ")");
            return INVALID_INPUT;
        }
        
        int maxReducible = currentQuantity - StockConfig.MIN_QUANTITY;
        if (maxReducible < 1) {
            LOGGER.warning("Cannot reduce quantity: would go below minimum");
            return INVALID_INPUT;
        }
        
        int quantityToReduce = ValidationUtil.intValidation(1, currentQuantity);
        
        if (quantityToReduce == INVALID_INPUT) {
            LOGGER.warning("Invalid quantity to reduce");
            return INVALID_INPUT;
        }
        
        if (quantityToReduce < 1 || quantityToReduce > currentQuantity) {
            LOGGER.warning("Quantity to reduce out of range: " + quantityToReduce + " (max: " + currentQuantity + ")");
            return INVALID_INPUT;
        }
        
        // Check if reducing would go below minimum
        int newQuantity = currentQuantity - quantityToReduce;
        if (newQuantity < StockConfig.MIN_QUANTITY) {
            LOGGER.warning("Cannot reduce quantity: would go below minimum (" + StockConfig.MIN_QUANTITY + ")");
            return INVALID_INPUT;
        }
        
        return quantityToReduce;
    }

    /**
     * Validates edit choice for stock editing operations.
     * 
     * @param choice The choice value to validate
     * @return true if choice is valid (0-3), false otherwise
     */
    public static boolean isValidEditChoice(int choice) {
        return choice == EDIT_BACK || choice == EDIT_PRODUCT_NAME || 
               choice == EDIT_QUANTITY || choice == EDIT_PRICE;
    }

    /**
     * Validates quantity edit choice (add or reduce).
     * 
     * @param choice The choice value to validate
     * @return true if choice is valid (1 or 2), false otherwise
     */
    public static boolean isValidQuantityEditChoice(int choice) {
        return choice == 1 || choice == 2; // 1 = ADD_STOCK, 2 = REDUCE_STOCK
    }

    /**
     * Validates if continue operation (add another, delete another, etc.).
     * Checks if input is "Y" (case-insensitive).
     * 
     * @param input The input string to validate
     * @return true if input is "Y" (case-insensitive), false otherwise
     */
    public static boolean isContinueOperation(String input) {
        if (input == null) {
            return false;
        }
        return input.trim().equalsIgnoreCase("Y");
    }
}


