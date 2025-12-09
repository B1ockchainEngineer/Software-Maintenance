package assignment.service;

import assignment.model.Stock;
import assignment.repo.MockOrderRepository;
import assignment.repo.MockStockRepository;
import assignment.repo.OrderRepository;
import assignment.repo.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SalesService.
 * Tests Sales functionality with updated stock to ensure integration works correctly.
 * Verifies that SalesService can access and use stock after it's been updated via StockService.
 */
class SalesServiceTest {
    private StockRepository stockRepo;
    private OrderRepository orderRepo;
    private StockService stockService;
    private SalesService salesService;

    @BeforeEach
    void setUp() {
        stockRepo = new MockStockRepository();
        orderRepo = new MockOrderRepository();
        stockService = new StockService(stockRepo);
        salesService = new SalesService(stockRepo, orderRepo);
    }

    // ========== Tests for SalesService with Updated Stock ==========

    @Test
    void findStockItem_ShouldReturnUpdatedStock_AfterQuantityUpdate() {
        // Given: Stock exists and is added
        Stock originalStock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(originalStock);
        
        // When: Stock quantity is updated via StockService
        boolean updated = stockService.updateStock(10001, "TEST PRODUCT", 30, 10.0);
        assertTrue(updated, "Stock should be updated successfully");
        
        // Then: SalesService should find the updated stock with new quantity
        Stock foundStock = salesService.findStockItem(10001);
        assertNotNull(foundStock, "SalesService should find the stock");
        assertEquals(30, foundStock.getQty(), "Quantity should be updated to 30");
        assertEquals(10.0, foundStock.getPrice(), "Price should remain unchanged");
    }

    @Test
    void findStockItem_ShouldReturnUpdatedStock_AfterPriceUpdate() {
        // Given: Stock exists
        Stock originalStock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(originalStock);
        
        // When: Stock price is updated via StockService
        boolean updated = stockService.updateStock(10001, "TEST PRODUCT", 50, 15.5);
        assertTrue(updated, "Stock should be updated successfully");
        
        // Then: SalesService should find the updated stock with new price
        Stock foundStock = salesService.findStockItem(10001);
        assertNotNull(foundStock, "SalesService should find the stock");
        assertEquals(15.5, foundStock.getPrice(), 0.01, "Price should be updated to 15.5");
        assertEquals(50, foundStock.getQty(), "Quantity should remain unchanged");
    }

    @Test
    void findStockItem_ShouldReturnUpdatedStock_AfterNameUpdate() {
        // Given: Stock exists
        Stock originalStock = new Stock(10001, "Old Product Name", 50, 10.0);
        stockService.addNewStock(originalStock);
        
        // When: Stock name is updated via StockService
        boolean updated = stockService.updateStock(10001, "NEW PRODUCT NAME", 50, 10.0);
        assertTrue(updated, "Stock should be updated successfully");
        
        // Then: SalesService should find the stock by ID with updated name
        Stock foundStock = salesService.findStockItem(10001);
        assertNotNull(foundStock, "SalesService should find the stock by ID");
        assertEquals("NEW PRODUCT NAME", foundStock.getStockName(), "Name should be updated");
        assertEquals(10001, foundStock.getStockID(), "ID should remain unchanged");
    }

    @Test
    void getAvailableStock_ShouldReturnUpdatedStock_AfterMultipleUpdates() {
        // Given: Multiple stocks exist
        Stock stock1 = new Stock(10001, "Product 1", 20, 10.0);
        Stock stock2 = new Stock(10002, "Product 2", 30, 15.0);
        stockService.addNewStock(stock1);
        stockService.addNewStock(stock2);
        
        // When: Both stocks are updated
        stockService.updateStock(10001, "PRODUCT 1 UPDATED", 25, 12.0);
        stockService.updateStock(10002, "PRODUCT 2 UPDATED", 35, 18.0);
        
        // Then: SalesService should return updated stocks
        var availableStock = salesService.getAvailableStock();
        assertEquals(2, availableStock.size(), "Should return 2 stocks");
        
        Stock found1 = salesService.findStockItem(10001);
        assertEquals("PRODUCT 1 UPDATED", found1.getStockName());
        assertEquals(25, found1.getQty());
        assertEquals(12.0, found1.getPrice(), 0.01);
        
        Stock found2 = salesService.findStockItem(10002);
        assertEquals("PRODUCT 2 UPDATED", found2.getStockName());
        assertEquals(35, found2.getQty());
        assertEquals(18.0, found2.getPrice(), 0.01);
    }

