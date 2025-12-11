package assignment.controller;

import assignment.enums.QuantityEditMenu;
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
                stockView.printEnterProductNamePrompt();
                inputName = ValidationUtil.scanner.nextLine();

                if (inputName.equalsIgnoreCase("E")) {
                    stockView.printExitingProductAddition();
                    continueAdding = false;
                    break;
                }

                if (inputName.trim().isEmpty()) {
                    LOGGER.warning("Invalid product name: empty input");
                    stockView.printInvalidProductName();
                    continue;
                }

                if (!stockService.isStockNameUnique(inputName)) {
                    LOGGER.warning("Product name already exists: " + inputName);
                    stockView.printNameAlreadyExists();
                } else {
                    newStock.setStockName(inputName);
                    break;
                }
            } while (true);

            if (!continueAdding) break;

            // 2. Get Quantity
            int qty;
            do {
                stockView.printEnterProductQuantityPrompt(StockConfig.MIN_QUANTITY, StockConfig.MAX_QUANTITY);
                qty = ValidationUtil.intValidation(StockConfig.MIN_QUANTITY, StockConfig.MAX_QUANTITY);
                if (qty == -9999) {
                    LOGGER.warning("Invalid quantity input: out of range or invalid format");
                    stockView.printInvalidQuantity();
                    continue;
                }
                newStock.setQty(qty);
                break;
            } while (true);

            // 3. Get Price
            double price;
            do {
                stockView.printEnterPricePrompt(StockConfig.MIN_PRICE);
                price = ValidationUtil.doubleValidation();
                if (price == -9999) continue;
                if (price >= StockConfig.MIN_PRICE) {
                    newStock.setPrice(price);
                    break;
                } else {
                    LOGGER.warning("Invalid price input: " + price + " (minimum: " + StockConfig.MIN_PRICE + ")");
                    stockView.printInvalidPrice();
                }
            } while (true);

            // Show Summary
            stockView.printNewStockSummary(newStock);

            // 4. Confirmation and Save
            OUTER:
            while (true) {
                char confirmation = ValidationUtil.confirmValidation("\n" + StockConfig.PROMPT_CONFIRM_ADD_PRODUCT);

                switch (confirmation) {
                    case 'Y' -> {
                        if (stockService.addNewStock(newStock)) {
                            stockView.printProductAdded();
                        } else {
                            LOGGER.warning("Failed to add stock: name already exists - " + newStock.getStockName());
                            stockView.printNameAlreadyExists();
                        }
                        break OUTER;
                    }
                    case 'N' -> {
                        stockView.printProductNotAdded();
                        break OUTER;
                    }
                    default -> {
                        LOGGER.warning("Invalid confirmation option: " + confirmation);
                        stockView.printInvalidOption();
                    }
                }
            }

            // 5. Ask to continue
            stockView.printAddAnotherProductPrompt();
            String continueInput = ValidationUtil.scanner.nextLine().toUpperCase();

            if (!continueInput.equals("Y")) {
                stockView.printExitingProductAddition2();
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

            stockView.printEnterProductIdToDeletePrompt();
            int inputID = ValidationUtil.intValidation(0, 0);

            if (inputID <= 0) {
                if (inputID == 0) stockView.printExitingDeleteOperation();
                continueDelete = false;
                break;
            }

            Stock productToDelete = stockService.getStockByID(inputID);

            if (productToDelete != null) {
                // Safety: block deletion if quantity > 0 to avoid accidental loss of in-use stock
                if (productToDelete.getQty() > 0) {
                    LOGGER.warning("Cannot delete product ID " + inputID + ": quantity > 0 (current qty: " + productToDelete.getQty() + ")");
                    stockView.printCannotDeleteWithQuantity();
                    ConsoleUtil.systemPause();
                    continue;
                }

                stockView.displayStockDetails(productToDelete);

                char confirm = ValidationUtil.confirmValidation(stockView.getConfirmDeleteProductPrompt());

                if (confirm == 'Y') {
                    if (stockService.deleteStock(inputID)) {
                        stockView.printProductDeleted(inputID);
                    } else {
                        LOGGER.warning("Failed to delete product ID: " + inputID);
                        stockView.printDeleteFailed();
                    }
                } else {
                    stockView.printDeletionCancelled();
                }
            } else {
                LOGGER.warning("Stock not found for deletion: ID " + inputID);
                stockView.printStockNotFound(inputID);
            }

            stockView.printDeleteAnotherProductPrompt();
            String input = ValidationUtil.scanner.nextLine().toUpperCase();

            if (!input.equals("Y")) {
                stockView.printExitingProductDeletion();
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
        stockView.printEditProductHeader();

        // Show list so staff can see IDs
        stockView.displayAvailableStock(stockService.getAllStock());

        stockView.printEnterProductIdToEditPrompt();
        int inputID = ValidationUtil.intValidation(0, 0);
        if (inputID <= 0) {
            ConsoleUtil.clearScreen();
            return;
        }

        Stock target = stockService.getStockByID(inputID);
        if (target == null) {
            LOGGER.warning("Stock not found for editing: ID " + inputID);
            stockView.printStockNotFound(inputID);
            ConsoleUtil.systemPause();
            return;
        }

        boolean done = false;
        while (!done) {
            stockView.printCurrentProductDetailsHeader();
            stockView.displayStockDetails(target);
            stockView.printEditMenuSeparator();
            stockView.printWhatToEdit();
            stockView.printEditChoicePrompt();

            int opt = ValidationUtil.intValidation(0, 3);
            if (opt == -9999) {
                ConsoleUtil.systemPause();
                continue;
            }

            switch (opt) {
                case 1 -> {
                    String newName;
                    do {
                        stockView.printEnterNewProductNamePrompt();
                        newName = ValidationUtil.scanner.nextLine();
                        if (newName.trim().isEmpty()) {
                            LOGGER.warning("Invalid product name during edit: empty input");
                            stockView.printInvalidProductName();
                            continue;
                        }
                        if (!stockService.isStockNameUniqueForUpdate(newName, target.getStockID())) {
                            LOGGER.warning("Product name already exists during edit: " + newName + " (current ID: " + target.getStockID() + ")");
                            stockView.printNameAlreadyExists();
                        } else {
                            target.setStockName(newName);
                            break;
                        }
                    } while (true);
                }
                case 2 -> {
                    editQuantity(target);
                }
                case 3 -> {
                    double newPrice;
                    do {
                        stockView.printEnterNewPricePrompt(StockConfig.MIN_PRICE);
                        newPrice = ValidationUtil.doubleValidation();
                        if (newPrice == -9999) continue;
                        if (newPrice >= StockConfig.MIN_PRICE) {
                            target.setPrice(newPrice);
                            break;
                        } else {
                            LOGGER.warning("Invalid price input during edit: " + newPrice + " (minimum: " + StockConfig.MIN_PRICE + ")");
                            stockView.printInvalidPrice();
                        }
                    } while (true);
                }
                case 0 -> done = true;
                default -> {
                    LOGGER.warning("Invalid edit option: " + opt);
                    stockView.printInvalidOption();
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
                    stockView.printProductUpdated();
                    // Reload fresh data to confirm persisted values
                    Stock refreshed = stockService.getStockByID(target.getStockID());
                    if (refreshed != null) {
                        stockView.displayStockDetails(refreshed);
                        target = refreshed; // keep local reference in sync
                    }
                } else {
                    LOGGER.warning("Update failed for stock ID " + target.getStockID() + ": check name duplicates");
                    stockView.printUpdateFailed();
                }

                stockView.printEditMoreFieldsPrompt();
                char more = ValidationUtil.charValidation();
                if (more == 'N') {
                    done = true;
                }
            }
        }

        ConsoleUtil.systemPause();
        ConsoleUtil.clearScreen();
    }

    /**
     * Handles quantity editing with add/reduce menu.
     * @param target The stock item to edit
     */
    private void editQuantity(Stock target) {
        ConsoleUtil.clearScreen();
        stockView.printEditMenuSeparator();
        stockView.printQuantityEditMenu();
        
        stockView.printCurrentQuantity(target.getQty());
        stockView.printEditMenuSeparator();
        stockView.printQuantityChoicePrompt();
        
        int choice = ValidationUtil.intValidation(1, 2);
        if (choice == -9999) {
            LOGGER.warning("Invalid quantity edit choice");
            stockView.printInvalidQuantityChoice();
            ConsoleUtil.systemPause();
            return;
        }
        
        QuantityEditMenu editMenu = QuantityEditMenu.getByOption(choice);
        if (editMenu == null) {
            LOGGER.warning("Invalid quantity edit choice: " + choice);
            stockView.printInvalidQuantityChoice();
            ConsoleUtil.systemPause();
            return;
        }
        
        int currentQty = target.getQty();
        int quantityChange;
        int newQty;
        
        switch (editMenu) {
            case ADD_STOCK -> {
                // Check if already at maximum
                if (currentQty >= StockConfig.MAX_QUANTITY) {
                    stockView.printQuantityAtMaximum(StockConfig.MAX_QUANTITY);
                    ConsoleUtil.systemPause();
                    return;
                }
                
                int maxAddable = StockConfig.MAX_QUANTITY - currentQty;
                stockView.printQuantityToAddPrompt();
                quantityChange = ValidationUtil.intValidation(1, maxAddable);
                
                if (quantityChange == -9999) {
                    LOGGER.warning("Invalid quantity to add");
                    stockView.printInvalidQuantity();
                    ConsoleUtil.systemPause();
                    return;
                }
                
                newQty = currentQty + quantityChange;
                target.setQty(newQty);
                stockView.printQuantityAddedSuccess(newQty);
            }
            case REDUCE_STOCK -> {
                stockView.printQuantityToReducePrompt();
                quantityChange = ValidationUtil.intValidation(1, currentQty);
                
                if (quantityChange == -9999) {
                    LOGGER.warning("Invalid quantity to reduce");
                    stockView.printInvalidQuantity();
                    ConsoleUtil.systemPause();
                    return;
                }
                
                if (quantityChange > currentQty) {
                    LOGGER.warning("Cannot reduce more than current quantity: " + quantityChange + " > " + currentQty);
                    stockView.printCannotReduceMoreThanCurrent();
                    ConsoleUtil.systemPause();
                    return;
                }
                
                newQty = currentQty - quantityChange;
                
                // Check if new quantity is below minimum
                if (newQty < StockConfig.MIN_QUANTITY) {
                    LOGGER.warning("Quantity below minimum after reducing: " + newQty + " (min: " + StockConfig.MIN_QUANTITY + ")");
                    stockView.printInvalidQuantity();
                    ConsoleUtil.systemPause();
                    return;
                }
                
                target.setQty(newQty);
                stockView.printQuantityReducedSuccess(newQty);
            }
        }
        
        ConsoleUtil.systemPause();
    }
}