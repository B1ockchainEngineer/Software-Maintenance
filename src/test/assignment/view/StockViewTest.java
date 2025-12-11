package assignment.view;

import assignment.model.Stock;
import assignment.util.config.StockConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StockView class.
 * Tests all display methods by capturing System.out output.
 */
class StockViewTest {

    private StockView stockView;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        stockView = new StockView();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String getOutput() {
        return outputStream.toString();
    }

    // ================== displayAvailableStock TESTS ==================

    @Test
    void displayAvailableStock_WithEmptyList_ShouldDisplayNoStockMessage() {
        // Given: Empty stock list
        List<Stock> emptyList = new ArrayList<>();

        // When: Displaying available stock
        stockView.displayAvailableStock(emptyList);

        // Then: Should show title and no stock message
        String output = getOutput();
        assertTrue(output.contains(StockConfig.TITLE_VIEW_STOCK));
        assertTrue(output.contains(StockConfig.ErrorMessage.NO_STOCK_TO_DISPLAY));
    }

    @Test
    void displayAvailableStock_WithSingleStock_ShouldDisplayCorrectly() {
        // Given: Single stock item
        List<Stock> stockList = new ArrayList<>();
        stockList.add(new Stock(10001, "Test Product", 10, 25.50));

        // When: Displaying available stock
        stockView.displayAvailableStock(stockList);

        // Then: Should display header and stock details
        String output = getOutput();
        assertTrue(output.contains(StockConfig.TITLE_VIEW_STOCK));
        assertTrue(output.contains("PRODUCT ID"));
        assertTrue(output.contains("PRODUCT NAME"));
        assertTrue(output.contains("QUANTITY"));
        assertTrue(output.contains("PRICE"));
        assertTrue(output.contains("10001"));
        assertTrue(output.contains("TEST PRODUCT"));
        assertTrue(output.contains("10"));
        assertTrue(output.contains("25.50"));
    }

    @Test
    void displayAvailableStock_WithMultipleStocks_ShouldDisplayAll() {
        // Given: Multiple stock items
        List<Stock> stockList = new ArrayList<>();
        stockList.add(new Stock(10001, "Product A", 10, 5.0));
        stockList.add(new Stock(10002, "Product B", 20, 7.5));
        stockList.add(new Stock(10003, "Product C", 15, 10.0));

        // When: Displaying available stock
        stockView.displayAvailableStock(stockList);

        // Then: Should display all products
        String output = getOutput();
        assertTrue(output.contains("10001"));
        assertTrue(output.contains("PRODUCT A"));
        assertTrue(output.contains("10002"));
        assertTrue(output.contains("PRODUCT B"));
        assertTrue(output.contains("10003"));
        assertTrue(output.contains("PRODUCT C"));
    }

    @Test
    void displayAvailableStock_WithZeroQuantity_ShouldStillDisplay() {
        // Given: Stock with zero quantity
        List<Stock> stockList = new ArrayList<>();
        stockList.add(new Stock(10004, "Out of Stock", 0, 10.0));

        // When: Displaying available stock
        stockView.displayAvailableStock(stockList);

        // Then: Should still display the product
        String output = getOutput();
        assertTrue(output.contains("10004"));
        assertTrue(output.contains("OUT OF STOCK"));
        assertTrue(output.contains("0"));
    }

    // ================== printAddStockHeader TESTS ==================

    @Test
    void printAddStockHeader_ShouldDisplayCorrectTitle() {
        // When: Printing add stock header
        stockView.printAddStockHeader();

        // Then: Should display title and separator
        String output = getOutput();
        assertTrue(output.contains(StockConfig.TITLE_ADD_PRODUCT));
        assertTrue(output.contains("-------------------------------------------------------"));
    }

    // ================== printProductID TESTS ==================

    @Test
    void printProductID_ShouldDisplayFormattedID() {
        // When: Printing product ID
        stockView.printProductID(10005);

        // Then: Should display formatted ID
        String output = getOutput();
        assertTrue(output.contains("PRODUCT ID >> P-10005"));
    }

    @Test
    void printProductID_WithZero_ShouldDisplayZero() {
        stockView.printProductID(0);
        String output = getOutput();
        assertTrue(output.contains("P-0"));
    }

    // ================== printNewStockSummary TESTS ==================

    @Test
    void printNewStockSummary_ShouldDisplayProductInformation() {
        // Given: A new stock item
        Stock newStock = new Stock(10006, "New Product", 25, 15.75);

        // When: Printing new stock summary
        stockView.printNewStockSummary(newStock);

        // Then: Should display product information
        String output = getOutput();
        assertTrue(output.contains("PRODUCT INFORMATION:"));
        assertTrue(output.contains("10006"));
        assertTrue(output.contains("NEW PRODUCT"));
        assertTrue(output.contains("25"));
        assertTrue(output.contains("15.75"));
    }

