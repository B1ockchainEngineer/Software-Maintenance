package assignment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Transaction model class.
 * Tests constructor, getters, and immutability.
 */
@DisplayName("Transaction Model Tests")
class TransactionModelTest {

    private Transaction transaction;
    private List<Stock> items;

    @BeforeEach
    void setUp() {
        items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", 2, 50.0));
        items.add(new Stock(1002, "Product B", 1, 100.0));
        transaction = new Transaction(200.0, 20.0, 10.8, 190.8, items);
    }

    // ================== CONSTRUCTOR TESTS ==================

    @Test
    @DisplayName("Constructor should initialize all fields correctly")
    void constructor_ShouldInitializeAllFieldsCorrectly() {
        // Given: Transaction with all parameters
        List<Stock> testItems = new ArrayList<>();
        testItems.add(new Stock(1003, "Test Product", 5, 10.0));
        Transaction t = new Transaction(50.0, 5.0, 2.7, 47.7, testItems);

        // Then: All fields should be set correctly
        assertEquals(50.0, t.getSubtotal(), 0.01);
        assertEquals(5.0, t.getDiscount(), 0.01);
        assertEquals(2.7, t.getTax(), 0.01);
        assertEquals(47.7, t.getTotal(), 0.01);
        assertEquals(1, t.getItems().size());
        assertEquals(1003, t.getItems().get(0).getStockID());
    }

    @Test
    @DisplayName("Constructor should create defensive copy of items list")
    void constructor_ShouldCreateDefensiveCopyOfItems() {
        // Given: Transaction with items
        List<Stock> originalItems = new ArrayList<>();
        originalItems.add(new Stock(1004, "Original", 1, 10.0));
        Transaction t = new Transaction(10.0, 0.0, 0.6, 10.6, originalItems);

        // When: Modifying original list
        originalItems.add(new Stock(1005, "New Item", 1, 20.0));

        // Then: Transaction items should not be affected
        assertEquals(1, t.getItems().size());
        assertEquals(1004, t.getItems().get(0).getStockID());
    }

    @Test
    @DisplayName("Constructor with empty items list should work")
    void constructor_WithEmptyItemsList_ShouldWork() {
        // Given: Transaction with empty items
        List<Stock> emptyItems = new ArrayList<>();
        Transaction t = new Transaction(0.0, 0.0, 0.0, 0.0, emptyItems);

        // Then: Should initialize correctly
        assertEquals(0.0, t.getSubtotal(), 0.01);
        assertEquals(0.0, t.getDiscount(), 0.01);
        assertEquals(0.0, t.getTax(), 0.01);
        assertEquals(0.0, t.getTotal(), 0.01);
        assertTrue(t.getItems().isEmpty());
    }

    @Test
    @DisplayName("Constructor with multiple items should store all items")
    void constructor_WithMultipleItems_ShouldStoreAllItems() {
        // Given: Transaction with multiple items
        List<Stock> multipleItems = new ArrayList<>();
        multipleItems.add(new Stock(1006, "Item 1", 2, 10.0));
        multipleItems.add(new Stock(1007, "Item 2", 3, 20.0));
        multipleItems.add(new Stock(1008, "Item 3", 1, 30.0));
        Transaction t = new Transaction(110.0, 11.0, 5.94, 104.94, multipleItems);

        // Then: Should contain all items
        assertEquals(3, t.getItems().size());
        assertEquals(1006, t.getItems().get(0).getStockID());
        assertEquals(1007, t.getItems().get(1).getStockID());
        assertEquals(1008, t.getItems().get(2).getStockID());
    }

    // ================== GETTER TESTS ==================

    @Test
    @DisplayName("getSubtotal should return subtotal")
    void getSubtotal_ShouldReturnSubtotal() {
        assertEquals(200.0, transaction.getSubtotal(), 0.01);
    }

    @Test
    @DisplayName("getDiscount should return discount")
    void getDiscount_ShouldReturnDiscount() {
        assertEquals(20.0, transaction.getDiscount(), 0.01);
    }

    @Test
    @DisplayName("getTax should return tax")
    void getTax_ShouldReturnTax() {
        assertEquals(10.8, transaction.getTax(), 0.01);
    }

    @Test
    @DisplayName("getTotal should return total")
    void getTotal_ShouldReturnTotal() {
        assertEquals(190.8, transaction.getTotal(), 0.01);
    }

    @Test
    @DisplayName("getItems should return defensive copy")
    void getItems_ShouldReturnDefensiveCopy() {
        // Given: Transaction with items
        List<Stock> items = transaction.getItems();

        // When: Modifying returned list
        items.add(new Stock(1009, "New Item", 1, 10.0));

        // Then: Original transaction items should not be affected
        assertEquals(2, transaction.getItems().size());
        assertNotSame(items, transaction.getItems());
    }

