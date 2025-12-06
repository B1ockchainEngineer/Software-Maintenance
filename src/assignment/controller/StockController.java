package assignment.controller;

import assignment.model.Stock;
import assignment.service.StockService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import assignment.view.StockView;
import java.util.List;
import java.util.Scanner;

public class StockController {
    private final StockService stockService;
    private final StockView stockView;
    private final Scanner scanner;

    public StockController(StockService stockService) {
        this.stockService = stockService;
        this.stockView = new StockView();
        // Scanner is fine here, as it's primarily used for non-validated string inputs (like confirmation)
        this.scanner = new Scanner(System.in);
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
                inputName = scanner.nextLine();

                if (inputName.equalsIgnoreCase("E")) {
                    System.out.println("\nEXITING PRODUCT ADDITION");
                    continueAdding = false;
                    break;
                }

                if (inputName.trim().isEmpty()) {
                    System.out.println("<<<INVALID PRODUCT NAME! Please enter a valid name.>>>");
                    continue;
                }

                if (!stockService.isStockNameUnique(inputName)) {
                    System.out.println("<<<The item name already exists, try another name!>>>");
                } else {
                    newStock.setStockName(inputName);
                    break;
                }
            } while (true);

            if (!continueAdding) break;

            // 2. Get Quantity
            int qty;
            do {
                System.out.print("ENTER PRODUCT QUANTITY TO BE ADDED (Must be >= 1): ");
                qty = ValidationUtil.intValidation(1, 100000);
                if (qty == -9999) continue;
                newStock.setQty(qty);
                break;
            } while (true);

            // 3. Get Price
            double price;
            do {
                System.out.print("ENTER PRICE OF THE PRODUCT (Must be >= 1.00):  RM ");
                price = ValidationUtil.doubleValidation();
                if (price == -9999) continue;
                if (price >= 1.0) {
                    newStock.setPrice(price);
                    break;
                } else {
                    System.out.println("<<<PRICE CANNOT BE LESS THAN RM 1.00!!!>>>");
                }
            } while (true);

            // Show Summary
            stockView.printNewStockSummary(newStock);

            // 4. Confirmation and Save
            OUTER:
            while (true) {
                System.out.print("\nDO YOU WANT TO ADD THIS PRODUCT? (Y = YES / N = NO): ");
                char confirmation = ValidationUtil.charValidation();

                switch (confirmation) {
                    case 'Y' -> {
                        if (stockService.addNewStock(newStock)) {
                            stockView.printAddSuccess();
                        } else {
                            stockView.printAddFailure();
                        }
                        break OUTER;
                    }
                    case 'N' -> {
                        System.out.println("\nPRODUCT NOT ADDED. RETURNING TO THE MAIN MENU...");
                        break OUTER;
                    }
                    default ->
                            System.out.println("<<<Invalid input. Please enter 'Y' for yes or 'N' for no.>>>");
                }
            }

            // 5. Ask to continue
            System.out.print("\nDO YOU WANT TO ADD ANOTHER PRODUCT? (Y FOR YES, ANY KEY TO EXIT): ");
            String continueInput = scanner.nextLine().toUpperCase();

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
                stockView.displayStockDetails(productToDelete);

                System.out.print("ARE YOU SURE YOU WANT TO DELETE THIS PRODUCT? (Y = YES, N = CANCEL): ");
                char confirm = ValidationUtil.charValidation();

                if (confirm == 'Y') {
                    if (stockService.deleteStock(inputID)) {
                        stockView.printDeleteSuccess(inputID);
                    } else {
                        stockView.printDeleteFailure();
                    }
                } else {
                    System.out.println("DELETION CANCELLED.");
                }
            } else {
                stockView.printStockNotFound(inputID);
            }

            System.out.print("\nDO YOU WANT TO DELETE ANOTHER PRODUCT? (Y FOR YES, ANY KEY TO EXIT): ");
            String input = scanner.nextLine().toUpperCase();

            if (!input.equals("Y")) {
                System.out.println("EXITING PRODUCT DELETION");
                continueDelete = false;
            }
        } while (continueDelete);

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }
}