    // ================== printDeleteStockMenu TESTS ==================

    @Test
    void printDeleteStockMenu_ShouldDisplayCorrectTitle() {
        // When: Printing delete stock menu
        stockView.printDeleteStockMenu();

        // Then: Should display title and separator
        String output = getOutput();
        assertTrue(output.contains(StockConfig.TITLE_DELETE_PRODUCT));
        assertTrue(output.contains("-------------------------------------------------------"));
    }

    // ================== displayStockDetails TESTS ==================

    @Test
    void displayStockDetails_ShouldDisplayProductInformation() {
        // Given: A stock item
        Stock stock = new Stock(10007, "Detail Test", 30, 20.50);

        // When: Displaying stock details
        stockView.displayStockDetails(stock);

        // Then: Should display separator, title, and all product details
        String output = getOutput();
        assertTrue(output.contains("-------------------------------------------------------"));
        assertTrue(output.contains("PRODUCT INFORMATION:"));
        assertTrue(output.contains("10007"));
        assertTrue(output.contains("DETAIL TEST"));
        assertTrue(output.contains("30"));
        assertTrue(output.contains("20.50"));
    }

    @Test
    void displayStockDetails_ShouldIncludeSeparatorLine() {
        Stock stock = new Stock(10008, "Separator Test", 5, 5.0);
        stockView.displayStockDetails(stock);
        String output = getOutput();
        // Should end with separator line
        assertTrue(output.contains("-------------------------------------------------------"));
    }

    @Test
    void displayStockDetails_WithZeroValues_ShouldDisplayCorrectly() {
        Stock stock = new Stock();
        stockView.displayStockDetails(stock);
        String output = getOutput();
        assertTrue(output.contains("PRODUCT INFORMATION:"));
        assertTrue(output.contains("0"));
        assertTrue(output.contains("RM0.00"));
    }

    // ================== printQuantityEditMenu TESTS ==================

    @Test
    void printQuantityEditMenu_ShouldDisplayMenu() {
        // When: Printing quantity edit menu
        stockView.printQuantityEditMenu();

        // Then: Should display menu with options
        String output = getOutput();
        assertTrue(output.contains(StockConfig.TITLE_EDIT_QUANTITY));
        assertTrue(output.contains(StockConfig.MSG_QUANTITY_EDIT_MENU));
        assertTrue(output.contains(StockConfig.OPTION_ADD_STOCK));
        assertTrue(output.contains(StockConfig.OPTION_REDUCE_STOCK));
    }

    // ================== printQuantityToAddPrompt TESTS ==================

    @Test
    void printQuantityToAddPrompt_ShouldDisplayPrompt() {
        // When: Printing quantity to add prompt
        stockView.printQuantityToAddPrompt();

        // Then: Should display prompt
        String output = getOutput();
        assertTrue(output.contains(StockConfig.PROMPT_ENTER_QUANTITY_TO_ADD));
    }

    // ================== printQuantityToReducePrompt TESTS ==================

    @Test
    void printQuantityToReducePrompt_ShouldDisplayPrompt() {
        // When: Printing quantity to reduce prompt
        stockView.printQuantityToReducePrompt();

        // Then: Should display prompt
        String output = getOutput();
        assertTrue(output.contains(StockConfig.PROMPT_ENTER_QUANTITY_TO_REDUCE));
    }

    // ================== printQuantityAddedSuccess TESTS ==================

    @Test
    void printQuantityAddedSuccess_ShouldDisplaySuccessMessage() {
        // When: Printing quantity added success
        stockView.printQuantityAddedSuccess(25);

        // Then: Should display success message and new quantity
        String output = getOutput();
        assertTrue(output.contains(StockConfig.SuccessfulMessage.QUANTITY_ADDED_SUCCESS));
        assertTrue(output.contains("25"));
    }

    // ================== printQuantityReducedSuccess TESTS ==================

    @Test
    void printQuantityReducedSuccess_ShouldDisplaySuccessMessage() {
        // When: Printing quantity reduced success
        stockView.printQuantityReducedSuccess(15);

        // Then: Should display success message and new quantity
        String output = getOutput();
        assertTrue(output.contains(StockConfig.SuccessfulMessage.QUANTITY_REDUCED_SUCCESS));
        assertTrue(output.contains("15"));
    }
}