    @Test
    void addToCart_ShouldUseUpdatedQuantity_AfterStockQuantityUpdate() {
        // Given: Stock with quantity 50
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        
        // When: Stock quantity is updated to 30, then SalesService adds to cart
        stockService.updateStock(10001, "TEST PRODUCT", 30, 10.0);
        boolean added = salesService.addToCart(10001, 5);
        
        // Then: Should succeed and use updated quantity (30)
        assertTrue(added, "Should add to cart successfully");
        assertEquals(1, salesService.getCartItems().size(), "Cart should have 1 item");
        
        // Verify stock quantity was deducted correctly (30 - 5 = 25)
        Stock updatedStock = salesService.findStockItem(10001);
        assertEquals(25, updatedStock.getQty(), "Stock quantity should be 25 after deduction");
    }

    @Test
    void addToCart_ShouldUseUpdatedPrice_AfterStockPriceUpdate() {
        // Given: Stock with price 10.0
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        
        // When: Stock price is updated to 15.5, then SalesService adds to cart
        stockService.updateStock(10001, "TEST PRODUCT", 50, 15.5);
        boolean added = salesService.addToCart(10001, 3);
        
        // Then: Cart item should use updated price
        assertTrue(added, "Should add to cart successfully");
        Stock cartItem = salesService.getCartItems().get(0);
        assertEquals(15.5, cartItem.getPrice(), 0.01, "Cart item should use updated price");
        assertEquals(10001, cartItem.getStockID(), "Stock ID should match");
    }

    @Test
    void addToCart_ShouldFail_WhenUpdatedQuantityIsInsufficient() {
        // Given: Stock with quantity 50
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        
        // When: Stock quantity is updated to 5, then trying to add 10 to cart
        stockService.updateStock(10001, "TEST PRODUCT", 5, 10.0);
        boolean added = salesService.addToCart(10001, 10);
        
        // Then: Should fail due to insufficient stock
        assertFalse(added, "Should fail when quantity is insufficient");
        assertEquals(0, salesService.getCartItems().size(), "Cart should remain empty");
        
        // Verify stock quantity was not changed
        Stock updatedStock = salesService.findStockItem(10001);
        assertEquals(5, updatedStock.getQty(), "Stock quantity should remain 5");
    }

    @Test
    void editOrderQuantity_ShouldWorkWithUpdatedStock() {
        // Given: Stock exists and item is added to cart
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        salesService.addToCart(10001, 5);
        
        // When: Stock quantity is updated, then order quantity is edited
        stockService.updateStock(10001, "TEST PRODUCT", 30, 10.0);
        boolean edited = salesService.editOrderQuantity(1, 2, 2); // Add 2 more
        
        // Then: Should succeed and use updated stock quantity
        assertTrue(edited, "Should edit order successfully");
        Stock cartItem = salesService.getCartItems().get(0);
        assertEquals(7, cartItem.getQty(), "Cart item quantity should be 7 (5 + 2)");
        
        // Verify stock was deducted correctly (30 - 2 = 28, but original 5 was already deducted)
        // Actually, when we added to cart initially, 5 was deducted, leaving 50-5=45
        // Then we updated to 30, so it became 30
        // Then we added 2 more, so it became 30-2=28
        Stock updatedStock = salesService.findStockItem(10001);
        assertEquals(28, updatedStock.getQty(), "Stock quantity should reflect the edit");
    }

