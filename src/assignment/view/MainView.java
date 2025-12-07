package assignment.view;

import assignment.enums.LogMenu;
import assignment.enums.MainMenu;
import assignment.enums.OrderMenu;
import assignment.enums.SalesMenu;
import assignment.enums.StockMenu;
import assignment.model.Staff;
import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.util.config.TransactionConfig;
import java.util.List;

/**
 * View class for Main application entry and global menus.
 */
public class MainView {

    public void printLoginMenu() {
        System.out.println("[ LOGIN MENU ]");
        System.out.println("-------------------------------------------------------");
        System.out.println("Welcome to TAR CAFE Management System");
        System.out.println("Please select an option:");
        System.out.println("-------------------------------------------------------");
        for (LogMenu menu : LogMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void printMainMenu(Staff currentStaff) {
        System.out.println("[ MAIN MENU ]");
        System.out.println("-------------------------------------------------------");
        if (currentStaff != null) {
            System.out.println("Logged in as: " + currentStaff.getName().toUpperCase());
            System.out.println("-------------------------------------------------------");
        }
        System.out.println("Please select an option:");
        System.out.println("-------------------------------------------------------");
        for (MainMenu menu : MainMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void printStockMenu() {
        System.out.println("[ FOOD AND BEVERAGE MANAGEMENT SYSTEM ]");
        System.out.println("-------------------------------------------------------");
        for (StockMenu menu : StockMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void printSalesMenu() {
        System.out.println("[ SALES MANAGEMENT SYSTEM ]");
        System.out.println("-------------------------------------------------------");
        for (SalesMenu menu : SalesMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void printCreateOrderMenu() {
        System.out.println("[ ORDERING MANAGEMENT]");
        System.out.println("-------------------------------------------------------");
        for (OrderMenu menu : OrderMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println("-------------------------------------------------------");
    }

    public void printTransactionSummary(List<Transaction> transactions) {
        System.out.println(TransactionConfig.TITLE_TRANSACTION_REPORT);
        System.out.println("-------------------------------------------------------");

        if (transactions.isEmpty()) {
            System.out.println(TransactionConfig.MSG_NO_TRANSACTIONS);
        } else {
            System.out.printf("%-10s %-15s %-15s %-15s %-15s\n", 
                    TransactionConfig.HEADER_NO, 
                    TransactionConfig.HEADER_SUBTOTAL, 
                    TransactionConfig.HEADER_DISCOUNT, 
                    TransactionConfig.HEADER_TAX, 
                    TransactionConfig.HEADER_TOTAL);
            System.out.println("-------------------------------------------------------");

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

            System.out.println("-------------------------------------------------------");
            System.out.printf("%-10s RM%-14.2f RM%-14.2f RM%-14.2f RM%-14.2f\n",
                    TransactionConfig.LABEL_GRAND_TOTAL,
                    grandTotalSubtotal,
                    grandTotalDiscount,
                    grandTotalTax,
                    grandTotal);
            System.out.println("-------------------------------------------------------");
            System.out.println(TransactionConfig.LABEL_TOTAL_TRANSACTIONS + " " + transactions.size());
            System.out.println("-------------------------------------------------------");
        }
    }

    public void printTransactionDetails(Transaction transaction, int transactionNo) {
        System.out.println("\n===============================================================");
        System.out.printf(TransactionConfig.TITLE_TRANSACTION_DETAILS + "\n", transactionNo);
        System.out.println("===============================================================");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-15s RM%.2f\n", TransactionConfig.LABEL_SUBTOTAL, transaction.getSubtotal());
        System.out.printf("%-15s RM%.2f\n", TransactionConfig.LABEL_DISCOUNT, transaction.getDiscount());
        System.out.printf("%-15s RM%.2f\n", TransactionConfig.LABEL_TAX, transaction.getTax());
        System.out.printf("%-15s RM%.2f\n", TransactionConfig.LABEL_TOTAL, transaction.getTotal());
        System.out.println("-------------------------------------------------------");

        List<Stock> items = transaction.getItems();
        if (items.isEmpty()) {
            System.out.println(TransactionConfig.LABEL_NO_ITEMS);
        } else {
            System.out.println(TransactionConfig.LABEL_ITEMS);
            System.out.printf("%-15s %-25s %-10s %-12s %-15s\n", 
                    TransactionConfig.HEADER_PRODUCT_ID, 
                    TransactionConfig.HEADER_PRODUCT_NAME, 
                    TransactionConfig.HEADER_QUANTITY, 
                    TransactionConfig.HEADER_PRICE, 
                    TransactionConfig.HEADER_ITEM_TOTAL);
            System.out.println("-------------------------------------------------------");
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
        System.out.println("-------------------------------------------------------");
        System.out.println("===============================================================");
    }

    public void printTransactionSelectionPrompt(int maxTransaction) {
        System.out.printf("\n" + TransactionConfig.MSG_SELECT_TRANSACTION, maxTransaction);
    }

    public void printInvalidTransactionNumber() {
        System.out.println(TransactionConfig.MSG_INVALID_TRANSACTION);
    }

    public void printExitMessage() {
        System.out.println("\n========================================");
        System.out.println("  THANK YOU FOR USING TAR CAFE SYSTEM");
        System.out.println("========================================");
        System.out.println("EXITING THE PROGRAM...\n");
    }

    public void printBackToMainMessage() {
        System.out.println("BACK TO MAIN MENU...");
    }

    public void printBackToPreviousMessage() {
        System.out.println("BACK TO PREVIOUS PAGE...");
    }

    public void printLoggedOutMessage() {
        System.out.println("\nRETURNING TO LOGIN MENU...");
    }
}
