package assignment.controller;

import assignment.model.Order;
import assignment.model.Stock;
import assignment.service.OrderService;
import assignment.util.ConsoleUtil;
import assignment.util.SalesUtil;
import assignment.util.config.SalesConfig;
import assignment.util.ValidationUtil;
import assignment.view.OrderView;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;



/**
 * Controller for order management operations.
 * Handles adding, searching, removing, and editing orders in the cart.
 */
public class OrderController {
    private final Logger logger = Logger.getLogger(OrderController.class.getName());
    private final OrderService orderService;
    private final OrderView orderView;

    /**
     * Constructs an OrderController with the specified OrderService.
     * @param orderService The order service to use for order operations
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
        this.orderView = new OrderView();
        logger.info("OrderController initialized with OrderService");
    }

    /**
     * Result class for product ID input.
     */
    private static class ProductIdResult {
        final int itemID;
        final Stock foundStock;
        final boolean shouldExit;
        final boolean shouldReenter;

        /**
         * Constructs a ProductIdResult with the specified values.
         * @param itemID The product ID entered by user
         * @param foundStock The stock item found, or null if not found
         * @param shouldExit Whether the user wants to exit
         * @param shouldReenter Whether the user should reenter the product ID
         */
        ProductIdResult(int itemID, Stock foundStock, boolean shouldExit, boolean shouldReenter) {
            this.itemID = itemID;
            this.foundStock = foundStock;
            this.shouldExit = shouldExit;
            this.shouldReenter = shouldReenter;
        }
    }

    /**
     * Gets product ID input from user.
     * @return ProductIdResult containing itemID, foundStock for exit/reenter
     */
    private ProductIdResult getProductIdInput() {
        logger.info("Getting product ID input from user...");
        int itemID;
        Stock foundStock = null;

        do {
            orderView.printProductIdPrompt();
            itemID = ValidationUtil.intValidation(0, 0);

            if (itemID == 0) {
                logger.info("User entered 0 - exiting order entry");
                return new ProductIdResult(0, null, true, false);
            }

            if (itemID == SalesUtil.INVALID_INPUT) {
                logger.info("Invalid input detected - user should reenter product ID");
                return new ProductIdResult(SalesUtil.INVALID_INPUT, null, false, true);
            }

            logger.info("Searching for stock item with ID: " + itemID);
            foundStock = orderService.findStockItem(itemID);

            if (foundStock == null || foundStock.getQty() == 0) {
                if (foundStock == null) {
                    logger.warning("Invalid item ID: " + itemID + " (stock not found)");
                } else {
                    logger.warning("Invalid item ID: " + itemID + " (stock quantity is 0)");
                }
                orderView.printInvalidItemIdMessage();
                itemID = SalesUtil.INVALID_INPUT;
            } else {
                logger.info("Stock item found: ID=" + foundStock.getStockID() + ", Name=" + foundStock.getStockName() + ", Qty=" + foundStock.getQty());
                orderView.printProductDetails(foundStock);
                break;
            }
        } while (true);

        logger.info("Product ID input completed: ID=" + itemID);
        return new ProductIdResult(itemID, foundStock, false, false);
    }

