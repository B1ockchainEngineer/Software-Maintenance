package assignment.controller;

import assignment.model.Stock;
import assignment.service.StockService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import assignment.util.config.StockConfig;
import assignment.view.StockView;

import java.util.logging.Logger;

/**
 * Controller for stock/inventory management flows.
 * Handles menu presentation and delegates work to StockService.
 */
public class StockController {

    private static final Logger LOGGER = Logger.getLogger(StockController.class.getName());
    private final StockService stockService;
    private final StockView stockView;

    // Constructor injection
    public StockController(StockService stockService) {
        this.stockService = stockService;
        this.stockView = new StockView();
    }

    public void view() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        stockView.displayAvailableStock(stockService.getAvailableStock());
        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void add() {
        boolean continueAdding = true;

        do {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            stockView.printAddStockHeader();

            Stock newStock = new Stock();

            int displayID = stockService.getNextStockID();
            stockView.printProductID(displayID);
            ConsoleUtil.systemPause();

            // 1. Get Product Name
            String inputName;
            do {
                System.out.print("ENTER PRODUCT NAME [OR 'E' TO Exit]: ");
                inputName = ValidationUtil.scanner.nextLine();

                if (inputName.equalsIgnoreCase("E")) {
                    System.out.println("\nEXITING PRODUCT ADDITION");
                    continueAdding = false;
                    break;
                }

                if (inputName.trim().isEmpty()) {
                    LOGGER.warning("Invalid product name: empty input");
                    System.out.println(StockConfig.ErrorMessage.INVALID_PRODUCT_NAME);
                    continue;
                }

                if (!stockService.isStockNameUnique(inputName)) {
                    LOGGER.warning("Product name already exists: " + inputName);
                    System.out.println(StockConfig.ErrorMessage.NAME_ALREADY_EXISTS);
                } else {
                    newStock.setStockName(inputName);
                    break;
                }
            } while (true);

            if (!continueAdding) break;

            // 2. Get Quantity
            int qty;
            do {
                System.out.print(String.format("ENTER PRODUCT QUANTITY (Must be %d-%d): ",
                        StockConfig.MIN_QUANTITY, StockConfig.MAX_QUANTITY));
                qty = ValidationUtil.intValidation(StockConfig.MIN_QUANTITY, StockConfig.MAX_QUANTITY);
                if (qty == -9999) {
                    LOGGER.warning("Invalid quantity input: out of range or invalid format");
                    System.out.println(StockConfig.ErrorMessage.INVALID_QUANTITY);
                    continue;
                }
                newStock.setQty(qty);
                break;
            } while (true);

            // 3. Get Price
            double price;
            do {
                System.out.print(String.format("ENTER PRICE (Must be >= RM %.2f): RM ", StockConfig.MIN_PRICE));
                price = ValidationUtil.doubleValidation();
                if (price == -9999) continue;
                if (price >= StockConfig.MIN_PRICE) {
                    newStock.setPrice(price);
                    break;
                } else {
                    LOGGER.warning("Invalid price input: " + price + " (minimum: " + StockConfig.MIN_PRICE + ")");
                    System.out.println(StockConfig.ErrorMessage.INVALID_PRICE);
                }
            } while (true);

            // Show Summary
            stockView.printNewStockSummary(newStock);

            // 4. Confirmation and Save
            OUTER:
            while (true) {
                char confirmation = ValidationUtil.confirmValidation("\nDO YOU WANT TO ADD THIS PRODUCT? (Y = YES / N = NO): ");

                switch (confirmation) {
                    case 'Y' -> {
                        if (stockService.addNewStock(newStock)) {
                            System.out.println(StockConfig.SuccessfulMessage.PRODUCT_ADDED);
                            System.out.println("---------------------------------------------------");
                        } else {
                            LOGGER.warning("Failed to add stock: name already exists - " + newStock.getStockName());
                            System.out.println(StockConfig.ErrorMessage.NAME_ALREADY_EXISTS);
                        }
                        break OUTER;
                    }
                    case 'N' -> {
                        System.out.println("\nPRODUCT NOT ADDED. RETURNING TO THE MAIN MENU...");
                        break OUTER;
                    }
                    default -> {
                        LOGGER.warning("Invalid confirmation option: " + confirmation);
                        System.out.println(StockConfig.ErrorMessage.INVALID_OPTION);
                    }
                }
            }

            // 5. Ask to continue
            System.out.print("\nDO YOU WANT TO ADD ANOTHER PRODUCT? (Y FOR YES, ANY KEY TO EXIT): ");
            String continueInput = ValidationUtil.scanner.nextLine().toUpperCase();

            if (!continueInput.equals("Y")) {
                System.out.println("EXITING PRODUCT ADDITION...");
                continueAdding = false;
            }
        } while (continueAdding);

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void delete() {
        boolean continueDelete;

        do {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            stockView.printDeleteStockMenu();

            // Show current stock list so staff can see IDs before deleting
            stockView.displayAvailableStock(stockService.getAllStock());
            continueDelete = true;

            System.out.print("ENTER PRODUCT ID TO BE DELETED [OR '0' TO EXIT]: ");
            int inputID = ValidationUtil.intValidation(0, 0);

            if (inputID <= 0) {
                if (inputID == 0) System.out.println("EXISITING DELETE OPERATION.");
                continueDelete = false;
                break;
            }

            Stock productToDelete = stockService.getStockByID(inputID);

            if (productToDelete != null) {
                // Safety: block deletion if quantity > 0 to avoid accidental loss of in-use stock
                if (productToDelete.getQty() > 0) {
                    LOGGER.warning("Cannot delete product ID " + inputID + ": quantity > 0 (current qty: " + productToDelete.getQty() + ")");
                    System.out.println("<<< CANNOT DELETE: PRODUCT STILL HAS QUANTITY > 0. PLEASE ADJUST STOCK TO 0 BEFORE DELETING. >>>");
                    ConsoleUtil.systemPause();
                    continue;
                }

                stockView.displayStockDetails(productToDelete);

                char confirm = ValidationUtil.confirmValidation("ARE YOU SURE YOU WANT TO DELETE THIS PRODUCT? (Y = YES, N = CANCEL): ");

                if (confirm == 'Y') {
                    if (stockService.deleteStock(inputID)) {
                        System.out.println(String.format(StockConfig.SuccessfulMessage.PRODUCT_DELETED, inputID));
                    } else {
                        LOGGER.warning("Failed to delete product ID: " + inputID);
                        System.out.println(StockConfig.ErrorMessage.DELETE_FAILED);
                    }
                } else {
                    System.out.println("DELETION CANCELLED.");
                }
            } else {
                LOGGER.warning("Stock not found for deletion: ID " + inputID);
                System.out.println(String.format(StockConfig.ErrorMessage.STOCK_NOT_FOUND, inputID));
            }

            System.out.print("\nDO YOU WANT TO DELETE ANOTHER PRODUCT? (Y FOR YES, ANY KEY TO EXIT): ");
            String input = ValidationUtil.scanner.nextLine().toUpperCase();

            if (!input.equals("Y")) {
                System.out.println("EXITING PRODUCT DELETION");
                continueDelete = false;
            }
        } while (continueDelete);

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    /**
     * Allows editing of stock details (Name, Quantity, Price).
     */
    public void edit() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("[ EDIT PRODUCT ]");
        System.out.println("-------------------------------------------------------");

        // Show list so staff can see IDs
        stockView.displayAvailableStock(stockService.getAllStock());

        System.out.print("ENTER PRODUCT ID TO EDIT [OR '0' TO EXIT]: ");
        int inputID = ValidationUtil.intValidation(0, 0);
        if (inputID <= 0) {
            ConsoleUtil.clearScreen();
            return;
        }

        Stock target = stockService.getStockByID(inputID);
        if (target == null) {
            LOGGER.warning("Stock not found for editing: ID " + inputID);
            System.out.println(String.format(StockConfig.ErrorMessage.STOCK_NOT_FOUND, inputID));
            ConsoleUtil.systemPause();
            return;
        }

        boolean done = false;
        while (!done) {
            System.out.println("-------------------------------------------------------");
            System.out.println("CURRENT PRODUCT DETAILS:");
            stockView.displayStockDetails(target);
            System.out.println("-------------------------------------------------------");
            System.out.println("WHAT DO YOU WANT TO EDIT?");
            System.out.println("1. PRODUCT NAME");
            System.out.println("2. QUANTITY");
            System.out.println("3. PRICE");
            System.out.println("0. BACK");
            System.out.print("YOUR CHOICE: ");

            int opt = ValidationUtil.intValidation(0, 3);
            if (opt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            switch (opt) {
                case 1 -> {
                    String newName;
                    do {
                        System.out.print("ENTER NEW PRODUCT NAME: ");
                        newName = ValidationUtil.scanner.nextLine();
                        if (newName.trim().isEmpty()) {
                            LOGGER.warning("Invalid product name during edit: empty input");
                            System.out.println(StockConfig.ErrorMessage.INVALID_PRODUCT_NAME);
                            continue;
                        }
                        if (!stockService.isStockNameUniqueForUpdate(newName, target.getStockID())) {
                            LOGGER.warning("Product name already exists during edit: " + newName + " (current ID: " + target.getStockID() + ")");
                            System.out.println(StockConfig.ErrorMessage.NAME_ALREADY_EXISTS);
                        } else {
                            target.setStockName(newName);
                            break;
                        }
                    } while (true);
                }
                case 2 -> {
                    int newQty;
                    do {
                        System.out.print(String.format("ENTER NEW QUANTITY (%d-%d): ",
                                StockConfig.MIN_QUANTITY, StockConfig.MAX_QUANTITY));
                        newQty = ValidationUtil.intValidation(StockConfig.MIN_QUANTITY, StockConfig.MAX_QUANTITY);
                        if (newQty == -9999) {
                            LOGGER.warning("Invalid quantity input during edit: out of range or invalid format");
                            System.out.println(StockConfig.ErrorMessage.INVALID_QUANTITY);
                            continue;
                        }
                        target.setQty(newQty);
                        break;
                    } while (true);
                }
                case 3 -> {
                    double newPrice;
                    do {
                        System.out.print(String.format("ENTER NEW PRICE (>= RM %.2f): RM ", StockConfig.MIN_PRICE));
                        newPrice = ValidationUtil.doubleValidation();
                        if (newPrice == -9999) continue;
                        if (newPrice >= StockConfig.MIN_PRICE) {
                            target.setPrice(newPrice);
                            break;
                        } else {
                            LOGGER.warning("Invalid price input during edit: " + newPrice + " (minimum: " + StockConfig.MIN_PRICE + ")");
                            System.out.println(StockConfig.ErrorMessage.INVALID_PRICE);
                        }
                    } while (true);
                }
                case 0 -> done = true;
                default -> {
                    LOGGER.warning("Invalid edit option: " + opt);
                    System.out.println(StockConfig.ErrorMessage.INVALID_OPTION);
                }
            }

            if (!done) {
                // Persist changes
                boolean updated = stockService.updateStock(
                        target.getStockID(),
                        target.getStockName(),
                        target.getQty(),
                        target.getPrice()
                );
                if (updated) {
                    System.out.println(StockConfig.SuccessfulMessage.PRODUCT_UPDATED);
                    // Reload fresh data to confirm persisted values
                    Stock refreshed = stockService.getStockByID(target.getStockID());
                    if (refreshed != null) {
                        stockView.displayStockDetails(refreshed);
                        target = refreshed; // keep local reference in sync
                    }
                } else {
                    LOGGER.warning("Update failed for stock ID " + target.getStockID() + ": check name duplicates");
                    System.out.println("<<< UPDATE FAILED. CHECK NAME DUPLICATES. >>>");
                }

                System.out.print("EDIT MORE FIELDS FOR THIS PRODUCT? (Y = YES, N = NO): ");
                char more = ValidationUtil.charValidation();
                if (more == 'N') {
                    done = true;
                }
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }
}