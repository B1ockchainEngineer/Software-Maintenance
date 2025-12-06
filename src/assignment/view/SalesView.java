package assignment.view;

import assignment.model.Stock;
import assignment.service.PaymentService;
import java.util.List;

/**
 * View class for Sales and Payment management.
 * Handles all print outputs for sales operations.
 */
public class SalesView {

    public void displayAvailableItems(List<Stock> availableStock) {
        System.out.println("-------------------------------------------------------");
        System.out.println("            AVAILABLE ITEMS FOR PURCHASE");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-15s %-20s %-15s %-10s\n", "PRODUCT ID", "PRODUCT NAME", "PRICE (RM)", "QUANTITY");
        System.out.println("-------------------------------------------------------");

        for (Stock stockItem : availableStock) {
            if (stockItem.getQty() > 0) {
                System.out.printf("%-15d %-20s %-15.2f %-10d\n",
                        stockItem.getStockID(),
                        stockItem.getStockName(),
                        stockItem.getPrice(),
                        stockItem.getQty());
            }
        }
        System.out.println("-------------------------------------------------------");
    }

    public void printOrderMenu() {
        System.out.println("[ ORDERING SYSTEM ]");
        System.out.println("-------------------------------------------------------");
    }

    public void printProductDetails(Stock foundStock) {
        System.out.printf("PRODUCT NAME: %s\n", foundStock.getStockName());
        System.out.printf("PRODUCT PRICE: RM%.2f\n", foundStock.getPrice());
        System.out.printf("AVAILABLE QUANTITY: %d\n", foundStock.getQty());
        System.out.println("-------------------------------------------------------");
    }

    public void printCartSummary(Stock foundStock, int quantity) {
        System.out.println("-------------------------------------------------------");
        System.out.printf("TOTAL COST: RM%.2f\n", foundStock.getPrice() * quantity);
        System.out.println("-------------------------------------------------------");
        System.out.println("[ORDER ADDED]");
    }

    public void printAddOrderFailure() {
        System.out.println("<<<Failed to add order. Invalid item or quantity.>>>");
    }

    public void printSearchOrderMenu() {
        System.out.println("[ SEARCH AN ORDER ]");
        System.out.println("-------------------------------------------------------");
    }

    public void displayOrderDetail(Stock item) {
        System.out.println("ORDER NO " + item.getOrderNo());
        System.out.println("PRODUCT NAME: " + item.getStockName());
        System.out.println("QUANTITY: " + item.getQty());
        System.out.printf("TOTAL COST: RM%.2f\n", item.calculateTotalCost());
        System.out.println("-----------------------");
    }

    public void printOrderNotFound() {
        System.out.println("The order is not found in the cart. Please try again.");
    }

    public void printRemoveOrderMenu() {
        System.out.println("[ REMOVE AN ORDER ]");
        System.out.println("-------------------------------------------------------");
    }

    public void printRemoveConfirmation(Stock cartItem) {
        System.out.println("-------------------------------------------------------");
        System.out.println("ORDER DETAILS:");
        System.out.println(cartItem.toString());
        System.out.println("-------------------------------------------------------");
    }

    public void printRemoveSuccess() {
        System.out.println("ORDER REMOVED SUCCESSFULLY");
    }

    public void printRemoveFailure() {
        System.out.println("ORDER REMOVAL FAILED (Error in Service)");
    }

    public void printRemoveCancelled() {
        System.out.println("ORDER REMOVAL CANCELLED");
    }

    public void printEditOrderMenu() {
        System.out.println("[ EDIT AN ORDER ]");
        System.out.println("-------------------------------------------------------");
    }

    public void printEditOrderDetails(Stock cartItem, Stock stockItem) {
        System.out.println("ORDER NO " + cartItem.getOrderNo());
        System.out.println("-------------------------------------------------------");
        System.out.println("PRODUCT NAME: " + cartItem.getStockName());
        System.out.println("CURRENT QUANTITY IN ORDER: " + cartItem.getQty());
        System.out.println("AVAILABLE QUANTITY IN INVENTORY: " + stockItem.getQty());
        System.out.println("-------------------------------------------------------");
    }

    public void printEditOrderSubMenu() {
        System.out.println("1. REDUCE QUANTITY");
        System.out.println("2. ADD QUANTITY");
        System.out.println("0. CANCEL");
        System.out.println("-------------------------------------------------------");
    }

    public void printEditSuccess(String action, int newQty) {
        System.out.println("QUANTITY " + action + " SUCCESSFULLY");
        System.out.printf("NEW ORDER QUANTITY: %d\n", newQty);
    }

    public void printEditFailure() {
        System.out.println("<<<Invalid quantity. Check limits.>>>");
    }

    public void printPaymentMenu() {
        System.out.println("[ MAKE PAYMENT ]");
        System.out.println("-------------------------------------------------------");
    }

    public void displayCartItems(List<Stock> cart) {
        System.out.println("CART ITEMS:");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-15s %-20s %-10s %-10s %-15s\n", "ORDER NO", "PRODUCT NAME", "QUANTITY", "PRICE", "TOTAL");
        System.out.println("-------------------------------------------------------");

        for (Stock item : cart) {
            System.out.printf("%-15d %-20s %-10d RM%-9.2f RM%-14.2f\n",
                    item.getOrderNo(),
                    item.getStockName(),
                    item.getQty(),
                    item.getPrice(),
                    item.calculateTotalCost());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void printPaymentSummary(PaymentService.PaymentResult result) {
        System.out.println("\n-------------------------------------------------------");
        System.out.println("PAYMENT SUMMARY");
        System.out.println("-------------------------------------------------------");
        System.out.printf("SUBTOTAL:        RM%.2f\n", result.getSubtotal());
        System.out.printf("DISCOUNT:        RM%.2f\n", result.getDiscount());
        System.out.printf("TAX (6%%):        RM%.2f\n", result.getTax());
        System.out.println("-------------------------------------------------------");
        System.out.printf("TOTAL:           RM%.2f\n", result.getTotal());
        System.out.println("-------------------------------------------------------");
    }

    public void printPaymentSuccess() {
        System.out.println("\nPAYMENT PROCESSED SUCCESSFULLY!");
        System.out.println("Items have been saved to PaidItem.txt");
        System.out.println("Transaction has been recorded in Transaction.txt");
    }

    public void printPaymentFailure() {
        System.out.println("<<<PAYMENT FAILED.>>>");
    }
}
