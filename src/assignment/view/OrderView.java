package assignment.view;

import assignment.enums.OrderMenu;
import assignment.model.Order;
import assignment.model.Stock;
import assignment.util.config.AppConfig;
import assignment.util.config.SalesConfig;
import java.util.List;

/**
 * View class for Order management operations.
 * Handles all display outputs for order/cart operations.
 */
public class OrderView {

    /**
     * Displays the ordering management menu with all available options.
     */
    public void printOrderMenu() {
        System.out.println(SalesConfig.TITLE_ORDERING_MANAGEMENT);
        System.out.println(AppConfig.SEPARATOR_LINE);
        for (OrderMenu menu : OrderMenu.values()) {
            System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays the ordering system title.
     */
    public void printOrderingSystemTitle() {
        System.out.println(SalesConfig.TITLE_ORDERING_SYSTEM);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays available items for purchase.
     */
    public void displayAvailableItems(List<Stock> availableStock) {
        System.out.println(SalesConfig.TITLE_AVAILABLE_ITEMS);
        System.out.println(AppConfig.SEPARATOR_LONG);

        // Header
        System.out.printf("%-10s      %-25s%-10s  %-10s%n", "PRODUCT ID", "PRODUCT NAME", "QUANTITY", "PRICE");
        System.out.println(AppConfig.SEPARATOR_LONG);

        boolean found = false;
        for (Stock stockItem : availableStock) {
            if (stockItem.getQty() > 0) {
                System.out.printf("%-10d      %-25s%-10d  RM%-10.2f%n",
                        stockItem.getStockID(),
                        stockItem.getStockName(),
                        stockItem.getQty(),
                        stockItem.getPrice());
                System.out.println(AppConfig.SEPARATOR_LONG);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No products available.");
            System.out.println(AppConfig.SEPARATOR_LONG);
        }
    }

    /**
     * Displays product details when creating an order.
     */
    public void printProductDetails(Stock foundStock) {
        System.out.printf("%s %s%n", SalesConfig.LABEL_PRODUCT_NAME, foundStock.getStockName());
        System.out.printf("%s RM%.2f%n", SalesConfig.LABEL_PRODUCT_PRICE, foundStock.getPrice());
        System.out.printf("%s %d%n", SalesConfig.LABEL_AVAILABLE_QUANTITY, foundStock.getQty());
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays cart summary after adding an order.
     */
    public void printCartSummary(Stock foundStock, int quantity) {
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("%s RM%.2f%n", SalesConfig.LABEL_TOTAL_COST, foundStock.getPrice() * quantity);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(SalesConfig.TITLE_ORDER_ADDED);
    }

    /**
     * Displays failure message when adding an order fails.
     */
    public void printAddOrderFailure() {
        System.out.println(SalesConfig.MSG_ADD_ORDER_FAILURE);
    }

    /**
     * Displays the search order menu title.
     */
    public void printSearchOrderMenu() {
        System.out.println(SalesConfig.TITLE_SEARCH_ORDER);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays order details when searching.
     */
    public void displayOrderDetail(Order item) {
        System.out.println(SalesConfig.LABEL_ORDER_NO + " >> " + item.getOrderNo());
        System.out.println(SalesConfig.LABEL_PRODUCT_NAME + " " + item.getStockName());
        System.out.println(SalesConfig.LABEL_QUANTITY + " " + item.getQuantity());
        System.out.printf("%s RM%.2f%n", SalesConfig.LABEL_TOTAL_COST, item.calculateTotalCost());
        System.out.println(AppConfig.SEPARATOR_SHORT);
    }

    /**
     * Displays message when order is not found.
     */
    public void printOrderNotFound() {
        System.out.println(SalesConfig.MSG_ORDER_NOT_FOUND);
    }

    /**
     * Displays the remove order menu title.
     */
    public void printRemoveOrderMenu() {
        System.out.println(SalesConfig.TITLE_REMOVE_ORDER);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays order details for removal confirmation.
     */
    public void printRemoveConfirmation(Order cartItem) {
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println("ORDER INFORMATION TO BE REMOVED:");
        System.out.println(cartItem.toString());
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays success message when order is removed.
     */
    public void printRemoveSuccess() {
        System.out.println(SalesConfig.MSG_ORDER_REMOVED_SUCCESS);
    }

    /**
     * Displays failure message when order removal fails.
     */
    public void printRemoveFailure() {
        System.out.println(SalesConfig.MSG_ORDER_REMOVAL_FAILED);
    }

    /**
     * Displays message when order removal is cancelled.
     */
    public void printRemoveCancelled() {
        System.out.println(SalesConfig.MSG_ORDER_REMOVAL_CANCELLED);
    }

    /**
     * Displays the edit order menu title.
     */
    public void printEditOrderMenu() {
        System.out.println(SalesConfig.TITLE_EDIT_ORDER);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays order details for editing.
     */
    public void printEditOrderDetails(Order cartItem, Stock stockItem) {
        System.out.println(SalesConfig.LABEL_ORDER_NO + " >> " + cartItem.getOrderNo());
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(SalesConfig.LABEL_PRODUCT_NAME + " " + cartItem.getStockName());
        System.out.println(SalesConfig.LABEL_CURRENT_QUANTITY_IN_ORDER + " " + cartItem.getQuantity());
        System.out.println(SalesConfig.LABEL_AVAILABLE_QUANTITY_IN_INVENTORY + " " + stockItem.getQty());
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays the edit order submenu options.
     */
    public void printEditOrderSubMenu() {
        System.out.println(SalesConfig.OPTION_REDUCE_QUANTITY);
        System.out.println(SalesConfig.OPTION_ADD_QUANTITY);
        System.out.println(SalesConfig.OPTION_CANCEL);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays success message when order is edited.
     */
    public void printEditSuccess(String action, int newQty) {
        System.out.printf(SalesConfig.MSG_QUANTITY_SUCCESS + "%n", action);
        System.out.printf("%s %d%n", SalesConfig.LABEL_NEW_ORDER_QUANTITY, newQty);
    }

    /**
     * Displays failure message when order edit fails.
     */
    public void printEditFailure() {
        System.out.println(SalesConfig.MSG_INVALID_QUANTITY_LIMITS);
    }

    /**
     * Displays all orders header.
     */
    public void printAllOrdersHeader() {
        System.out.println(SalesConfig.LABEL_ALL_ORDERS);
    }

    /**
     * Displays empty cart message.
     */
    public void printEmptyCartMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays message when no order is found.
     */
    public void printNoOrderFoundMessage() {
        System.out.println(SalesConfig.MSG_ORDER_NOT_FOUND);
    }

    /**
     * Displays message when stock is not found.
     */
    public void printStockNotFoundMessage() {
        System.out.println(SalesConfig.MSG_STOCK_NOT_FOUND);
    }

    /**
     * Displays warning when reducing full quantity will delete the order.
     */
    public void printFullQuantityDeleteWarning() {
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(SalesConfig.MSG_FULL_QUANTITY_DELETE_WARNING);
    }

    // ================== ORDER PROMPTS ==================

    /**
     * Prompts for product ID.
     */
    public void printProductIdPrompt() {
        System.out.print(SalesConfig.PROMPT_PRODUCT_ID);
    }

    /**
     * Displays invalid item ID message.
     */
    public void printInvalidItemIdMessage() {
        System.out.println(SalesConfig.MSG_INVALID_ITEM_ID);
    }

    /**
     * Prompts for desired quantity.
     */
    public void printQuantityPrompt() {
        System.out.print(SalesConfig.PROMPT_DESIRED_QUANTITY);
    }

    /**
     * Displays invalid quantity message with range.
     */
    public void printInvalidQuantityMessage(int maxQty) {
        System.out.printf(SalesConfig.MSG_INVALID_QUANTITY_RANGE + "%n", maxQty);
    }

    /**
     * Displays invalid quantity input message.
     */
    public void printInvalidQuantityInputMessage() {
        System.out.println(SalesConfig.MSG_INVALID_QUANTITY_INPUT);
    }

    /**
     * Prompts for finished ordering confirmation.
     */
    public void printFinishedOrderingPrompt() {
        System.out.print(SalesConfig.PROMPT_FINISHED_ORDERING);
    }

    /**
     * Prompts for order number to search.
     */
    public void printOrderNoSearchPrompt() {
        System.out.print(SalesConfig.PROMPT_ORDER_NO_SEARCH);
    }

    /**
     * Prompts for order number to remove.
     */
    public void printOrderNoRemovePrompt() {
        System.out.print(SalesConfig.PROMPT_ORDER_NO_REMOVE);
    }

    /**
     * Prompts for delete order confirmation.
     */
    public void printDeleteOrderConfirmationPrompt() {
        System.out.print(SalesConfig.PROMPT_DELETE_ORDER_CONFIRM);
    }

    /**
     * Prompts for order number to edit.
     */
    public void printOrderNoEditPrompt() {
        System.out.print(SalesConfig.PROMPT_ORDER_NO_EDIT);
    }

    /**
     * Prompts for edit choice.
     */
    public void printEditChoicePrompt() {
        System.out.print(SalesConfig.PROMPT_EDIT_CHOICE);
    }

    /**
     * Displays message when edit is cancelled.
     */
    public void printEditCancelledMessage() {
        System.out.println(SalesConfig.MSG_EDIT_CANCELLED);
    }

    /**
     * Displays invalid choice message.
     */
    public void printInvalidChoiceMessage() {
        System.out.println(SalesConfig.MSG_INVALID_CHOICE);
    }

    /**
     * Prompts for quantity to reduce or add.
     */
    public void printQuantityChangePrompt(boolean isReduce, int maxChange) {
        System.out.printf((isReduce ? SalesConfig.PROMPT_QUANTITY_TO_REDUCE : SalesConfig.PROMPT_QUANTITY_TO_ADD), maxChange);
    }

    /**
     * Displays an empty line.
     */
    public void printEmptyLine() {
        System.out.println();
    }

    /**
     * Displays a separator line.
     */
    public void printSeparatorLine() {
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays cart items (used in order listing).
     */
    public void displayCartItems(List<Order> cart) {
        System.out.println(SalesConfig.LABEL_CART_ITEMS);
        System.out.println(AppConfig.SEPARATOR_LONG);

        // Header
        System.out.printf("%-10s      %-25s%-10s  %-12s  %-12s%n", 
                SalesConfig.HEADER_ORDER_NO, 
                SalesConfig.HEADER_PRODUCT_NAME, 
                SalesConfig.HEADER_QUANTITY, 
                SalesConfig.HEADER_PRICE, 
                SalesConfig.HEADER_TOTAL);
        System.out.println(AppConfig.SEPARATOR_LONG);

        for (Order item : cart) {
            System.out.printf("%-10d      %-25s%-10d  RM%-12.2f  RM%-12.2f%n",
                    item.getOrderNo(),
                    item.getStockName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.calculateTotalCost());
            System.out.println(AppConfig.SEPARATOR_LONG);
        }
    }
}

