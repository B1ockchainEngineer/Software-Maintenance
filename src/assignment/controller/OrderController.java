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
        int itemID;
        Stock foundStock = null;

        do {
            orderView.printProductIdPrompt();
            itemID = ValidationUtil.intValidation(0, 0);

            if (itemID == 0) {
                return new ProductIdResult(0, null, true, false);
            }

            if (itemID == SalesUtil.INVALID_INPUT) {
                return new ProductIdResult(SalesUtil.INVALID_INPUT, null, false, true);
            }

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
                orderView.printProductDetails(foundStock);
                break;
            }
        } while (true);

        return new ProductIdResult(itemID, foundStock, false, false);
    }

    /**
     * Gets quantity input from user and adds item to cart.
     * @param itemID The product ID
     * @param foundStock The stock item
     * @return true if should reenter product, false otherwise
     */
    private boolean processQuantityInput(int itemID, Stock foundStock) {
        int quantity = -1;
        int maxQty = foundStock.getQty();

        do {
            orderView.printQuantityPrompt();
            quantity = ValidationUtil.intValidation(0, 10000);

            if (quantity == SalesUtil.INVALID_INPUT) continue;
            if (SalesUtil.isReenterProduct(quantity)) {
                return true; // Re-enter product ID
            }

            if (quantity <= 0 || quantity > maxQty) {
                logger.warning("Invalid quantity: " + quantity + " (max available: " + maxQty + ", item ID: " + itemID + ")");
                orderView.printInvalidQuantityMessage(maxQty);
            } else {
                // Add to Cart (Business Logic Handled by Service)
                boolean success = orderService.addToCart(itemID, quantity);
                if (success) {
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
        List<Order> cartItems = orderService.getCartItems();
        
        if (cartItems.isEmpty()) {
            String message = (emptyMessage != null) ? emptyMessage : SalesConfig.MSG_NO_ORDERS_IN_CART;
            orderView.printEmptyCartMessage(message);
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return false;
        }

        // Display all orders with their numbers
        if (showHeader) {
            orderView.printAllOrdersHeader();
        }
        orderView.displayCartItems(cartItems);
        if (showHeader) {
            orderView.printEmptyLine();
        }
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
        char nextOrder = 'N';

        do {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            orderView.printOrderingSystemTitle();
            orderView.displayAvailableItems(orderService.getAvailableStock());

            // Get Product ID
            ProductIdResult productResult = getProductIdInput();
            if (productResult.shouldExit) {
                break;
            }
            if (productResult.shouldReenter) {
                continue;
            }

            // Get Quantity and Add to Cart
            boolean shouldReenter = processQuantityInput(productResult.itemID, productResult.foundStock);
            if (shouldReenter) {
                ConsoleUtil.systemPause();
                ConsoleUtil.clearScreen();
                continue;
            }

            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();

            // Ask for next order
            nextOrder = ValidationUtil.confirmValidation(SalesConfig.PROMPT_FINISHED_ORDERING);

        } while (nextOrder != 'Y');

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    /**
     * Searches for an order by order number and displays its details.
     * Prompts user for order number and shows order information if found,
     * or displays a not found message if the order doesn't exist.
     */
    public void searchOrder() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        orderView.printSearchOrderMenu();

        orderView.printOrderNoSearchPrompt();
        int orderNoSearch = ValidationUtil.intValidation(1, 10000);

        if (orderNoSearch == SalesUtil.INVALID_INPUT) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        Order item = orderService.findCartItemByOrderNo(orderNoSearch);

        if (item != null) {
            orderView.displayOrderDetail(item);
        } else {
            logger.warning("Order not found: order number " + orderNoSearch);
            orderView.printOrderNotFound();
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    /**
     * Removes an order from the cart.
     * Displays all orders, prompts for order number to remove,
     * asks for confirmation, and removes the order if confirmed.
     * Stock quantity is automatically refunded when order is removed.
     */
    public void removeOrder() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        orderView.printRemoveOrderMenu();

        // Display all orders from the cart
        if (!listAllOrders(null, true)) {
            return; // Cart is empty, exit early
        }

        orderView.printOrderNoRemovePrompt();
        int orderNoRemove = ValidationUtil.intValidation(1, 10000);

        if (orderNoRemove == SalesUtil.INVALID_INPUT) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        Order cartItem = orderService.findCartItemByOrderNo(orderNoRemove);

        if (cartItem == null) {
            logger.warning("Order not found for removal: order number " + orderNoRemove);
            orderView.printOrderNotFound();
        } else {
            orderView.printRemoveConfirmation(cartItem);

            char confirm = ValidationUtil.confirmValidation(SalesConfig.PROMPT_DELETE_ORDER_CONFIRM);

            if (confirm == 'Y') {
                if (orderService.removeOrder(orderNoRemove)) {
                    orderView.printRemoveSuccess();
                } else {
                    logger.warning("Failed to remove order: order number " + orderNoRemove);
                    orderView.printRemoveFailure();
                }
            } else {
                orderView.printRemoveCancelled();
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
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
        if (orderNoEdit == SalesUtil.INVALID_INPUT) {
            return new OrderValidationResult(null, null, false);
        }

        Order cartItem = orderService.findCartItemByOrderNo(orderNoEdit);
        if (cartItem == null) {
            logger.warning("Order not found for editing: order number " + orderNoEdit);
            orderView.printNoOrderFoundMessage();
            return new OrderValidationResult(null, null, false);
        }

        Stock stockItem = orderService.findStockItem(cartItem.getStockID());
        if (stockItem == null) {
            logger.warning("Stock not found for order editing: stock ID " + cartItem.getStockID() + " (order number: " + orderNoEdit + ")");
            orderView.printStockNotFoundMessage();
            return new OrderValidationResult(null, null, false);
        }

        return new OrderValidationResult(cartItem, stockItem, true);
    }

    /**
     * Handles full quantity deletion when user reduces quantity to zero.
     * @param orderNoEdit The order number to delete
     * @param cartItem The cart item to delete
     */
    private void handleFullQuantityDeletion(int orderNoEdit, Order cartItem) {
        orderView.printFullQuantityDeleteWarning();
        orderView.printRemoveConfirmation(cartItem);
        char confirm = ValidationUtil.confirmValidation(SalesConfig.PROMPT_DELETE_ORDER_CONFIRM);
        
        if (confirm == 'Y') {
            if (orderService.removeOrder(orderNoEdit)) {
                orderView.printRemoveSuccess();
            } else {
                logger.warning("Failed to remove order during full quantity deletion: order number " + orderNoEdit);
                orderView.printRemoveFailure();
            }
        } else {
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
        int maxChange = (choice == SalesUtil.REDUCE_QUANTITY) ? cartItem.getQuantity() : stockItem.getQty();
        orderView.printQuantityChangePrompt(choice == SalesUtil.REDUCE_QUANTITY, maxChange);
        int quantityChange = ValidationUtil.intValidation(1, maxChange);

        if (quantityChange == SalesUtil.INVALID_INPUT) {
            logger.warning("Invalid quantity input for order edit: order number " + orderNoEdit + ", choice " + choice);
            orderView.printInvalidQuantityInputMessage();
            return;
        }

        // Check if reducing full quantity (same as deleting)
        if (choice == SalesUtil.REDUCE_QUANTITY && quantityChange == cartItem.getQuantity()) {
            handleFullQuantityDeletion(orderNoEdit, cartItem);
        } else {
            // Normal edit operation
            boolean success = orderService.editOrderQuantity(orderNoEdit, quantityChange, choice);
            if (success) {
                String action = (choice == SalesUtil.REDUCE_QUANTITY ? "REDUCED" : "ADDED");
                Order updatedOrder = orderService.findCartItemByOrderNo(orderNoEdit);
                if (updatedOrder != null) {
                    int newQty = updatedOrder.getQuantity();
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
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        orderView.printEditOrderMenu();

        if (!listAllOrders(null, true)) {
            return;
        }

        orderView.printOrderNoEditPrompt();
        int orderNoEdit = ValidationUtil.intValidation(1, 10000);
        orderView.printSeparatorLine();

        // Validate order
        OrderValidationResult validation = validateOrderForEdit(orderNoEdit);
        if (!validation.isValid) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        // Display edit order details and submenu
        orderView.printEditOrderDetails(validation.cartItem, validation.stockItem);
        orderView.printEditOrderSubMenu();

        // Get edit choice
        orderView.printEditChoicePrompt();
        int choice = ValidationUtil.intValidation(0, 2);

        if (choice == 0) {
            orderView.printEditCancelledMessage();
        } else if (choice == SalesUtil.INVALID_INPUT) {
            logger.warning("Invalid edit choice: " + choice + " (order number: " + orderNoEdit + ")");
            orderView.printInvalidChoiceMessage();
        } else {
            processQuantityChange(orderNoEdit, validation.cartItem, validation.stockItem, choice);
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

}

