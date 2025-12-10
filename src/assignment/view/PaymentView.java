package assignment.view;

import assignment.model.Order;
import assignment.model.PaymentResult;
import assignment.util.config.AppConfig;
import assignment.util.config.SalesConfig;
import java.util.List;

/**
 * View class for Payment processing operations.
 * Handles all display outputs for payment operations.
 */
public class PaymentView {

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

    /**
     * Displays payment summary with subtotal, discount, tax, and total.
     */
    public void printPaymentSummary(PaymentResult result) {
        System.out.println("\n" + AppConfig.SEPARATOR_LINE);
        System.out.println(SalesConfig.TITLE_PAYMENT_SUMMARY);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("%-20s RM%.2f%n", SalesConfig.LABEL_PAYMENT_SUBTOTAL, result.getSubtotal());
        System.out.printf("%-20s RM%.2f%n", SalesConfig.LABEL_PAYMENT_DISCOUNT, result.getDiscount());
        System.out.printf("%-20s RM%.2f%n", SalesConfig.LABEL_PAYMENT_TAX, result.getTax());
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.printf("%-20s RM%.2f%n", SalesConfig.LABEL_PAYMENT_TOTAL, result.getTotal());
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

    /**
     * Displays empty cart message.
     */
    public void printEmptyCartMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays all orders header.
     */
    public void printAllOrdersHeader() {
        System.out.println(SalesConfig.LABEL_ALL_ORDERS);
    }

    /**
     * Displays an empty line.
     */
    public void printEmptyLine() {
        System.out.println();
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
        System.out.printf("%s %.1f%%%n", SalesConfig.LABEL_DISCOUNT_RATE, discountRate * 100);
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
}

