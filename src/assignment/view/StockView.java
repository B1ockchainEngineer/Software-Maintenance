package assignment.view;

import assignment.model.Stock;
import java.util.List;

/**
 * View class for Stock management.
 * Handles all print outputs for stock operations.
 */
public class StockView {

    public void displayAvailableStock(List<Stock> stockList) {
        System.out.println("         [ VIEW ALL PRODUCTS IN STOCK ]");
        System.out.println("------------------------------------------------------------------");

        // Header
        System.out.printf("%-10s      %-25s%-10s  %-10s\n", "PRODUCT ID", "PRODUCT NAME", "QUANTITY", "PRICE");
        System.out.println("------------------------------------------------------------------");

        boolean found = false;
        for (Stock product : stockList) {
            // Display all products, even those with 0 quantity, for inventory view
            System.out.printf("%-10d      %-25s%-10d  RM%-10.2f\n",
                    product.getStockID(),
                    product.getStockName(),
                    product.getQty(),
                    product.getPrice());
            System.out.println("------------------------------------------------------------------");
            found = true;
        }

        if (!found) {
            System.out.println("No products found in the inventory file.");
            System.out.println("------------------------------------------------------------------");
        }
    }

    public void printAddStockHeader() {
        System.out.println("[ ADD NEW PRODUCT ]");
        System.out.println("-------------------------------------------------------");
    }

    public void printProductID(int displayID) {
        System.out.println("PRODUCT ID >> P-" + displayID);
    }

    public void printNewStockSummary(Stock newStock) {
        System.out.println("\nPRODUCT INFORMATION:");
        System.out.println(newStock.toString());
    }

    public void printAddSuccess() {
        System.out.println("\nNEW PRODUCT ADDED TO THE SYSTEM...");
        System.out.println("---------------------------------------------------");
    }

    public void printAddFailure() {
        System.out.println("\nFAILED TO ADD PRODUCT! Check service logs.");
    }

    public void printDeleteStockMenu() {
        System.out.println("[ DELETE A PRODUCT ]");
        System.out.println("-------------------------------------------------------");
    }

    public void displayStockDetails(Stock stock) {
        System.out.println("-------------------------------------------------------");
        System.out.println("PRODUCT INFORMATION TO BE DELETED:");
        System.out.println(stock.toString());
        System.out.println("-------------------------------------------------------");
    }

    public void printDeleteSuccess(int inputID) {
        System.out.println("PRODUCT WITH ID " + inputID + " HAS BEEN DELETED");
    }

    public void printDeleteFailure() {
        System.out.println("PRODUCT DELETION FAILED.");
    }

    public void printStockNotFound(int inputID) {
        System.out.println("PRODUCT WITH ID " + inputID + " NOT FOUND");
    }
}