    /**
     * Gets quantity input from user and adds item to cart.
     * @param itemID The product ID
     * @param foundStock The stock item
     * @return true if should reenter product, false otherwise
     */
    private boolean processQuantityInput(int itemID, Stock foundStock) {
        logger.info("Processing quantity input for item ID: " + itemID);
        int quantity = -1;
        int maxQty = foundStock.getQty();
        logger.info("Maximum available quantity: " + maxQty);

        do {
            orderView.printQuantityPrompt();
            quantity = ValidationUtil.intValidation(0, 10000);

            if (quantity == SalesUtil.INVALID_INPUT) {
                logger.info("Invalid quantity input - continuing loop");
                continue;
            }
            if (SalesUtil.isReenterProduct(quantity)) {
                logger.info("User requested to reenter product (quantity=999)");
                return true; // Re-enter product ID
            }

            if (quantity <= 0 || quantity > maxQty) {
                logger.warning("Invalid quantity: " + quantity + " (max available: " + maxQty + ", item ID: " + itemID + ")");
                orderView.printInvalidQuantityMessage(maxQty);
            } else {
                // Add to Cart (Business Logic Handled by Service)
                logger.info("Adding to cart: Item ID=" + itemID + ", Quantity=" + quantity);
                boolean success = orderService.addToCart(itemID, quantity);
                if (success) {
                    logger.info("Order added to cart successfully: Item ID=" + itemID + ", Quantity=" + quantity);
                    orderView.printCartSummary(foundStock, quantity);
                } else {
                    logger.warning("Failed to add order to cart: item ID " + itemID + ", quantity " + quantity);
                    orderView.printAddOrderFailure();
                }
                break; // Exit quantity input loop
            }
        } while (true);

        return false;
    }

    /**
     * Lists all orders from the cart.
     * @param emptyMessage Custom message to display when cart is empty (null for default)
     * @param showHeader Whether to show "ALL ORDERS:" header (default: true)
     * @return true if orders exist and were displayed, false if cart is empty
     */
    private boolean listAllOrders(String emptyMessage, boolean showHeader) {
        logger.info("Listing all orders from cart...");
        List<Order> cartItems = orderService.getCartItems();
        logger.info("Cart contains " + cartItems.size() + " order(s)");
        
        if (cartItems.isEmpty()) {
            String message = (emptyMessage != null) ? emptyMessage : SalesConfig.MSG_NO_ORDERS_IN_CART;
            logger.info("Cart is empty - displaying empty message");
            orderView.printEmptyCartMessage(message);
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return false;
        }

        // Display all orders with their numbers
        logger.info("Displaying " + cartItems.size() + " order(s) with header=" + showHeader);
        if (showHeader) {
            orderView.printAllOrdersHeader();
        }
        orderView.displayCartItems(cartItems);
        if (showHeader) {
            orderView.printEmptyLine();
        }
        logger.info("Orders displayed successfully");
        return true;
    }

    /**
     * Adds a new order to the cart.
     * Displays available items, prompts for product ID and quantity,
     * and allows multiple orders to be added in sequence.
     * User can exit by entering 0 for product ID or confirm finished ordering.
     * @throws IOException if an I/O error occurs
     */
    public void addOrder() throws IOException {
        logger.info("=========================================");
        logger.info("OrderController - Starting addOrder()");
        logger.info("=========================================");
        char nextOrder = 'N';

        do {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            orderView.printOrderingSystemTitle();
            List<Stock> availableStock = orderService.getAvailableStock();
            logger.info("Displaying " + availableStock.size() + " available stock items");
            orderView.displayAvailableItems(availableStock);

            // Get Product ID
            ProductIdResult productResult = getProductIdInput();
            if (productResult.shouldExit) {
                logger.info("User chose to exit order entry");
                break;
            }
            if (productResult.shouldReenter) {
                logger.info("User needs to reenter product ID");
                continue;
            }

            // Get Quantity and Add to Cart
            boolean shouldReenter = processQuantityInput(productResult.itemID, productResult.foundStock);
            if (shouldReenter) {
                logger.info("User requested to reenter product");
                ConsoleUtil.systemPause();
                ConsoleUtil.clearScreen();
                continue;
            }

            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();

            // Ask for next order
            logger.info("Prompting user for next order confirmation");
            nextOrder = ValidationUtil.confirmValidation(SalesConfig.PROMPT_FINISHED_ORDERING);
            logger.info("User response: " + nextOrder);

        } while (nextOrder != 'Y');

        logger.info("Order entry completed. Total orders in cart: " + orderService.getCartItems().size());
        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
        logger.info("=========================================");
        logger.info("OrderController - addOrder() completed");
        logger.info("=========================================");
    }

