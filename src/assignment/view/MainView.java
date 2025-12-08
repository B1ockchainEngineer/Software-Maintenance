package assignment.view;

import assignment.enums.LogMenu;
import assignment.enums.MainMenu;
import assignment.enums.StockMenu;
import assignment.model.Staff;
import assignment.util.config.AppConfig;

/**
 * View class for Main application entry and global menus.
 */
public class MainView {

    public void printLoginMenu() {
        System.out.println("[ LOGIN MENU ]");
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println("Welcome to TAR CAFE Management System");
        System.out.println("Please select an option:");
        System.out.println(AppConfig.SEPARATOR_LINE);
        for (LogMenu menu : LogMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printMainMenu(Staff currentStaff) {
        System.out.println("[ MAIN MENU ]");
        System.out.println(AppConfig.SEPARATOR_LINE);
        if (currentStaff != null) {
            System.out.println("Logged in as: " + currentStaff.getName().toUpperCase());
            System.out.println(AppConfig.SEPARATOR_LINE);
        }
        System.out.println("Please select an option:");
        System.out.println(AppConfig.SEPARATOR_LINE);
        for (MainMenu menu : MainMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printStockMenu() {
        System.out.println("[ FOOD AND BEVERAGE MANAGEMENT SYSTEM ]");
        System.out.println(AppConfig.SEPARATOR_LINE);
        for (StockMenu menu : StockMenu.values()) {
            System.out.printf("%d. %s\n", menu.getOption(), menu.getDescription());
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printExitMessage() {
        System.out.println("\n========================================");
        System.out.println("  THANK YOU FOR USING TAR CAFE SYSTEM");
        System.out.println(AppConfig.SEPARATOR_LONG);
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

    /**
     * Prints the selection prompt used across all menus.
     */
    public void printSelectionPrompt() {
        System.out.print(AppConfig.PROMPT_SELECTION);
    }

    /**
     * Prints the invalid option error message.
     */
    public void printInvalidOptionMessage() {
        System.out.println(AppConfig.MSG_INVALID_OPTION);
    }
}