    @Test
    @DisplayName("getItems should return list with same content")
    void getItems_ShouldReturnListWithSameContent() {
        // Given: Transaction with items
        List<Stock> items = transaction.getItems();

        // Then: Should have same size and content
        assertEquals(2, items.size());
        assertEquals(1001, items.get(0).getStockID());
        assertEquals(1002, items.get(1).getStockID());
    }

    // ================== IMMUTABILITY TESTS ==================

    @Test
    @DisplayName("Transaction should be immutable (fields are final)")
    void transaction_ShouldBeImmutable() {
        // Given: Transaction object
        // Note: We can't directly test final fields, but we can verify
        // that getters return consistent values
        double initialSubtotal = transaction.getSubtotal();
        double initialDiscount = transaction.getDiscount();
        double initialTax = transaction.getTax();
        double initialTotal = transaction.getTotal();
        int initialItemCount = transaction.getItems().size();

        // When: Multiple calls to getters
        // Then: Values should remain the same
        assertEquals(initialSubtotal, transaction.getSubtotal(), 0.01);
        assertEquals(initialDiscount, transaction.getDiscount(), 0.01);
        assertEquals(initialTax, transaction.getTax(), 0.01);
        assertEquals(initialTotal, transaction.getTotal(), 0.01);
        assertEquals(initialItemCount, transaction.getItems().size());
    }

    // ================== EDGE CASE TESTS ==================

    @Test
    @DisplayName("Transaction with zero values should work")
    void transaction_WithZeroValues_ShouldWork() {
        List<Stock> emptyItems = new ArrayList<>();
        Transaction t = new Transaction(0.0, 0.0, 0.0, 0.0, emptyItems);

        assertEquals(0.0, t.getSubtotal(), 0.01);
        assertEquals(0.0, t.getDiscount(), 0.01);
        assertEquals(0.0, t.getTax(), 0.01);
        assertEquals(0.0, t.getTotal(), 0.01);
        assertTrue(t.getItems().isEmpty());
    }

    @Test
    @DisplayName("Transaction with negative discount should work")
    void transaction_WithNegativeDiscount_ShouldWork() {
        // Note: In real scenarios, discount might be negative (surcharge)
        List<Stock> testItems = new ArrayList<>();
        testItems.add(new Stock(1010, "Test", 1, 100.0));
        Transaction t = new Transaction(100.0, -10.0, 5.4, 115.4, testItems);

        assertEquals(100.0, t.getSubtotal(), 0.01);
        assertEquals(-10.0, t.getDiscount(), 0.01);
        assertEquals(5.4, t.getTax(), 0.01);
        assertEquals(115.4, t.getTotal(), 0.01);
    }

    @Test
    @DisplayName("Transaction with large values should work")
    void transaction_WithLargeValues_ShouldWork() {
        List<Stock> testItems = new ArrayList<>();
        testItems.add(new Stock(1011, "Expensive", 1, 1000000.0));
        Transaction t = new Transaction(1000000.0, 100000.0, 54000.0, 954000.0, testItems);

        assertEquals(1000000.0, t.getSubtotal(), 0.01);
        assertEquals(100000.0, t.getDiscount(), 0.01);
        assertEquals(54000.0, t.getTax(), 0.01);
        assertEquals(954000.0, t.getTotal(), 0.01);
    }

    // ================== INTEGRATION TESTS ==================

    @Test
    @DisplayName("Should handle complete transaction with multiple items")
    void shouldHandleCompleteTransactionWithMultipleItems() {
        // Given: Multiple items
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1012, "Product 1", 2, 25.0));
        items.add(new Stock(1013, "Product 2", 3, 30.0));
        items.add(new Stock(1014, "Product 3", 1, 40.0));

        // When: Creating transaction
        double subtotal = 2 * 25.0 + 3 * 30.0 + 1 * 40.0; // 50 + 90 + 40 = 180
        double discount = subtotal * 0.10; // 18
        double amountAfterDiscount = subtotal - discount; // 162
        double tax = amountAfterDiscount * 0.06; // 9.72
        double total = amountAfterDiscount + tax; // 171.72

        Transaction t = new Transaction(subtotal, discount, tax, total, items);

        // Then: Should have correct values
        assertEquals(180.0, t.getSubtotal(), 0.01);
        assertEquals(18.0, t.getDiscount(), 0.01);
        assertEquals(9.72, t.getTax(), 0.01);
        assertEquals(171.72, t.getTotal(), 0.01);
        assertEquals(3, t.getItems().size());
    }
}