    /**
     * Searches for an order by order number and displays its details.
     * Prompts user for order number and shows order information if found,
     * or displays a not found message if the order doesn't exist.
     */
    public void searchOrder() {
        logger.info("=========================================");
        logger.info("OrderController - Starting searchOrder()");
        logger.info("=========================================");
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        orderView.printSearchOrderMenu();

        orderView.printOrderNoSearchPrompt();
        int orderNoSearch = ValidationUtil.intValidation(1, 10000);
        logger.info("User entered order number: " + orderNoSearch);

        if (orderNoSearch == SalesUtil.INVALID_INPUT) {
            logger.info("Invalid order number input - exiting search");
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        logger.info("Searching for order number: " + orderNoSearch);
        Order item = orderService.findCartItemByOrderNo(orderNoSearch);

        if (item != null) {
            logger.info("Order found: Order No=" + item.getOrderNo() + ", Stock ID=" + item.getStockID() + ", Quantity=" + item.getQuantity());
            orderView.displayOrderDetail(item);
        } else {
            logger.warning("Order not found: order number " + orderNoSearch);
            orderView.printOrderNotFound();
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
        logger.info("=========================================");
        logger.info("OrderController - searchOrder() completed");
        logger.info("=========================================");
    }

    /**
     * Removes an order from the cart.
     * Displays all orders, prompts for order number to remove,
     * asks for confirmation, and removes the order if confirmed.
     * Stock quantity is automatically refunded when order is removed.
     */
    public void removeOrder() {
        logger.info("=========================================");
        logger.info("OrderController - Starting removeOrder()");
        logger.info("=========================================");
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        orderView.printRemoveOrderMenu();

        // Display all orders from the cart
        if (!listAllOrders(null, true)) {
            logger.info("Cart is empty - exiting removeOrder()");
            return; // Cart is empty, exit early
        }

        orderView.printOrderNoRemovePrompt();
        int orderNoRemove = ValidationUtil.intValidation(1, 10000);
        logger.info("User entered order number to remove: " + orderNoRemove);

        if (orderNoRemove == SalesUtil.INVALID_INPUT) {
            logger.info("Invalid order number input - exiting removeOrder()");
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        logger.info("Searching for order number: " + orderNoRemove);
        Order cartItem = orderService.findCartItemByOrderNo(orderNoRemove);

        if (cartItem == null) {
            logger.warning("Order not found for removal: order number " + orderNoRemove);
            orderView.printOrderNotFound();
        } else {
            logger.info("Order found for removal: Order No=" + cartItem.getOrderNo() + ", Stock ID=" + cartItem.getStockID() + ", Quantity=" + cartItem.getQuantity());
            orderView.printRemoveConfirmation(cartItem);

            char confirm = ValidationUtil.confirmValidation(SalesConfig.PROMPT_DELETE_ORDER_CONFIRM);
            logger.info("User confirmation: " + confirm);

            if (confirm == 'Y') {
                logger.info("User confirmed removal - removing order number: " + orderNoRemove);
                if (orderService.removeOrder(orderNoRemove)) {
                    logger.info("Order removed successfully: Order No=" + orderNoRemove);
                    orderView.printRemoveSuccess();
                } else {
                    logger.warning("Failed to remove order: order number " + orderNoRemove);
                    orderView.printRemoveFailure();
                }
            } else {
                logger.info("User cancelled order removal");
                orderView.printRemoveCancelled();
            }
        }

        logger.info("Cart now contains " + orderService.getCartItems().size() + " order(s)");
        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
        logger.info("=========================================");
        logger.info("OrderController - removeOrder() completed");
        logger.info("=========================================");
    }

    /**
     * Result class for order validation.
     */
    private static class OrderValidationResult {
        final Order cartItem;
        final Stock stockItem;
        final boolean isValid;

        /**
         * Constructs an OrderValidationResult with the specified values.
         * @param cartItem The cart item found, or null if not found
         * @param stockItem The stock item found, or null if not found
         * @param isValid Whether the order validation was successful
         */
        OrderValidationResult(Order cartItem, Stock stockItem, boolean isValid) {
            this.cartItem = cartItem;
            this.stockItem = stockItem;
            this.isValid = isValid;
        }
    }

    /**
     * Validates order number and retrieves cart item and stock item.
     * @param orderNoEdit The order number to validate
     * @return OrderValidationResult containing cartItem, stockItem, and validation status
     */
    private OrderValidationResult validateOrderForEdit(int orderNoEdit) {
        logger.info("Validating order for edit: Order No=" + orderNoEdit);
        if (orderNoEdit == SalesUtil.INVALID_INPUT) {
            logger.info("Invalid order number input");
            return new OrderValidationResult(null, null, false);
        }

        Order cartItem = orderService.findCartItemByOrderNo(orderNoEdit);
        if (cartItem == null) {
            logger.warning("Order not found for editing: order number " + orderNoEdit);
            orderView.printNoOrderFoundMessage();
            return new OrderValidationResult(null, null, false);
        }
        logger.info("Order found: Order No=" + cartItem.getOrderNo() + ", Stock ID=" + cartItem.getStockID() + ", Quantity=" + cartItem.getQuantity());

        Stock stockItem = orderService.findStockItem(cartItem.getStockID());
        if (stockItem == null) {
            logger.warning("Stock not found for order editing: stock ID " + cartItem.getStockID() + " (order number: " + orderNoEdit + ")");
            orderView.printStockNotFoundMessage();
            return new OrderValidationResult(null, null, false);
        }
        logger.info("Stock item found: Stock ID=" + stockItem.getStockID() + ", Available Qty=" + stockItem.getQty());

        logger.info("Order validation successful");
        return new OrderValidationResult(cartItem, stockItem, true);
    }

    /**
     * Handles full quantity deletion when user reduces quantity to zero.
     * @param orderNoEdit The order number to delete
     * @param cartItem The cart item to delete
     */
    private void handleFullQuantityDeletion(int orderNoEdit, Order cartItem) {
        logger.info("Handling full quantity deletion: Order No=" + orderNoEdit + ", Current Quantity=" + cartItem.getQuantity());
        orderView.printFullQuantityDeleteWarning();
        orderView.printRemoveConfirmation(cartItem);
        char confirm = ValidationUtil.confirmValidation(SalesConfig.PROMPT_DELETE_ORDER_CONFIRM);
        logger.info("User confirmation for full quantity deletion: " + confirm);
        
        if (confirm == 'Y') {
            logger.info("User confirmed - removing order number: " + orderNoEdit);
            if (orderService.removeOrder(orderNoEdit)) {
                logger.info("Order removed successfully during full quantity deletion: Order No=" + orderNoEdit);
                orderView.printRemoveSuccess();
            } else {
                logger.warning("Failed to remove order during full quantity deletion: order number " + orderNoEdit);
                orderView.printRemoveFailure();
            }
        } else {
            logger.info("User cancelled full quantity deletion");
            orderView.printRemoveCancelled();
        }
    }

    /**
     * Processes quantity change for an order.
     * @param orderNoEdit The order number to edit
     * @param cartItem The cart item
     * @param stockItem The stock item
     * @param choice The edit choice (1 = reduce, 2 = add)
     */
    private void processQuantityChange(int orderNoEdit, Order cartItem, Stock stockItem, int choice) {
        String actionType = (choice == SalesUtil.REDUCE_QUANTITY) ? "REDUCE" : "ADD";
        logger.info("Processing quantity change: Order No=" + orderNoEdit + ", Action=" + actionType + ", Current Qty=" + cartItem.getQuantity());
        
        int maxChange = (choice == SalesUtil.REDUCE_QUANTITY) ? cartItem.getQuantity() : stockItem.getQty();
        logger.info("Maximum change allowed: " + maxChange);
        orderView.printQuantityChangePrompt(choice == SalesUtil.REDUCE_QUANTITY, maxChange);
        int quantityChange = ValidationUtil.intValidation(1, maxChange);
        logger.info("User entered quantity change: " + quantityChange);

        if (quantityChange == SalesUtil.INVALID_INPUT) {
            logger.warning("Invalid quantity input for order edit: order number " + orderNoEdit + ", choice " + choice);
            orderView.printInvalidQuantityInputMessage();
            return;
        }

        // Check if reducing full quantity (same as deleting)
        if (choice == SalesUtil.REDUCE_QUANTITY && quantityChange == cartItem.getQuantity()) {
            logger.info("Full quantity reduction detected - treating as order deletion");
            handleFullQuantityDeletion(orderNoEdit, cartItem);
        } else {
            // Normal edit operation
            logger.info("Performing " + actionType + " operation: Quantity Change=" + quantityChange);
            boolean success = orderService.editOrderQuantity(orderNoEdit, quantityChange, choice);
            if (success) {
                String action = (choice == SalesUtil.REDUCE_QUANTITY ? "REDUCED" : "ADDED");
                Order updatedOrder = orderService.findCartItemByOrderNo(orderNoEdit);
                if (updatedOrder != null) {
                    int newQty = updatedOrder.getQuantity();
                    logger.info("Order edited successfully: Order No=" + orderNoEdit + ", New Quantity=" + newQty);
                    orderView.printEditSuccess(action, newQty);
                } else {
                    logger.warning("Failed to edit order: order number " + orderNoEdit + ", quantity change " + quantityChange + ", choice " + choice);
                    orderView.printEditFailure();
                }
            } else {
                logger.warning("Failed to edit order: order number " + orderNoEdit + ", quantity change " + quantityChange + ", choice " + choice);
                orderView.printEditFailure();
            }
        }
    }

    /**
     * Edits the quantity of an existing order in the cart.
     * Displays all orders, prompts for order number, validates the order,
     * and allows user to either reduce or add quantity.
     * If reducing full quantity, treats it as order deletion.
     */
    public void editOrder() {
        logger.info("=========================================");
        logger.info("OrderController - Starting editOrder()");
        logger.info("=========================================");
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        orderView.printEditOrderMenu();

        if (!listAllOrders(null, true)) {
            logger.info("Cart is empty - exiting editOrder()");
            return;
        }

        orderView.printOrderNoEditPrompt();
        int orderNoEdit = ValidationUtil.intValidation(1, 10000);
        logger.info("User entered order number to edit: " + orderNoEdit);
        orderView.printSeparatorLine();

        // Validate order
        OrderValidationResult validation = validateOrderForEdit(orderNoEdit);
        if (!validation.isValid) {
            logger.info("Order validation failed - exiting editOrder()");
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        // Display edit order details and submenu
        logger.info("Displaying edit order details and submenu");
        orderView.printEditOrderDetails(validation.cartItem, validation.stockItem);
        orderView.printEditOrderSubMenu();

        // Get edit choice
        orderView.printEditChoicePrompt();
        int choice = ValidationUtil.intValidation(0, 2);
        logger.info("User selected edit choice: " + choice + " (0=Cancel, 1=Reduce, 2=Add)");

        if (choice == 0) {
            logger.info("User cancelled order edit");
            orderView.printEditCancelledMessage();
        } else if (choice == SalesUtil.INVALID_INPUT) {
            logger.warning("Invalid edit choice: " + choice + " (order number: " + orderNoEdit + ")");
            orderView.printInvalidChoiceMessage();
        } else {
            processQuantityChange(orderNoEdit, validation.cartItem, validation.stockItem, choice);
        }

        logger.info("Cart now contains " + orderService.getCartItems().size() + " order(s)");
        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
        logger.info("=========================================");
        logger.info("OrderController - editOrder() completed");
        logger.info("=========================================");
    }

}

