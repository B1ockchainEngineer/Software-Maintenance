package assignment.controller;

import assignment.model.Stock;
import assignment.service.StockService;
import assignment.util.ConsoleUtil;
import assignment.util.ValidationUtil;
import java.util.List;
import java.util.Scanner;

public class StockController {
    private final StockService stockService;
    private final Scanner scanner;

    public StockController(StockService stockService) {
        this.stockService = stockService;
        // Scanner is fine here, as it's primarily used for non-validated string inputs (like confirmation)
        this.scanner = new Scanner(System.in);
    }

    public void view() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.logo();
        System.out.println("         [ VIEW ALL PRODUCTS IN STOCK ]");
        System.out.println("------------------------------------------------------------------");

        // Header
        System.out.printf("%-10s      %-25s%-10s  %-10s\n", "PRODUCT ID", "PRODUCT NAME", "QUANTITY", "PRICE");
        System.out.println("------------------------------------------------------------------");

        List<Stock> stockList = stockService.getAvailableStock();
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

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    public void add() {
        boolean continueAdding = true;

        do {
            ConsoleUtil.clearScreen();
            ConsoleUtil.logo();
            System.out.println("[ ADD NEW PRODUCT ]");
            System.out.println("-------------------------------------------------------");

            Stock newStock = new Stock();

            int displayID = stockService.getNextStockID();
            System.out.println("PRODUCT ID >> P-" + displayID);
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

            System.out.println("\nPRODUCT INFORMATION:");
            System.out.println(newStock.toString());

            // 4. Confirmation and Save
            OUTER:
            while (true) {
                System.out.print("\nDO YOU WANT TO ADD THIS PRODUCT? (Y = YES / N = NO): ");
                char confirmation = ValidationUtil.charValidation();

                switch (confirmation) {
                    case 'Y' -> {
                        if (stockService.addNewStock(newStock)) {
                            System.out.println("\nNEW PRODUCT ADDED TO THE SYSTEM...");
                            System.out.println("---------------------------------------------------");
                        } else {
                            System.out.println("\nFAILED TO ADD PRODUCT! Check service logs.");
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
            System.out.println("[ DELETE A PRODUCT ]");
            System.out.println("-------------------------------------------------------");
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
                System.out.println("-------------------------------------------------------");
                System.out.println("PRODUCT INFORMATION TO BE DELETED:");
                System.out.println(productToDelete.toString());
                System.out.println("-------------------------------------------------------");

                System.out.print("ARE YOU SURE YOU WANT TO DELETE THIS PRODUCT? (Y = YES, N = CANCEL): ");
                char confirm = ValidationUtil.charValidation();

                if (confirm == 'Y') {
                    if (stockService.deleteStock(inputID)) {
                        System.out.println("PRODUCT WITH ID " + inputID + " HAS BEEN DELETED");
                    } else {
                        System.out.println("PRODUCT DELETION FAILED.");
                    }
                } else {
                    System.out.println("DELETION CANCELLED.");
                }
            } else {
                System.out.println("PRODUCT WITH ID " + inputID + " NOT FOUND");
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