    @Test
    void removeOrder_ShouldRefundToUpdatedStock() {
        // Given: Stock exists and item is added to cart
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        salesService.addToCart(10001, 10);
        
        // Verify initial state: stock should be 40 (50 - 10)
        Stock initialStock = salesService.findStockItem(10001);
        assertEquals(40, initialStock.getQty(), "Stock should be 40 after adding to cart");
        
        // When: Stock quantity is updated to 30, then order is removed
        stockService.updateStock(10001, "TEST PRODUCT", 30, 10.0);
        boolean removed = salesService.removeOrder(1);
        
        // Then: Should succeed and refund to updated stock
        assertTrue(removed, "Should remove order successfully");
        assertEquals(0, salesService.getCartItems().size(), "Cart should be empty");
        
        // Verify stock was refunded correctly
        // Original: 50, after addToCart: 40, after updateStock: 30, after removeOrder: 30+10=40
        Stock refundedStock = salesService.findStockItem(10001);
        assertEquals(40, refundedStock.getQty(), "Stock should be refunded to 40");
    }

    @Test
    void findStockItem_ShouldReturnNull_AfterStockIsDeleted() {
        // Given: Stock exists
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        
        // Verify it exists
        assertNotNull(salesService.findStockItem(10001), "Stock should exist initially");
        
        // When: Stock is deleted via StockService
        boolean deleted = stockService.deleteStock(10001);
        assertTrue(deleted, "Stock should be deleted successfully");
        
        // Then: SalesService should not find the stock
        Stock foundStock = salesService.findStockItem(10001);
        assertNull(foundStock, "SalesService should not find deleted stock");
    }

    @Test
    void getAvailableStock_ShouldExcludeDeletedStock() {
        // Given: Multiple stocks exist
        Stock stock1 = new Stock(10001, "Product 1", 20, 10.0);
        Stock stock2 = new Stock(10002, "Product 2", 30, 15.0);
        stockService.addNewStock(stock1);
        stockService.addNewStock(stock2);
        
        // Verify both exist
        assertEquals(2, salesService.getAvailableStock().size(), "Should have 2 stocks");
        
        // When: One stock is deleted
        stockService.deleteStock(10001);
        
        // Then: SalesService should only return remaining stock
        var availableStock = salesService.getAvailableStock();
        assertEquals(1, availableStock.size(), "Should have 1 stock remaining");
        assertEquals(10002, availableStock.get(0).getStockID(), "Remaining stock should be Product 2");
    }

    @Test
    void addToCart_ShouldFail_AfterStockIsDeleted() {
        // Given: Stock exists and is added to cart
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        salesService.addToCart(10001, 5);
        
        // When: Stock is deleted
        stockService.deleteStock(10001);
        
        // Then: Adding more of the same stock should fail
        boolean added = salesService.addToCart(10001, 3);
        assertFalse(added, "Should fail to add deleted stock to cart");
        
        // Cart should still have the original item (deletion doesn't affect existing cart items)
        assertEquals(1, salesService.getCartItems().size(), "Cart should still have original item");
    }

    // ========== Tests for Stock Deduction/Refund When Orders Are Created/Modified/Removed ==========

    @Test
    void addToCart_ShouldDeductStockQuantity_WhenOrderCreated() {
        // Given: Stock with initial quantity 50
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        
        // Verify initial stock quantity
        Stock initialStock = salesService.findStockItem(10001);
        assertEquals(50, initialStock.getQty(), "Initial stock quantity should be 50");
        
        // When: Creating an order for 10 units
        boolean added = salesService.addToCart(10001, 10);
        
        // Then: Order should be created and stock should be deducted
        assertTrue(added, "Order should be created successfully");
        assertEquals(1, salesService.getCartItems().size(), "Cart should have 1 item");
        
        // Verify stock quantity was deducted (50 - 10 = 40)
        Stock updatedStock = salesService.findStockItem(10001);
        assertEquals(40, updatedStock.getQty(), "Stock quantity should be deducted to 40");
        
        // Verify cart item has correct quantity
        Stock cartItem = salesService.getCartItems().get(0);
        assertEquals(10, cartItem.getQty(), "Cart item quantity should be 10");
        assertEquals(10001, cartItem.getStockID(), "Cart item stock ID should match");
    }

