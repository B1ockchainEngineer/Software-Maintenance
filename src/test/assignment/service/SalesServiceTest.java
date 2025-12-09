package test.assignment.service;

import assignment.model.Stock;
import assignment.repo.OrderRepository;
import assignment.repo.StockRepository;
import assignment.service.SalesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SalesService.
 * Tests cart operations: add, remove, edit, and search functionality.
 */
@DisplayName("SalesService Tests")
class SalesServiceTest {

    private StockRepository stockRepo;
    private OrderRepository orderRepo;
    private SalesService salesService;

    @BeforeEach
    void setUp() {
        // Create mock repositories with in-memory data
        stockRepo = new StockRepository() {
            private final List<Stock> stocklist = new ArrayList<>();
            private final List<Stock> cart = new ArrayList<>();

            @Override
            public List<Stock> getStocklist() {
                return stocklist;
            }

            @Override
            public List<Stock> getCart() {
                return cart;
            }

            @Override
            public void clearCart() {
                cart.clear();
            }

            @Override
            public List<Stock> loadStockFromFile() {
                // Initialize with test data
                if (stocklist.isEmpty()) {
                    stocklist.add(new Stock(1001, "Product A", 10, 50.0));
                    stocklist.add(new Stock(1002, "Product B", 5, 100.0));
                    stocklist.add(new Stock(1003, "Product C", 0, 25.0)); // Out of stock
                }
                return stocklist;
            }

            @Override
            public void saveStockToFile() {
                // Mock - do nothing
            }
        };

        orderRepo = new OrderRepository() {
            @Override
            public void appendOrder(Stock order) {
                // Mock - do nothing
            }

            @Override
            public boolean deleteOrder(int orderNo) {
                // Mock - return success
                return true;
            }

            @Override
            public boolean updateOrder(Stock order) {
                // Mock - return success
                return true;
            }

            @Override
            public List<Stock> loadAllOrders() {
                return new ArrayList<>(); // Empty initially
            }

            @Override
            public void clearAllOrders() {
                // Mock - do nothing
            }
        };

        salesService = new SalesService(stockRepo, orderRepo);
    }

    @Test
    @DisplayName("Should return available stock items")
    void testGetAvailableStock() {
        List<Stock> availableStock = salesService.getAvailableStock();
        assertNotNull(availableStock);
        assertEquals(3, availableStock.size());
    }

    @Test
    @DisplayName("Should return empty cart initially")
    void testGetCartItems_InitiallyEmpty() {
        List<Stock> cartItems = salesService.getCartItems();
        assertNotNull(cartItems);
        assertTrue(cartItems.isEmpty());
    }

    @Test
    @DisplayName("Should find stock item by ID")
    void testFindStockItem_Exists() {
        Stock found = salesService.findStockItem(1001);
        assertNotNull(found);
        assertEquals(1001, found.getStockID());
        assertEquals("PRODUCT A", found.getStockName()); // getStockName() returns uppercase
    }

    @Test
    @DisplayName("Should return null when stock item not found")
    void testFindStockItem_NotFound() {
        Stock found = salesService.findStockItem(9999);
        assertNull(found);
    }

    @Test
    @DisplayName("Should successfully add item to cart")
    void testAddToCart_Success() {
        boolean result = salesService.addToCart(1001, 3);
        assertTrue(result);

        List<Stock> cart = salesService.getCartItems();
        assertEquals(1, cart.size());
        assertEquals(1001, cart.get(0).getStockID());
        assertEquals(3, cart.get(0).getQty());
        assertEquals(1, cart.get(0).getOrderNo()); // First order should be #1

        // Verify stock was deducted
        Stock stockItem = salesService.findStockItem(1001);
        assertEquals(7, stockItem.getQty()); // 10 - 3 = 7
    }

    @Test
    @DisplayName("Should fail to add item when stock insufficient")
    void testAddToCart_InsufficientStock() {
        boolean result = salesService.addToCart(1001, 15); // More than available (10)
        assertFalse(result);
        assertTrue(salesService.getCartItems().isEmpty());
    }

    @Test
    @DisplayName("Should fail to add item when quantity is zero or negative")
    void testAddToCart_InvalidQuantity() {
        assertFalse(salesService.addToCart(1001, 0));
        assertFalse(salesService.addToCart(1001, -1));
    }

    @Test
    @DisplayName("Should fail to add item when item not found")
    void testAddToCart_ItemNotFound() {
        boolean result = salesService.addToCart(9999, 1);
        assertFalse(result);
    }

    @Test
    @DisplayName("Should fail to add item when out of stock")
    void testAddToCart_OutOfStock() {
        boolean result = salesService.addToCart(1003, 1); // Product C has 0 quantity
        assertFalse(result);
    }

    @Test
    @DisplayName("Should assign sequential order numbers")
    void testAddToCart_OrderNumbers() {
        salesService.addToCart(1001, 2);
        salesService.addToCart(1002, 1);

        List<Stock> cart = salesService.getCartItems();
        assertEquals(2, cart.size());
        assertEquals(1, cart.get(0).getOrderNo());
        assertEquals(2, cart.get(1).getOrderNo());
    }

