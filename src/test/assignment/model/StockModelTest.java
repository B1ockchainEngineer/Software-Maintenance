package assignment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Stock model class.
 * Tests constructors, getters, setters, and utility methods.
 */
class StockModelTest {

    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock(10001, "Test Product", 10, 25.50);
    }

    // ================== CONSTRUCTOR TESTS ==================

    @Test
    void constructor_WithFourParameters_ShouldInitializeCorrectly() {
        // Given: Stock with 4 parameters
        Stock s = new Stock(10002, "Apple", 5, 3.50);

        // Then: All fields should be set correctly
        assertEquals(10002, s.getStockID());
        assertEquals("APPLE", s.getStockName()); // Name is uppercased
        assertEquals(5, s.getQty());
        assertEquals(3.50, s.getPrice(), 0.01);
        assertEquals(0, s.getOrderNo()); // Default orderNo
    }

    @Test
    void constructor_WithFiveParameters_ShouldInitializeWithOrderNo() {
        // Given: Stock with 5 parameters (including orderNo)
        Stock s = new Stock(2001, 10003, "Banana", 8, 2.75);

        // Then: All fields including orderNo should be set
        assertEquals(2001, s.getOrderNo());
        assertEquals(10003, s.getStockID());
        assertEquals("BANANA", s.getStockName());
        assertEquals(8, s.getQty());
        assertEquals(2.75, s.getPrice(), 0.01);
    }

    @Test
    void constructor_NoParameters_ShouldInitializeWithDefaults() {
        // Given: Default constructor
        Stock s = new Stock();

        // Then: All fields should be default values
        assertEquals(0, s.getStockID());
        assertEquals("", s.getStockName());
        assertEquals(0, s.getQty());
        assertEquals(0.0, s.getPrice(), 0.01);
        assertEquals(0, s.getOrderNo());
    }

    @Test
    void constructor_WithNullName_ShouldHandleGracefully() {
        // Given: Stock with null name
        Stock s = new Stock(10004, null, 10, 5.0);

        // Then: Should return empty string for name
        assertEquals("", s.getStockName());
    }

    // ================== GETTER TESTS ==================

    @Test
    void getStockID_ShouldReturnCorrectID() {
        assertEquals(10001, stock.getStockID());
    }

    @Test
    void getStockName_ShouldReturnUppercaseName() {
        assertEquals("TEST PRODUCT", stock.getStockName());
    }

    @Test
    void getStockName_WithMixedCase_ShouldReturnUppercase() {
        Stock s = new Stock(10005, "MiXeD cAsE", 5, 10.0);
        assertEquals("MIXED CASE", s.getStockName());
    }

    @Test
    void getQty_ShouldReturnCorrectQuantity() {
        assertEquals(10, stock.getQty());
    }

    @Test
    void getPrice_ShouldReturnCorrectPrice() {
        assertEquals(25.50, stock.getPrice(), 0.01);
    }

    @Test
    void getOrderNo_ShouldReturnCorrectOrderNo() {
        Stock s = new Stock(3001, 10006, "Item", 5, 10.0);
        assertEquals(3001, s.getOrderNo());
    }

    // ================== SETTER TESTS ==================

    @Test
    void setStockID_ShouldUpdateID() {
        stock.setStockID(20001);
        assertEquals(20001, stock.getStockID());
    }

    @Test
    void setStockName_ShouldUpdateName() {
        stock.setStockName("New Name");
        assertEquals("NEW NAME", stock.getStockName());
    }

    @Test
    void setQty_ShouldUpdateQuantity() {
        stock.setQty(50);
        assertEquals(50, stock.getQty());
    }

    @Test
    void setPrice_ShouldUpdatePrice() {
        stock.setPrice(99.99);
        assertEquals(99.99, stock.getPrice(), 0.01);
    }

    @Test
    void setOrderNo_ShouldUpdateOrderNo() {
        stock.setOrderNo(5001);
        assertEquals(5001, stock.getOrderNo());
    }

    // ================== UTILITY METHOD TESTS ==================

    @Test
    void calculateTotalCost_ShouldReturnQtyTimesPrice() {
        // Given: Stock with qty=10, price=25.50
        double expected = 10 * 25.50;

        // When: Calculating total cost
        double result = stock.calculateTotalCost();

        // Then: Should return correct total
        assertEquals(expected, result, 0.01);
    }

    @Test
    void calculateTotalCost_WithZeroQty_ShouldReturnZero() {
        stock.setQty(0);
        assertEquals(0.0, stock.calculateTotalCost(), 0.01);
    }

    @Test
    void calculateTotalCost_WithZeroPrice_ShouldReturnZero() {
        stock.setPrice(0.0);
        assertEquals(0.0, stock.calculateTotalCost(), 0.01);
    }

    @Test
    void toFileString_ShouldFormatCorrectly() {
        // Given: Stock with known values
        Stock s = new Stock(10007, "Test Item", 15, 12.50);

        // When: Converting to file string
        String result = s.toFileString();

        // Then: Should match expected format: "ID\tNAME\tQTY\tPRICE"
        assertEquals("10007\tTEST ITEM\t15\t12.5", result);
    }

    @Test
    void toFileString_WithZeroValues_ShouldFormatCorrectly() {
        Stock s = new Stock();
        String result = s.toFileString();
        assertEquals("0\t\t0\t0.0", result);
    }

    @Test
    void toString_ShouldFormatForConsoleDisplay() {
        // Given: Stock with known values
        Stock s = new Stock(10008, "Display Test", 20, 8.75);

        // When: Converting to string
        String result = s.toString();

        // Then: Should contain all product information
        assertTrue(result.contains("PRODUCT ID >> 10008"));
        assertTrue(result.contains("PRODUCT NAME >> DISPLAY TEST"));
        assertTrue(result.contains("QUANTITY >> 20"));
        assertTrue(result.contains("PRODUCT PRICE >> RM8.75"));
    }

    @Test
    void toString_ShouldFormatPriceWithTwoDecimals() {
        Stock s = new Stock(10009, "Price Test", 5, 9.9);
        String result = s.toString();
        assertTrue(result.contains("RM9.90")); // Should format to 2 decimals
    }

    @Test
    void toString_WithZeroPrice_ShouldDisplayZero() {
        Stock s = new Stock(10010, "Zero Price", 10, 0.0);
        String result = s.toString();
        assertTrue(result.contains("RM0.00"));
    }
}