    @Test
    void addToCart_ShouldDeductStock_WhenMultipleOrdersCreated() {
        // Given: Stock with initial quantity 100
        Stock stock = new Stock(10001, "Test Product", 100, 10.0);
        stockService.addNewStock(stock);
        
        // When: Creating multiple orders
        salesService.addToCart(10001, 20);  // First order: 20 units
        salesService.addToCart(10001, 15);  // Second order: 15 units
        salesService.addToCart(10001, 10);  // Third order: 10 units
        
        // Then: All orders should be created and stock should be deducted correctly
        assertEquals(3, salesService.getCartItems().size(), "Cart should have 3 items");
        
        // Verify total stock deduction: 100 - 20 - 15 - 10 = 55
        Stock updatedStock = salesService.findStockItem(10001);
        assertEquals(55, updatedStock.getQty(), "Stock quantity should be 55 after 3 orders");
    }

    @Test
    void addToCart_ShouldNotDeductStock_WhenOrderCreationFails() {
        // Given: Stock with quantity 5
        Stock stock = new Stock(10001, "Test Product", 5, 10.0);
        stockService.addNewStock(stock);
        
        // When: Trying to create order for 10 units (more than available)
        boolean added = salesService.addToCart(10001, 10);
        
        // Then: Order should fail and stock should NOT be deducted
        assertFalse(added, "Order creation should fail due to insufficient stock");
        assertEquals(0, salesService.getCartItems().size(), "Cart should be empty");
        
        // Verify stock quantity was NOT changed
        Stock unchangedStock = salesService.findStockItem(10001);
        assertEquals(5, unchangedStock.getQty(), "Stock quantity should remain 5");
    }

    @Test
    void removeOrder_ShouldRefundStockQuantity_WhenOrderRemoved() {
        // Given: Stock with initial quantity 50, order created for 15 units
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        salesService.addToCart(10001, 15);
        
        // Verify stock was deducted (50 - 15 = 35)
        Stock afterOrder = salesService.findStockItem(10001);
        assertEquals(35, afterOrder.getQty(), "Stock should be 35 after order creation");
        
        // When: Removing the order
        boolean removed = salesService.removeOrder(1);
        
        // Then: Order should be removed and stock should be refunded
        assertTrue(removed, "Order should be removed successfully");
        assertEquals(0, salesService.getCartItems().size(), "Cart should be empty");
        
        // Verify stock quantity was refunded (35 + 15 = 50)
        Stock refundedStock = salesService.findStockItem(10001);
        assertEquals(50, refundedStock.getQty(), "Stock quantity should be refunded to 50");
    }

    @Test
    void editOrderQuantity_ShouldDeductStock_WhenQuantityIncreased() {
        // Given: Stock with quantity 50, order created for 10 units
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        salesService.addToCart(10001, 10);
        
        // Verify initial state: stock should be 40 (50 - 10)
        Stock initialStock = salesService.findStockItem(10001);
        assertEquals(40, initialStock.getQty(), "Stock should be 40 after initial order");
        
        // When: Increasing order quantity by 5 (type 2 = Add)
        boolean edited = salesService.editOrderQuantity(1, 5, 2);
        
        // Then: Order quantity should increase and stock should be deducted
        assertTrue(edited, "Order edit should succeed");
        Stock cartItem = salesService.getCartItems().get(0);
        assertEquals(15, cartItem.getQty(), "Cart item quantity should be 15 (10 + 5)");
        
        // Verify stock was deducted: 40 - 5 = 35
        Stock updatedStock = salesService.findStockItem(10001);
        assertEquals(35, updatedStock.getQty(), "Stock quantity should be 35 after increasing order");
    }

    @Test
    void editOrderQuantity_ShouldRefundStock_WhenQuantityDecreased() {
        // Given: Stock with quantity 50, order created for 20 units
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        salesService.addToCart(10001, 20);
        
        // Verify initial state: stock should be 30 (50 - 20)
        Stock initialStock = salesService.findStockItem(10001);
        assertEquals(30, initialStock.getQty(), "Stock should be 30 after initial order");
        
        // When: Decreasing order quantity by 8 (type 1 = Reduce)
        boolean edited = salesService.editOrderQuantity(1, 8, 1);
        
        // Then: Order quantity should decrease and stock should be refunded
        assertTrue(edited, "Order edit should succeed");
        Stock cartItem = salesService.getCartItems().get(0);
        assertEquals(12, cartItem.getQty(), "Cart item quantity should be 12 (20 - 8)");
        
        // Verify stock was refunded: 30 + 8 = 38
        Stock updatedStock = salesService.findStockItem(10001);
        assertEquals(38, updatedStock.getQty(), "Stock quantity should be 38 after decreasing order");
    }

