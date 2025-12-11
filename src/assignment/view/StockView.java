package assignment.view;

import assignment.enums.QuantityEditMenu;
import assignment.model.Stock;
import assignment.util.config.AppConfig;
import assignment.util.config.StockConfig;
import java.util.List;
import java.util.logging.Logger;

/**
 * View class for Stock management.
 * Handles all print outputs for stock operations.
 */
public class StockView {
    private static final Logger LOGGER = Logger.getLogger(StockView.class.getName());

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
            LOGGER.warning(StockConfig.ErrorMessage.NO_STOCK_TO_DISPLAY);
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

    public void printQuantityEditMenu() {
        System.out.println(StockConfig.TITLE_EDIT_QUANTITY);
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(StockConfig.MSG_QUANTITY_EDIT_MENU);
        for (QuantityEditMenu menu : QuantityEditMenu.values()) {
            System.out.printf("%d. %s%n", menu.getOption(), menu.getDescription());
        }
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printQuantityToAddPrompt() {
        System.out.print(StockConfig.PROMPT_ENTER_QUANTITY_TO_ADD);
    }

    public void printQuantityToReducePrompt() {
        System.out.print(StockConfig.PROMPT_ENTER_QUANTITY_TO_REDUCE);
    }

    public void printQuantityAddedSuccess(int newQuantity) {
        System.out.println(StockConfig.SuccessfulMessage.QUANTITY_ADDED_SUCCESS);
        System.out.println(String.format(StockConfig.MSG_NEW_QUANTITY, newQuantity));
    }

    public void printQuantityReducedSuccess(int newQuantity) {
        System.out.println(StockConfig.SuccessfulMessage.QUANTITY_REDUCED_SUCCESS);
        System.out.println(String.format(StockConfig.MSG_NEW_QUANTITY, newQuantity));
    }

    // ================== ADD PRODUCT VIEW METHODS ==================
    
    public void printEnterProductNamePrompt() {
        System.out.print(StockConfig.PROMPT_ENTER_PRODUCT_NAME);
    }

    public void printExitingProductAddition() {
        System.out.println(StockConfig.Message.EXITING_PRODUCT_ADDITION);
    }

    public void printInvalidProductName() {
        LOGGER.warning(StockConfig.ErrorMessage.INVALID_PRODUCT_NAME);
        System.out.println(StockConfig.ErrorMessage.INVALID_PRODUCT_NAME);
    }

    public void printNameAlreadyExists() {
        LOGGER.warning(StockConfig.ErrorMessage.NAME_ALREADY_EXISTS);
        System.out.println(StockConfig.ErrorMessage.NAME_ALREADY_EXISTS);
    }

    public void printEnterProductQuantityPrompt(int minQty, int maxQty) {
        System.out.print(String.format(StockConfig.PROMPT_ENTER_PRODUCT_QUANTITY, minQty, maxQty));
    }

    public void printInvalidQuantity() {
        LOGGER.warning(StockConfig.ErrorMessage.INVALID_QUANTITY);
        System.out.println(StockConfig.ErrorMessage.INVALID_QUANTITY);
    }

    public void printEnterPricePrompt(double minPrice) {
        System.out.print(String.format(StockConfig.PROMPT_ENTER_PRICE, minPrice));
    }

    public void printInvalidPrice() {
        LOGGER.warning(StockConfig.ErrorMessage.INVALID_PRICE);
        System.out.println(StockConfig.ErrorMessage.INVALID_PRICE);
    }

    public void printConfirmAddProductPrompt() {
        System.out.print(StockConfig.PROMPT_CONFIRM_ADD_PRODUCT);
    }

    public void printProductAdded() {
        System.out.println(StockConfig.SuccessfulMessage.PRODUCT_ADDED);
        System.out.println("---------------------------------------------------");
    }

    public void printAddAnotherProductPrompt() {
        System.out.print("\n" + StockConfig.PROMPT_ADD_ANOTHER_PRODUCT);
    }

    public void printExitingProductAddition2() {
        System.out.println(StockConfig.Message.EXITING_PRODUCT_ADDITION_2);
    }

    public void printProductNotAdded() {
        System.out.println(StockConfig.Message.PRODUCT_NOT_ADDED);
    }

    public void printInvalidOption() {
        LOGGER.warning(StockConfig.ErrorMessage.INVALID_OPTION);
        System.out.println(StockConfig.ErrorMessage.INVALID_OPTION);
    }

    // ================== DELETE PRODUCT VIEW METHODS ==================

    public void printEnterProductIdToDeletePrompt() {
        System.out.print(StockConfig.PROMPT_ENTER_PRODUCT_ID_TO_DELETE);
    }

    public void printExitingDeleteOperation() {
        System.out.println(StockConfig.Message.EXITING_DELETE_OPERATION);
    }

    public void printCannotDeleteWithQuantity() {
        LOGGER.warning(StockConfig.ErrorMessage.CANNOT_DELETE_WITH_QUANTITY);
        System.out.println(StockConfig.ErrorMessage.CANNOT_DELETE_WITH_QUANTITY);
    }

    public String getConfirmDeleteProductPrompt() {
        return StockConfig.PROMPT_CONFIRM_DELETE_PRODUCT;
    }

    public void printProductDeleted(int productId) {
        System.out.println(String.format(StockConfig.SuccessfulMessage.PRODUCT_DELETED, productId));
    }

    public void printDeleteFailed() {
        LOGGER.warning(StockConfig.ErrorMessage.DELETE_FAILED);
        System.out.println(StockConfig.ErrorMessage.DELETE_FAILED);
    }

    public void printDeletionCancelled() {
        System.out.println(StockConfig.Message.DELETION_CANCELLED);
    }

    public void printStockNotFound(int productId) {
        String errorMsg = String.format(StockConfig.ErrorMessage.STOCK_NOT_FOUND, productId);
        LOGGER.warning(errorMsg);
        System.out.println(errorMsg);
    }

    public void printDeleteAnotherProductPrompt() {
        System.out.print("\n" + StockConfig.PROMPT_DELETE_ANOTHER_PRODUCT);
    }

    public void printExitingProductDeletion() {
        System.out.println(StockConfig.Message.EXITING_PRODUCT_DELETION);
    }

    // ================== EDIT PRODUCT VIEW METHODS ==================

    public void printEditProductHeader() {
        System.out.println(StockConfig.TITLE_EDIT_PRODUCT);
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printEnterProductIdToEditPrompt() {
        System.out.print(StockConfig.PROMPT_ENTER_PRODUCT_ID_TO_EDIT);
    }

    public void printCurrentProductDetailsHeader() {
        System.out.println(AppConfig.SEPARATOR_LINE);
        System.out.println(StockConfig.MSG_CURRENT_PRODUCT_DETAILS);
    }

    public void printEditMenuSeparator() {
        System.out.println(AppConfig.SEPARATOR_LINE);
    }

    public void printWhatToEdit() {
        System.out.println(StockConfig.MSG_WHAT_TO_EDIT);
        System.out.println(StockConfig.MSG_OPTION_PRODUCT_NAME);
        System.out.println(StockConfig.MSG_OPTION_QUANTITY);
        System.out.println(StockConfig.MSG_OPTION_PRICE);
        System.out.println(StockConfig.MSG_OPTION_BACK);
    }

    public void printEditChoicePrompt() {
        System.out.print(StockConfig.PROMPT_EDIT_CHOICE);
    }

    public void printEnterNewProductNamePrompt() {
        System.out.print(StockConfig.PROMPT_ENTER_NEW_PRODUCT_NAME);
    }

    public void printEnterNewPricePrompt(double minPrice) {
        System.out.print(String.format(StockConfig.PROMPT_ENTER_NEW_PRICE, minPrice));
    }

    public void printProductUpdated() {
        System.out.println(StockConfig.SuccessfulMessage.PRODUCT_UPDATED);
    }

    public void printUpdateFailed() {
        LOGGER.warning(StockConfig.ErrorMessage.UPDATE_FAILED_CHECK_DUPLICATES);
        System.out.println(StockConfig.ErrorMessage.UPDATE_FAILED_CHECK_DUPLICATES);
    }

    public void printEditMoreFieldsPrompt() {
        System.out.print(StockConfig.PROMPT_EDIT_MORE_FIELDS);
    }

    // ================== QUANTITY EDIT VIEW METHODS ==================

    public void printCurrentQuantity(int quantity) {
        System.out.println(String.format(StockConfig.MSG_CURRENT_QUANTITY, quantity));
    }

    public void printQuantityChoicePrompt() {
        System.out.print(StockConfig.PROMPT_QUANTITY_CHOICE);
    }

    public void printInvalidQuantityChoice() {
        LOGGER.warning(StockConfig.ErrorMessage.INVALID_QUANTITY_CHOICE);
        System.out.println(StockConfig.ErrorMessage.INVALID_QUANTITY_CHOICE);
    }

    public void printQuantityAtMaximum(int maxQuantity) {
        String errorMsg = String.format(StockConfig.ErrorMessage.QUANTITY_AT_MAXIMUM, maxQuantity);
        LOGGER.warning(errorMsg);
        System.out.println(errorMsg);
    }

    public void printCannotReduceMoreThanCurrent() {
        LOGGER.warning(StockConfig.ErrorMessage.CANNOT_REDUCE_MORE_THAN_CURRENT);
        System.out.println(StockConfig.ErrorMessage.CANNOT_REDUCE_MORE_THAN_CURRENT);
    }

}
