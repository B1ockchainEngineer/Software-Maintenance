package assignment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Order model class.
 * Tests constructors, getters, setters, and utility methods.
 */
@DisplayName("Order Model Tests")
class OrderModelTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(1, 1001, "Test Product", 5, 25.50);
    }

    // ================== CONSTRUCTOR TESTS ==================

    @Test
    @DisplayName("Constructor with all parameters should initialize correctly")
    void constructor_WithAllParameters_ShouldInitializeCorrectly() {
        // Given: Order with all parameters
        Order o = new Order(2, 1002, "Apple", 10, 3.50);

        // Then: All fields should be set correctly
        assertEquals(2, o.getOrderNo());
        assertEquals(1002, o.getStockID());
        assertEquals("APPLE", o.getStockName()); // Name is uppercased
        assertEquals(10, o.getQuantity());
        assertEquals(3.50, o.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Constructor without orderNo should set orderNo to 0")
    void constructor_WithoutOrderNo_ShouldSetOrderNoToZero() {
        // Given: Order without orderNo
        Order o = new Order(1003, "Banana", 8, 2.75);

        // Then: orderNo should be 0, other fields set correctly
        assertEquals(0, o.getOrderNo());
        assertEquals(1003, o.getStockID());
        assertEquals("BANANA", o.getStockName());
        assertEquals(8, o.getQuantity());
        assertEquals(2.75, o.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Default constructor should initialize with defaults")
    void constructor_NoParameters_ShouldInitializeWithDefaults() {
        // Given: Default constructor
        Order o = new Order();

        // Then: All fields should be default values
        assertEquals(0, o.getOrderNo());
        assertEquals(0, o.getStockID());
        assertEquals("", o.getStockName());
        assertEquals(0, o.getQuantity());
        assertEquals(0.0, o.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Constructor with null name should handle gracefully")
    void constructor_WithNullName_ShouldHandleGracefully() {
        // Given: Order with null name
        Order o = new Order(1004, null, 10, 5.0);

        // Then: Should return empty string for name
        assertEquals("", o.getStockName());
    }

    // ================== GETTER TESTS ==================

    @Test
    @DisplayName("getOrderNo should return order number")
    void getOrderNo_ShouldReturnOrderNo() {
        assertEquals(1, order.getOrderNo());
    }

    @Test
    @DisplayName("getStockID should return stock ID")
    void getStockID_ShouldReturnStockId() {
        assertEquals(1001, order.getStockID());
    }

    @Test
    @DisplayName("getStockName should return uppercase name")
    void getStockName_ShouldReturnUppercaseName() {
        assertEquals("TEST PRODUCT", order.getStockName());
    }

    @Test
    @DisplayName("getStockName with mixed case should return uppercase")
    void getStockName_WithMixedCase_ShouldReturnUppercase() {
        Order o = new Order(1005, "MiXeD cAsE", 5, 10.0);
        assertEquals("MIXED CASE", o.getStockName());
    }

    @Test
    @DisplayName("getStockName with null should return empty string")
    void getStockName_WithNull_ShouldReturnEmptyString() {
        Order o = new Order();
        o.setStockName(null);
        assertEquals("", o.getStockName());
    }

    @Test
    @DisplayName("getQuantity should return quantity")
    void getQuantity_ShouldReturnQuantity() {
        assertEquals(5, order.getQuantity());
    }

    @Test
    @DisplayName("getPrice should return price")
    void getPrice_ShouldReturnPrice() {
        assertEquals(25.50, order.getPrice(), 0.01);
    }

    // ================== SETTER TESTS ==================

    @Test
    @DisplayName("setOrderNo should update order number")
    void setOrderNo_ShouldUpdateOrderNo() {
        order.setOrderNo(10);
        assertEquals(10, order.getOrderNo());
    }

    @Test
    @DisplayName("setStockID should update stock ID")
    void setStockID_ShouldUpdateStockId() {
        order.setStockID(2001);
        assertEquals(2001, order.getStockID());
    }

    @Test
    @DisplayName("setStockName should update stock name")
    void setStockName_ShouldUpdateStockName() {
        order.setStockName("New Product");
        assertEquals("NEW PRODUCT", order.getStockName());
    }

    @Test
    @DisplayName("setQuantity should update quantity")
    void setQuantity_ShouldUpdateQuantity() {
        order.setQuantity(20);
        assertEquals(20, order.getQuantity());
    }

    @Test
    @DisplayName("setPrice should update price")
    void setPrice_ShouldUpdatePrice() {
        order.setPrice(99.99);
        assertEquals(99.99, order.getPrice(), 0.01);
    }

    // ================== UTILITY METHOD TESTS ==================

    @Test
    @DisplayName("calculateTotalCost should return quantity times price")
    void calculateTotalCost_ShouldReturnQtyTimesPrice() {
        // Given: Order with qty=5, price=25.50
        double expected = 5 * 25.50;

        // When: Calculating total cost
        double result = order.calculateTotalCost();

        // Then: Should return correct total
        assertEquals(expected, result, 0.01);
    }

    @Test
    @DisplayName("calculateTotalCost with zero quantity should return zero")
    void calculateTotalCost_WithZeroQuantity_ShouldReturnZero() {
        order.setQuantity(0);
        assertEquals(0.0, order.calculateTotalCost(), 0.01);
    }

    @Test
    @DisplayName("calculateTotalCost with zero price should return zero")
    void calculateTotalCost_WithZeroPrice_ShouldReturnZero() {
        order.setPrice(0.0);
        assertEquals(0.0, order.calculateTotalCost(), 0.01);
    }

    @Test
    @DisplayName("toFileString should format correctly")
    void toFileString_ShouldFormatCorrectly() {
        // Given: Order with known values
        Order o = new Order(1006, "Test Item", 15, 12.50);

        // When: Converting to file string
        String result = o.toFileString();

        // Then: Should match expected format: "stockID\tstockName\tquantity\tprice"
        assertEquals("1006\tTEST ITEM\t15\t12.5", result);
    }

    @Test
    @DisplayName("toFileString with zero values should format correctly")
    void toFileString_WithZeroValues_ShouldFormatCorrectly() {
        Order o = new Order();
        String result = o.toFileString();
        assertEquals("0\t\t0\t0.0", result);
    }

    @Test
    @DisplayName("toString should format for console display")
    void toString_ShouldFormatForConsoleDisplay() {
        // Given: Order with known values
        Order o = new Order(1007, "Display Test", 20, 8.75);

        // When: Converting to string
        String result = o.toString();

        // Then: Should contain all order information
        assertTrue(result.contains("PRODUCT ID >> 1007"));
        assertTrue(result.contains("PRODUCT NAME >> DISPLAY TEST"));
        assertTrue(result.contains("QUANTITY >> 20"));
        assertTrue(result.contains("PRODUCT PRICE >> RM8.75"));
    }

    @Test
    @DisplayName("toString should format price with two decimals")
    void toString_ShouldFormatPriceWithTwoDecimals() {
        Order o = new Order(1008, "Price Test", 5, 9.9);
        String result = o.toString();
        assertTrue(result.contains("RM9.90")); // Should format to 2 decimals
    }

    @Test
    @DisplayName("toString with zero price should display zero")
    void toString_WithZeroPrice_ShouldDisplayZero() {
        Order o = new Order(1009, "Zero Price", 10, 0.0);
        String result = o.toString();
        assertTrue(result.contains("RM0.00"));
    }

    // ================== INTEGRATION TESTS ==================

    @Test
    @DisplayName("Should handle complete order lifecycle")
    void shouldHandleCompleteOrderLifecycle() {
        // Create order
        Order o = new Order();
        o.setOrderNo(1);
        o.setStockID(2001);
        o.setStockName("Lifecycle Test");
        o.setQuantity(10);
        o.setPrice(50.00);

        // Verify initial state
        assertEquals(1, o.getOrderNo());
        assertEquals(2001, o.getStockID());
        assertEquals("LIFECYCLE TEST", o.getStockName());
        assertEquals(10, o.getQuantity());
        assertEquals(50.00, o.getPrice(), 0.01);
        assertEquals(500.00, o.calculateTotalCost(), 0.01);

        // Update order
        o.setQuantity(15);
        o.setPrice(60.00);

        // Verify updates
        assertEquals(15, o.getQuantity());
        assertEquals(60.00, o.getPrice(), 0.01);
        assertEquals(900.00, o.calculateTotalCost(), 0.01);
    }
}