    @Test
    void editOrderQuantity_ShouldNotChangeStock_WhenIncreaseFailsDueToInsufficientStock() {
        // Given: Stock with quantity 50, order created for 10 units
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        salesService.addToCart(10001, 10);
        
        // Stock is now 40 (50 - 10)
        Stock currentStock = salesService.findStockItem(10001);
        assertEquals(40, currentStock.getQty(), "Stock should be 40");
        
        // When: Trying to increase order by 50 (more than available stock)
        boolean edited = salesService.editOrderQuantity(1, 50, 2);
        
        // Then: Edit should fail and stock should NOT change
        assertFalse(edited, "Order edit should fail due to insufficient stock");
        Stock cartItem = salesService.getCartItems().get(0);
        assertEquals(10, cartItem.getQty(), "Cart item quantity should remain 10");
        
        // Verify stock quantity was NOT changed
        Stock unchangedStock = salesService.findStockItem(10001);
        assertEquals(40, unchangedStock.getQty(), "Stock quantity should remain 40");
    }

    @Test
    void editOrderQuantity_ShouldNotChangeStock_WhenDecreaseFailsDueToInvalidAmount() {
        // Given: Stock with quantity 50, order created for 10 units
        Stock stock = new Stock(10001, "Test Product", 50, 10.0);
        stockService.addNewStock(stock);
        salesService.addToCart(10001, 10);
        
        // Stock is now 40 (50 - 10)
        Stock currentStock = salesService.findStockItem(10001);
        assertEquals(40, currentStock.getQty(), "Stock should be 40");
        
        // When: Trying to decrease order by 15 (more than ordered)
        boolean edited = salesService.editOrderQuantity(1, 15, 1);
        
        // Then: Edit should fail and stock should NOT change
        assertFalse(edited, "Order edit should fail - cannot reduce more than ordered");
        Stock cartItem = salesService.getCartItems().get(0);
        assertEquals(10, cartItem.getQty(), "Cart item quantity should remain 10");
        
        // Verify stock quantity was NOT changed
        Stock unchangedStock = salesService.findStockItem(10001);
        assertEquals(40, unchangedStock.getQty(), "Stock quantity should remain 40");
    }

    @Test
    void stockDeduction_ShouldWorkCorrectly_WithMultipleProducts() {
        // Given: Multiple products with different quantities
        Stock product1 = new Stock(10001, "Product 1", 100, 10.0);
        Stock product2 = new Stock(10002, "Product 2", 50, 15.0);
        Stock product3 = new Stock(10003, "Product 3", 75, 20.0);
        stockService.addNewStock(product1);
        stockService.addNewStock(product2);
        stockService.addNewStock(product3);
        
        // When: Creating orders for different products
        salesService.addToCart(10001, 30);  // Product 1: 100 - 30 = 70
        salesService.addToCart(10002, 20);  // Product 2: 50 - 20 = 30
        salesService.addToCart(10003, 25);  // Product 3: 75 - 25 = 50
        
        // Then: All orders should be created and each product's stock should be deducted independently
        assertEquals(3, salesService.getCartItems().size(), "Cart should have 3 items");
        
        Stock updated1 = salesService.findStockItem(10001);
        assertEquals(70, updated1.getQty(), "Product 1 should have 70 remaining");
        
        Stock updated2 = salesService.findStockItem(10002);
        assertEquals(30, updated2.getQty(), "Product 2 should have 30 remaining");
        
        Stock updated3 = salesService.findStockItem(10003);
        assertEquals(50, updated3.getQty(), "Product 3 should have 50 remaining");
    }
}