    @Test
    @DisplayName("Should successfully remove order from cart")
    void testRemoveOrder_Success() {
        // Add items to cart
        salesService.addToCart(1001, 2);
        salesService.addToCart(1002, 1);

        // Remove first order
        boolean result = salesService.removeOrder(1);
        assertTrue(result);

        List<Stock> cart = salesService.getCartItems();
        assertEquals(1, cart.size());
        assertEquals(1002, cart.get(0).getStockID());

        // Verify order numbers were reassigned
        assertEquals(1, cart.get(0).getOrderNo());

        // Verify stock was refunded
        Stock stockItem = salesService.findStockItem(1001);
        assertEquals(10, stockItem.getQty()); // Refunded back to 10
    }

    @Test
    @DisplayName("Should fail to remove non-existent order")
    void testRemoveOrder_NotFound() {
        salesService.addToCart(1001, 2);
        boolean result = salesService.removeOrder(999);
        assertFalse(result);
    }

    @Test
    @DisplayName("Should reassign order numbers after removal")
    void testRemoveOrder_ReassignOrderNumbers() {
        salesService.addToCart(1001, 1);
        salesService.addToCart(1002, 1);
        salesService.addToCart(1001, 1);

        // Remove middle order (#2)
        salesService.removeOrder(2);

        List<Stock> cart = salesService.getCartItems();
        assertEquals(2, cart.size());
        assertEquals(1, cart.get(0).getOrderNo());
        assertEquals(2, cart.get(1).getOrderNo());
    }

    @Test
    @DisplayName("Should find cart item by order number")
    void testFindCartItemByOrderNo() {
        salesService.addToCart(1001, 2);
        salesService.addToCart(1002, 1);

        Stock found = salesService.findCartItemByOrderNo(2);
        assertNotNull(found);
        assertEquals(1002, found.getStockID());
        assertEquals(2, found.getOrderNo());
    }

    @Test
    @DisplayName("Should return null when cart item not found by order number")
    void testFindCartItemByOrderNo_NotFound() {
        salesService.addToCart(1001, 2);
        Stock found = salesService.findCartItemByOrderNo(999);
        assertNull(found);
    }

    @Test
    @DisplayName("Should successfully reduce order quantity")
    void testEditOrderQuantity_Reduce() {
        salesService.addToCart(1001, 5);
        Stock stockBefore = salesService.findStockItem(1001);
        int stockQtyBefore = stockBefore.getQty();

        boolean result = salesService.editOrderQuantity(1, 2, 1); // Reduce by 2
        assertTrue(result);

        Stock cartItem = salesService.findCartItemByOrderNo(1);
        assertEquals(3, cartItem.getQty()); // 5 - 2 = 3

        // Verify stock was refunded
        Stock stockAfter = salesService.findStockItem(1001);
        assertEquals(stockQtyBefore + 2, stockAfter.getQty());
    }

    @Test
    @DisplayName("Should successfully add order quantity")
    void testEditOrderQuantity_Add() {
        salesService.addToCart(1001, 3);
        Stock stockBefore = salesService.findStockItem(1001);
        int stockQtyBefore = stockBefore.getQty();

        boolean result = salesService.editOrderQuantity(1, 2, 2); // Add 2
        assertTrue(result);

        Stock cartItem = salesService.findCartItemByOrderNo(1);
        assertEquals(5, cartItem.getQty()); // 3 + 2 = 5

        // Verify stock was deducted
        Stock stockAfter = salesService.findStockItem(1001);
        assertEquals(stockQtyBefore - 2, stockAfter.getQty());
    }

    @Test
    @DisplayName("Should fail to reduce more than ordered quantity")
    void testEditOrderQuantity_ReduceTooMuch() {
        salesService.addToCart(1001, 3);
        boolean result = salesService.editOrderQuantity(1, 5, 1); // Try to reduce 5 from 3
        assertFalse(result);
    }

    @Test
    @DisplayName("Should fail to add more than available stock")
    void testEditOrderQuantity_AddTooMuch() {
        salesService.addToCart(1001, 3);
        // Stock now has 7 left (10 - 3 = 7)
        boolean result = salesService.editOrderQuantity(1, 10, 2); // Try to add 10 when only 7 available
        assertFalse(result);
    }

    @Test
    @DisplayName("Should fail to edit with invalid quantity change")
    void testEditOrderQuantity_InvalidQuantity() {
        salesService.addToCart(1001, 3);
        assertFalse(salesService.editOrderQuantity(1, 0, 1));
        assertFalse(salesService.editOrderQuantity(1, -1, 1));
    }

    @Test
    @DisplayName("Should fail to edit non-existent order")
    void testEditOrderQuantity_OrderNotFound() {
        salesService.addToCart(1001, 3);
        boolean result = salesService.editOrderQuantity(999, 1, 1);
        assertFalse(result);
    }

    @Test
    @DisplayName("Should fail to edit with invalid type")
    void testEditOrderQuantity_InvalidType() {
        salesService.addToCart(1001, 3);
        boolean result = salesService.editOrderQuantity(1, 1, 99); // Invalid type
        assertFalse(result);
    }
}
