package assignment.view;

import assignment.model.Stock;
import assignment.util.config.AppConfig;
import assignment.util.config.StockConfig;
import java.util.List;

/**
 * View class for Stock management.
 * Handles all print outputs for stock operations.
 */
public class StockView {

    public void displayAvailableStock(List<Stock> stockList) {
        System.out.println(StockConfig.TITLE_VIEW_STOCK);
        System.out.println("------------------------------------------------------------------");

        // Header
        System.out.printf("%-10s      %-25s%-10s  %-10s\n", "PRODUCT ID", "PRODUCT NAME", "QUANTITY", "PRICE");
        System.out.println(AppConfig.SEPARATOR_LONG);

        boolean found = false;
        for (Stock product : stockList) {
            // Display all products, even those with 0 quantity, for inventory view
            System.out.printf("%-10d      %-25s%-10d  RM%-10.2f\n",
                    product.getStockID(),
                    product.getStockName(),
                    product.getQty(),
                    product.getPrice());
            System.out.println(AppConfig.SEPARATOR_LONG);
            found = true;
        }

        if (!found) {
            System.out.println(StockConfig.ErrorMessage.NO_STOCK_TO_DISPLAY);
            System.out.println("------------------------------------------------------------------");
        }
    }

    public void printAddStockHeader() {
        System.out.println(StockConfig.TITLE_ADD_PRODUCT);
        System.out.println("-------------------------------------------------------");
    }

    public void printProductID(int displayID) {
        System.out.println("PRODUCT ID >> P-" + displayID);
    }

    public void printNewStockSummary(Stock newStock) {
        System.out.println("\nPRODUCT INFORMATION:");
        System.out.println(newStock.toString());
    }

    public void printDeleteStockMenu() {
        System.out.println(StockConfig.TITLE_DELETE_PRODUCT);
        System.out.println("-------------------------------------------------------");
    }

    public void displayStockDetails(Stock stock) {
        System.out.println("-------------------------------------------------------");
        System.out.println("PRODUCT INFORMATION:");
        System.out.println(stock.toString());
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

}
