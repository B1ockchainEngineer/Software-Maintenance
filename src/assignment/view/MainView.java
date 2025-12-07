package assignment.view;

import assignment.enums.LogMenu;
import assignment.enums.MainMenu;
import assignment.enums.OrderMenu;
import assignment.enums.SalesMenu;
import assignment.enums.StockMenu;
import assignment.model.Staff;
import assignment.repo.TransactionRepository;
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

    public void printTransactionReport(List<TransactionRepository.Transaction> transactions) {
        System.out.println("[ TRANSACTION REPORT ]");
        System.out.println("-------------------------------------------------------");

        if (transactions.isEmpty()) {
            System.out.println("NO TRANSACTIONS FOUND.");
        } else {
            System.out.printf("%-10s %-15s %-15s %-15s %-15s\n", "NO.", "SUBTOTAL", "DISCOUNT", "TAX", "TOTAL");
            System.out.println("-------------------------------------------------------");

            int transactionNo = 1;
            double grandTotalSubtotal = 0.0;
            double grandTotalDiscount = 0.0;
            double grandTotalTax = 0.0;
            double grandTotal = 0.0;

            for (TransactionRepository.Transaction transaction : transactions) {
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
                    "TOTAL:",
                    grandTotalSubtotal,
                    grandTotalDiscount,
                    grandTotalTax,
                    grandTotal);
            System.out.println("-------------------------------------------------------");
            System.out.println("TOTAL TRANSACTIONS: " + transactions.size());
        }
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
