package assignment.controller;

import assignment.model.Stock;
import assignment.service.SalesService;
import assignment.util.ConsoleUtil;
import assignment.util.SalesUtil;
import assignment.util.config.SalesConfig;
import assignment.util.ValidationUtil;
import assignment.view.SalesView;
import java.io.IOException;
import java.util.List;

import static assignment.util.SalesUtil.*;

/**
 * Controller for order management operations.
 * Handles adding, searching, removing, and editing orders in the cart.
 */
public class OrderController {
    private final SalesService salesService;
    private final SalesView salesView;

    public OrderController(SalesService salesService) {
        this.salesService = salesService;
        this.salesView = new SalesView();
    }

    /**
     * Result class for product ID input.
     */
    private static class ProductIdResult {
        final int itemID;
        final Stock foundStock;
        final boolean shouldExit;
        final boolean shouldReenter;

        ProductIdResult(int itemID, Stock foundStock, boolean shouldExit, boolean shouldReenter) {
            this.itemID = itemID;
            this.foundStock = foundStock;
            this.shouldExit = shouldExit;
            this.shouldReenter = shouldReenter;
        }
    }

    /**
     * Gets product ID input from user.
     * @return ProductIdResult containing itemID, foundStock, and flags for exit/reenter
     */
    private ProductIdResult getProductIdInput() {
        int itemID;
        Stock foundStock = null;

        do {
            salesView.printProductIdPrompt();
            itemID = ValidationUtil.intValidation(0, 0);

            if (itemID == 0) {
                return new ProductIdResult(0, null, true, false);
            }

            if (itemID == INVALID_INPUT) {
                return new ProductIdResult(INVALID_INPUT, null, false, true);
            }

            foundStock = salesService.findStockItem(itemID);

            if (foundStock == null || foundStock.getQty() == 0) {
                salesView.printInvalidItemIdMessage();
                itemID = INVALID_INPUT;
            } else {
                salesView.printProductDetails(foundStock);
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
            salesView.printQuantityPrompt();
            quantity = ValidationUtil.intValidation(0, 10000);

            if (quantity == INVALID_INPUT) continue;
            if (isReenterProduct(quantity)) {
                return true; // Re-enter product ID
            }

            if (quantity <= 0 || quantity > maxQty) {
                salesView.printInvalidQuantityMessage(maxQty);
            } else {
                // Add to Cart (Business Logic Handled by Service)
                boolean success = salesService.addToCart(itemID, quantity);
                if (success) {
                    salesView.printCartSummary(foundStock, quantity);
                } else {
                    salesView.printAddOrderFailure();
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
        List<Stock> cartItems = salesService.getCartItems();
        
        if (cartItems.isEmpty()) {
            String message = (emptyMessage != null) ? emptyMessage : SalesConfig.MSG_NO_ORDERS_IN_CART;
            salesView.printEmptyCartMessage(message);
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return false;
        }

        // Display all orders with their numbers
        if (showHeader) {
            salesView.printAllOrdersHeader();
        }
        salesView.displayCartItems(cartItems);
        if (showHeader) {
            salesView.printEmptyLine();
        }
        return true;
    }

    /**
     * Lists all orders from the cart with default settings.
     * @return true if orders exist and were displayed, false if cart is empty
     */
    private boolean listAllOrders() {
        return listAllOrders(null, true);
    }

    public void addOrder() throws IOException {
        char nextOrder = 'N';

        do {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            salesView.printOrderingSystemTitle();
            salesView.displayAvailableItems(salesService.getAvailableStock());

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
            salesView.printFinishedOrderingPrompt();
            nextOrder = SalesUtil.readYesNo();

        } while (Character.toUpperCase(nextOrder) != 'Y');

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void searchOrder() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        salesView.printSearchOrderMenu();

        salesView.printOrderNoSearchPrompt();
        int orderNoSearch = ValidationUtil.intValidation(1, 10000);

        if (orderNoSearch == INVALID_INPUT) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        Stock item = salesService.findCartItemByOrderNo(orderNoSearch);

        if (item != null) {
            salesView.displayOrderDetail(item);
        } else {
            salesView.printOrderNotFound();
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void removeOrder() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        salesView.printRemoveOrderMenu();

        // Display all orders from the cart
        if (!listAllOrders()) {
            return; // Cart is empty, exit early
        }

        salesView.printOrderNoRemovePrompt();
        int orderNoRemove = ValidationUtil.intValidation(1, 10000);

        if (orderNoRemove == INVALID_INPUT) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        Stock cartItem = salesService.findCartItemByOrderNo(orderNoRemove);

        if (cartItem == null) {
            salesView.printOrderNotFound();
        } else {
            salesView.printRemoveConfirmation(cartItem);

            salesView.printDeleteOrderConfirmationPrompt();
            char confirm = SalesUtil.readYesNo();

            if (confirm == 'Y') {
                if (salesService.removeOrder(orderNoRemove)) {
                    salesView.printRemoveSuccess();
                } else {
                    salesView.printRemoveFailure();
                }
            } else {
                salesView.printRemoveCancelled();
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    /**
     * Result class for order validation.
     */
    private static class OrderValidationResult {
        final Stock cartItem;
        final Stock stockItem;
        final boolean isValid;

        OrderValidationResult(Stock cartItem, Stock stockItem, boolean isValid) {
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
        if (orderNoEdit == INVALID_INPUT) {
            return new OrderValidationResult(null, null, false);
        }

        Stock cartItem = salesService.findCartItemByOrderNo(orderNoEdit);
        if (cartItem == null) {
            salesView.printNoOrderFoundMessage();
            return new OrderValidationResult(null, null, false);
        }

        Stock stockItem = salesService.findStockItem(cartItem.getStockID());
        if (stockItem == null) {
            salesView.printStockNotFoundMessage();
            return new OrderValidationResult(null, null, false);
        }

        return new OrderValidationResult(cartItem, stockItem, true);
    }

    /**
     * Handles full quantity deletion when user reduces quantity to zero.
     * @param orderNoEdit The order number to delete
     * @param cartItem The cart item to delete
     */
    private void handleFullQuantityDeletion(int orderNoEdit, Stock cartItem) {
        salesView.printFullQuantityDeleteWarning();
        salesView.printRemoveConfirmation(cartItem);
        salesView.printDeleteOrderConfirmationPrompt();
        char confirm = SalesUtil.readYesNo();
        
        if (confirm == 'Y') {
            if (salesService.removeOrder(orderNoEdit)) {
                salesView.printRemoveSuccess();
            } else {
                salesView.printRemoveFailure();
            }
        } else {
            salesView.printRemoveCancelled();
        }
    }

    /**
     * Processes quantity change for an order.
     * @param orderNoEdit The order number to edit
     * @param cartItem The cart item
     * @param stockItem The stock item
     * @param choice The edit choice (1 = reduce, 2 = add)
     */
    private void processQuantityChange(int orderNoEdit, Stock cartItem, Stock stockItem, int choice) {
        int maxChange = (choice == REDUCE_QUANTITY) ? cartItem.getQty() : stockItem.getQty();
        salesView.printQuantityChangePrompt(choice == REDUCE_QUANTITY, maxChange);
        int quantityChange = ValidationUtil.intValidation(1, maxChange);

        if (quantityChange == INVALID_INPUT) {
            salesView.printInvalidQuantityInputMessage();
            return;
        }

        // Check if reducing full quantity (same as deleting)
        if (choice == REDUCE_QUANTITY && quantityChange == cartItem.getQty()) {
            handleFullQuantityDeletion(orderNoEdit, cartItem);
        } else {
            // Normal edit operation
            boolean success = salesService.editOrderQuantity(orderNoEdit, quantityChange, choice);
            if (success) {
                String action = (choice == REDUCE_QUANTITY ? "REDUCED" : "ADDED");
                int newQty = salesService.findCartItemByOrderNo(orderNoEdit).getQty();
                salesView.printEditSuccess(action, newQty);
            } else {
                salesView.printEditFailure();
            }
        }
    }

    public void editOrder() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        salesView.printEditOrderMenu();

        if (!listAllOrders()) {
            return;
        }

        salesView.printOrderNoEditPrompt();
        int orderNoEdit = ValidationUtil.intValidation(1, 10000);
        salesView.printSeparatorLine();

        // Validate order
        OrderValidationResult validation = validateOrderForEdit(orderNoEdit);
        if (!validation.isValid) {
            ConsoleUtil.systemPause();
            ConsoleUtil.clearScreen();
            return;
        }

        // Display edit order details and submenu
        salesView.printEditOrderDetails(validation.cartItem, validation.stockItem);
        salesView.printEditOrderSubMenu();

        // Get edit choice
        salesView.printEditChoicePrompt();
        int choice = ValidationUtil.intValidation(0, 2);

        if (choice == 0) {
            salesView.printEditCancelledMessage();
        } else if (choice == INVALID_INPUT) {
            salesView.printInvalidChoiceMessage();
        } else {
            processQuantityChange(orderNoEdit, validation.cartItem, validation.stockItem, choice);
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

}

