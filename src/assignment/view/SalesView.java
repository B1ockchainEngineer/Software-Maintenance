package assignment.view;

import assignment.enums.OrderMenu;
import assignment.enums.SalesMenu;
import assignment.model.PaymentResult;
import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.util.config.AppConfig;
import assignment.util.config.SalesConfig;
import java.util.List;

/**
 * View class for Sales, Order, Payment, and Transaction management.
 * Handles all print outputs for sales operations.
 */
public class SalesView {

    // ================== SALES VIEWS ==================

    /**
     * Displays the sales menu with all available options.
     */
    public void printSalesMenu() {
        System.out.println(SalesConfig.TITLE_SALES_MENU);
        System.out.println(AppConfig.SEPARATOR_LINE);
        for (SalesMenu menu : SalesMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays available items for purchase.
     */
    public void displayAvailableItems(List<Stock> availableStock) {
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(SalesConfig.TITLE_AVAILABLE_ITEMS);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("%-15s %-20s %-15s %-10s\n", SalesConfig.HEADER_PRODUCT_ID, SalesConfig.HEADER_PRODUCT_NAME, SalesConfig.HEADER_PRICE, SalesConfig.HEADER_QUANTITY);
        System.out.println(AppConfig.SEPARATOR_LINE);

        for (Stock stockItem : availableStock) {
            if (stockItem.getQty() > 0) {
                System.out.printf("%-15d %-20s %-15.2f %-10d\n",
                        stockItem.getStockID(),
                        stockItem.getStockName(),
                        stockItem.getPrice(),
                        stockItem.getQty());
            }
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    // ================== ORDER VIEWS ==================

    /**
     * Displays the ordering management menu with all available options.
     */
    public void printOrderMenu() {
        System.out.println(SalesConfig.TITLE_ORDERING_MANAGEMENT);
        System.out.println(AppConfig.SEPARATOR_LINE);
        for (OrderMenu menu : OrderMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
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
     * Displays product details when creating an order.
     */
    public void printProductDetails(Stock foundStock) {
        System.out.printf("%s %s\n", SalesConfig.LABEL_PRODUCT_NAME, foundStock.getStockName());
        System.out.printf("%s RM%.2f\n", SalesConfig.LABEL_PRODUCT_PRICE, foundStock.getPrice());
        System.out.printf("%s %d\n", SalesConfig.LABEL_AVAILABLE_QUANTITY, foundStock.getQty());
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays cart summary after adding an order.
     */
    public void printCartSummary(Stock foundStock, int quantity) {
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("%s RM%.2f\n", SalesConfig.LABEL_TOTAL_COST, foundStock.getPrice() * quantity);
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
    public void displayOrderDetail(Stock item) {
        System.out.println(SalesConfig.LABEL_ORDER_NO + " " + item.getOrderNo());
        System.out.println(SalesConfig.LABEL_PRODUCT_NAME + " " + item.getStockName());
        System.out.println(SalesConfig.LABEL_QUANTITY + " " + item.getQty());
        System.out.printf("%s RM%.2f\n", SalesConfig.LABEL_TOTAL_COST, item.calculateTotalCost());
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
    public void printRemoveConfirmation(Stock cartItem) {
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(SalesConfig.LABEL_ORDER_DETAILS);
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
    public void printEditOrderDetails(Stock cartItem, Stock stockItem) {
        System.out.println(SalesConfig.LABEL_ORDER_NO + " " + cartItem.getOrderNo());
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(SalesConfig.LABEL_PRODUCT_NAME + " " + cartItem.getStockName());
        System.out.println(SalesConfig.LABEL_CURRENT_QUANTITY_IN_ORDER + " " + cartItem.getQty());
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
        System.out.printf(SalesConfig.MSG_QUANTITY_SUCCESS + "\n", action);
        System.out.printf("%s %d\n", SalesConfig.LABEL_NEW_ORDER_QUANTITY, newQty);
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
        System.out.printf(SalesConfig.MSG_INVALID_QUANTITY_RANGE + "\n", maxQty);
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

    // ================== PAYMENT VIEWS ==================

    /**
     * Displays the payment menu title.
     */
    public void printPaymentMenu() {
        System.out.println(SalesConfig.TITLE_PAYMENT_MENU);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays cart items for payment.
     */
    public void displayCartItems(List<Stock> cart) {
        System.out.println(SalesConfig.LABEL_CART_ITEMS);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("%-15s %-20s %-10s %-10s %-15s\n", SalesConfig.HEADER_ORDER_NO, SalesConfig.HEADER_PRODUCT_NAME, SalesConfig.HEADER_QUANTITY, SalesConfig.HEADER_PRICE, SalesConfig.HEADER_TOTAL);
        System.out.println(AppConfig.SEPARATOR_LINE);

        for (Stock item : cart) {
            System.out.printf("%-15d %-20s %-10d RM%-9.2f RM%-14.2f\n",
                    item.getOrderNo(),
                    item.getStockName(),
                    item.getQty(),
                    item.getPrice(),
                    item.calculateTotalCost());
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays payment summary with subtotal, discount, tax, and total.
     */
    public void printPaymentSummary(PaymentResult result) {
        System.out.println("\n" + AppConfig.SEPARATOR_LINE);
        System.out.println(SalesConfig.TITLE_PAYMENT_SUMMARY);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("%-15s RM%.2f\n", SalesConfig.LABEL_PAYMENT_SUBTOTAL, result.getSubtotal());
        System.out.printf("%-15s RM%.2f\n", SalesConfig.LABEL_PAYMENT_DISCOUNT, result.getDiscount());
        System.out.printf("%-15s RM%.2f\n", SalesConfig.LABEL_PAYMENT_TAX, result.getTax());
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("%-15s RM%.2f\n", SalesConfig.LABEL_PAYMENT_TOTAL, result.getTotal());
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    /**
     * Displays success message when payment is processed.
     */
    public void printPaymentSuccess() {
        System.out.println("\n" + SalesConfig.MSG_PAYMENT_SUCCESS);
    }

    /**
     * Displays failure message when payment fails.
     */
    public void printPaymentFailure() {
        System.out.println(SalesConfig.MSG_PAYMENT_FAILED);
    }

    /**
     * Displays message when payment is cancelled.
     */
    public void printPaymentCancelled() {
        System.out.println(SalesConfig.MSG_PAYMENT_CANCELLED);
    }

    /**
     * Displays message when payment confirmation is cancelled.
     */
    public void printPaymentConfirmationCancelled() {
        System.out.println(SalesConfig.MSG_PAYMENT_CANCELLED);
    }

    // ================== PAYMENT PROMPTS ==================

    /**
     * Prompts for member ID for discount.
     */
    public void printMemberIdPrompt() {
        System.out.print(SalesConfig.PROMPT_MEMBER_ID);
    }

    /**
     * Displays member found message with discount rate.
     */
    public void printMemberFoundMessage(String name, String type, double discountRate) {
        System.out.println(SalesConfig.LABEL_MEMBER_FOUND + " " + name + " (" + type + ")");
        System.out.printf("%s %.1f%%\n", SalesConfig.LABEL_DISCOUNT_RATE, discountRate * 100);
    }

    /**
     * Displays member error message.
     */
    public void printMemberErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }

    /**
     * Prompts for payment confirmation.
     */
    public void printPaymentConfirmationPrompt() {
        System.out.print(SalesConfig.PROMPT_CONFIRM_PAYMENT);
    }

    // ================== TRANSACTION VIEWS ==================

    /**
     * Displays transaction summary report with all transactions.
     */
    public void printTransactionSummary(List<Transaction> transactions) {
        System.out.println(SalesConfig.TITLE_TRANSACTION_REPORT);
        System.out.println(AppConfig.SEPARATOR_LINE);

        if (transactions.isEmpty()) {
            System.out.println(SalesConfig.MSG_NO_TRANSACTIONS);
        } else {
            System.out.printf("%-10s %-15s %-15s %-15s %-15s\n", 
                    SalesConfig.HEADER_NO,
                    SalesConfig.HEADER_SUBTOTAL,
                    SalesConfig.HEADER_DISCOUNT,
                    SalesConfig.HEADER_TAX,
                    SalesConfig.HEADER_TOTAL);
            System.out.println(AppConfig.SEPARATOR_LINE);

            int transactionNo = 1;
            double grandTotalSubtotal = 0.0;
            double grandTotalDiscount = 0.0;
            double grandTotalTax = 0.0;
            double grandTotal = 0.0;

            for (Transaction transaction : transactions) {
                System.out.printf("%-10d RM%-14.2f RM%-14.2f RM%-14.2f RM%-14.2f\n",
                        transactionNo++,
                        transaction.getSubtotal(),
                        transaction.getDiscount(),
                        transaction.getTax(),
                        transaction.getTotal());

                grandTotalSubtotal += transaction.getSubtotal();
                grandTotalDiscount += transaction.getDiscount();
                grandTotalTax += transaction.getTax();
                grandTotal += transaction.getTotal();
            }

            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.printf("%-10s RM%-14.2f RM%-14.2f RM%-14.2f RM%-14.2f\n",
                    SalesConfig.LABEL_GRAND_TOTAL,
                    grandTotalSubtotal,
                    grandTotalDiscount,
                    grandTotalTax,
                    grandTotal);
            System.out.println(AppConfig.SEPARATOR_LINE);
            System.out.println(SalesConfig.LABEL_TOTAL_TRANSACTIONS + " " + transactions.size());
            System.out.println(AppConfig.SEPARATOR_LINE);
        }
    }

    /**
     * Displays detailed transaction information.
     */
    public void printTransactionDetails(Transaction transaction, int transactionNo) {
        System.out.println("\n" + AppConfig.SEPARATOR_LONG);
        System.out.printf(SalesConfig.TITLE_TRANSACTION_DETAILS + "\n", transactionNo);
        System.out.println(AppConfig.SEPARATOR_LONG);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("%-15s RM%.2f\n", SalesConfig.LABEL_SUBTOTAL, transaction.getSubtotal());
        System.out.printf("%-15s RM%.2f\n", SalesConfig.LABEL_DISCOUNT, transaction.getDiscount());
        System.out.printf("%-15s RM%.2f\n", SalesConfig.LABEL_TAX, transaction.getTax());
        System.out.printf("%-15s RM%.2f\n", SalesConfig.LABEL_TOTAL, transaction.getTotal());
        System.out.println(AppConfig.SEPARATOR_LINE);

        List<Stock> items = transaction.getItems();
        if (items.isEmpty()) {
            System.out.println(SalesConfig.LABEL_NO_ITEMS);
        } else {
            System.out.println(SalesConfig.LABEL_ITEMS);
            System.out.printf("%-15s %-25s %-10s %-12s %-15s\n", 
                    SalesConfig.HEADER_PRODUCT_ID,
                    SalesConfig.HEADER_PRODUCT_NAME,
                    SalesConfig.HEADER_QUANTITY,
                    SalesConfig.HEADER_PRICE,
                    SalesConfig.HEADER_ITEM_TOTAL);
            System.out.println(AppConfig.SEPARATOR_LINE);
            for (Stock item : items) {
                double itemTotal = item.getQty() * item.getPrice();
                System.out.printf("%-15d %-25s %-10d %-12.2f %-15.2f\n",
                        item.getStockID(),
                        item.getStockName(),
                        item.getQty(),
                        item.getPrice(),
                        itemTotal);
            }
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(AppConfig.SEPARATOR_LONG);
    }

    /**
     * Prompts for transaction number selection.
     */
    public void printTransactionSelectionPrompt(int maxTransaction) {
        System.out.printf("\n" + SalesConfig.MSG_SELECT_TRANSACTION, maxTransaction);
    }

    /**
     * Displays message when invalid transaction number is entered.
     */
    public void printInvalidTransactionNumber() {
        System.out.println(SalesConfig.MSG_INVALID_TRANSACTION);
    }

    // ================== UTILITY METHODS ==================

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
}
