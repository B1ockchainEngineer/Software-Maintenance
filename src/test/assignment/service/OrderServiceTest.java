package assignment.service;

import assignment.model.Order;
import assignment.model.Stock;
import assignment.repo.OrderRepository;
import assignment.repo.StockRepository;
import assignment.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderService.
 * Tests cart operations: add, remove, edit, and search functionality.
 */
@DisplayName("OrderService Tests")
class OrderServiceTest {
    private final Logger logger = Logger.getLogger(OrderServiceTest.class.getName());

    private StockRepository stockRepo;
    private OrderRepository orderRepo;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        // Create mock repositories with in-memory data
        stockRepo = new StockRepository() {
            private final List<Stock> stocklist = new ArrayList<>();
            private final List<Order> cart = new ArrayList<>();

            @Override
            public List<Stock> getStocklist() {
                return stocklist;
            }

            @Override
            public List<Order> getCart() {
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
            public void appendOrder(Order order) {
                // Mock - do nothing
            }

            @Override
            public boolean deleteOrder(int orderNo) {
                // Mock - return success
                return true;
            }

            @Override
            public boolean updateOrder(Order order) {
                // Mock - return success
                return true;
            }

            @Override
            public List<Order> loadAllOrders() {
                return new ArrayList<>(); // Empty initially
            }

            @Override
            public void clearAllOrders() {
                // Mock - do nothing
            }
        };

        orderService = new OrderService(stockRepo, orderRepo);
    }

    @Test
    @DisplayName("Should return available stock items")
    void testGetAvailableStock() {
        List<Stock> availableStock = orderService.getAvailableStock();
        assertNotNull(availableStock);
        assertEquals(3, availableStock.size());
        logger.info("✓ SUCCESS: OrderService - Retrieved available stock items (3 items found)");
    }

    @Test
    @DisplayName("Should return empty cart initially")
    void testGetCartItems_InitiallyEmpty() {
        List<Order> cartItems = orderService.getCartItems();
        assertNotNull(cartItems);
        assertTrue(cartItems.isEmpty());
        logger.info("✓ SUCCESS: OrderService - Cart is initially empty as expected");
    }

    @Test
    @DisplayName("Should find stock item by ID")
    void testFindStockItem_Exists() {
        Stock found = orderService.findStockItem(1001);
        assertNotNull(found);
        assertEquals(1001, found.getStockID());
        assertEquals("PRODUCT A", found.getStockName()); // getStockName() returns uppercase
        logger.info("✓ SUCCESS: OrderService - Found stock item by ID (1001: PRODUCT A)");
    }

    @Test
    @DisplayName("Should return null when stock item not found")
    void testFindStockItem_NotFound() {
        Stock found = orderService.findStockItem(9999);
        assertNull(found);
        logger.info("✓ SUCCESS: OrderService - Correctly returns null for non-existent stock item (ID: 9999)");
    }

    @Test
    @DisplayName("Should successfully add item to cart")
    void testAddToCart_Success() {
        boolean result = orderService.addToCart(1001, 3);
        assertTrue(result);

        List<Order> cart = orderService.getCartItems();
        assertEquals(1, cart.size());
        assertEquals(1001, cart.get(0).getStockID());
        assertEquals(3, cart.get(0).getQuantity());
        assertEquals(1, cart.get(0).getOrderNo()); // First order should be #1

        // Verify stock was deducted
        Stock stockItem = orderService.findStockItem(1001);
        assertEquals(7, stockItem.getQty()); // 10 - 3 = 7
        logger.info("✓ SUCCESS: OrderService - Successfully added item to cart (Product 1001, Qty: 3, Stock deducted: 10→7)");
    }

    @Test
    @DisplayName("Should fail to add item when stock insufficient")
    void testAddToCart_InsufficientStock() {
        boolean result = orderService.addToCart(1001, 15); // More than available (10)
        assertFalse(result);
        assertTrue(orderService.getCartItems().isEmpty());
        logger.info("✓ SUCCESS: OrderService - Correctly rejected adding item with insufficient stock (Requested: 15, Available: 10)");
    }

    @Test
    @DisplayName("Should fail to add item when quantity is zero or negative")
    void testAddToCart_InvalidQuantity() {
        assertFalse(orderService.addToCart(1001, 0));
        assertFalse(orderService.addToCart(1001, -1));
        logger.info("✓ SUCCESS: OrderService - Correctly rejected invalid quantities (0 and -1)");
    }

    @Test
    @DisplayName("Should fail to add item when item not found")
    void testAddToCart_ItemNotFound() {
        boolean result = orderService.addToCart(9999, 1);
        assertFalse(result);
        logger.info("✓ SUCCESS: OrderService - Correctly rejected adding non-existent item (ID: 9999)");
    }

    @Test
    @DisplayName("Should fail to add item when out of stock")
    void testAddToCart_OutOfStock() {
        boolean result = orderService.addToCart(1003, 1); // Product C has 0 quantity
        assertFalse(result);
        logger.info("✓ SUCCESS: OrderService - Correctly rejected adding out-of-stock item (Product 1003, Qty: 0)");
    }

    @Test
    @DisplayName("Should assign sequential order numbers")
    void testAddToCart_OrderNumbers() {
        orderService.addToCart(1001, 2);
        orderService.addToCart(1002, 1);

        List<Order> cart = orderService.getCartItems();
        assertEquals(2, cart.size());
        assertEquals(1, cart.get(0).getOrderNo());
        assertEquals(2, cart.get(1).getOrderNo());
        logger.info("✓ SUCCESS: OrderService - Sequential order numbers assigned correctly (Order #1, #2)");
    }

    @Test
    @DisplayName("Should successfully remove order from cart")
    void testRemoveOrder_Success() {
        // Add items to cart
        orderService.addToCart(1001, 2);
        orderService.addToCart(1002, 1);

        // Remove first order
        boolean result = orderService.removeOrder(1);
        assertTrue(result);

        List<Order> cart = orderService.getCartItems();
        assertEquals(1, cart.size());
        assertEquals(1002, cart.get(0).getStockID());

        // Verify order numbers were reassigned
        assertEquals(1, cart.get(0).getOrderNo());

        // Verify stock was refunded
        Stock stockItem = orderService.findStockItem(1001);
        assertEquals(10, stockItem.getQty()); // Refunded back to 10
        logger.info("✓ SUCCESS: OrderService - Successfully removed order from cart (Order #1 removed, stock refunded: 7→10)");
    }

    @Test
    @DisplayName("Should fail to remove non-existent order")
    void testRemoveOrder_NotFound() {
        orderService.addToCart(1001, 2);
        boolean result = orderService.removeOrder(999);
        assertFalse(result);
        logger.info("✓ SUCCESS: OrderService - Correctly rejected removing non-existent order (Order #999)");
    }

    @Test
    @DisplayName("Should reassign order numbers after removal")
    void testRemoveOrder_ReassignOrderNumbers() {
        orderService.addToCart(1001, 1);
        orderService.addToCart(1002, 1);
        orderService.addToCart(1001, 1);

        // Remove middle order (#2)
        orderService.removeOrder(2);

        List<Order> cart = orderService.getCartItems();
        assertEquals(2, cart.size());
        assertEquals(1, cart.get(0).getOrderNo());
        assertEquals(2, cart.get(1).getOrderNo());
        logger.info("✓ SUCCESS: OrderService - Order numbers reassigned after removal (Order #2 removed, remaining: #1, #2)");
    }

    @Test
    @DisplayName("Should find cart item by order number")
    void testFindCartItemByOrderNo() {
        orderService.addToCart(1001, 2);
        orderService.addToCart(1002, 1);

        Order found = orderService.findCartItemByOrderNo(2);
        assertNotNull(found);
        assertEquals(1002, found.getStockID());
        assertEquals(2, found.getOrderNo());
        logger.info("✓ SUCCESS: OrderService - Found cart item by order number (Order #2: Product 1002)");
    }

    @Test
    @DisplayName("Should return null when cart item not found by order number")
    void testFindCartItemByOrderNo_NotFound() {
        orderService.addToCart(1001, 2);
        Order found = orderService.findCartItemByOrderNo(999);
        assertNull(found);
        logger.info("✓ SUCCESS: OrderService - Correctly returns null for non-existent order number (#999)");
    }

    @Test
    @DisplayName("Should successfully reduce order quantity")
    void testEditOrderQuantity_Reduce() {
        orderService.addToCart(1001, 5);
        Stock stockBefore = orderService.findStockItem(1001);
        int stockQtyBefore = stockBefore.getQty();

        boolean result = orderService.editOrderQuantity(1, 2, 1); // Reduce by 2
        assertTrue(result);

        Order cartItem = orderService.findCartItemByOrderNo(1);
        assertEquals(3, cartItem.getQuantity()); // 5 - 2 = 3

        // Verify stock was refunded
        Stock stockAfter = orderService.findStockItem(1001);
        assertEquals(stockQtyBefore + 2, stockAfter.getQty());
        logger.info("✓ SUCCESS: OrderService - Successfully reduced order quantity (Order #1: 5→3, Stock refunded: +2)");
    }

    @Test
    @DisplayName("Should successfully add order quantity")
    void testEditOrderQuantity_Add() {
        orderService.addToCart(1001, 3);
        Stock stockBefore = orderService.findStockItem(1001);
        int stockQtyBefore = stockBefore.getQty();

        boolean result = orderService.editOrderQuantity(1, 2, 2); // Add 2
        assertTrue(result);

        Order cartItem = orderService.findCartItemByOrderNo(1);
        assertEquals(5, cartItem.getQuantity()); // 3 + 2 = 5

        // Verify stock was deducted
        Stock stockAfter = orderService.findStockItem(1001);
        assertEquals(stockQtyBefore - 2, stockAfter.getQty());
        logger.info("✓ SUCCESS: OrderService - Successfully added order quantity (Order #1: 3→5, Stock deducted: -2)");
    }

    @Test
    @DisplayName("Should fail to reduce more than ordered quantity")
    void testEditOrderQuantity_ReduceTooMuch() {
        orderService.addToCart(1001, 3);
        boolean result = orderService.editOrderQuantity(1, 5, 1); // Try to reduce 5 from 3
        assertFalse(result);
        logger.info("✓ SUCCESS: OrderService - Correctly rejected reducing more than ordered (Order Qty: 3, Attempted: -5)");
    }

    @Test
    @DisplayName("Should fail to add more than available stock")
    void testEditOrderQuantity_AddTooMuch() {
        orderService.addToCart(1001, 3);
        // Stock now has 7 left (10 - 3 = 7)
        boolean result = orderService.editOrderQuantity(1, 10, 2); // Try to add 10 when only 7 available
        assertFalse(result);
        logger.info("✓ SUCCESS: OrderService - Correctly rejected adding more than available stock (Available: 7, Attempted: +10)");
    }

    @Test
    @DisplayName("Should fail to edit with invalid quantity change")
    void testEditOrderQuantity_InvalidQuantity() {
        orderService.addToCart(1001, 3);
        assertFalse(orderService.editOrderQuantity(1, 0, 1));
        assertFalse(orderService.editOrderQuantity(1, -1, 1));
        logger.info("✓ SUCCESS: OrderService - Correctly rejected invalid quantity changes (0 and -1)");
    }

    @Test
    @DisplayName("Should fail to edit non-existent order")
    void testEditOrderQuantity_OrderNotFound() {
        orderService.addToCart(1001, 3);
        boolean result = orderService.editOrderQuantity(999, 1, 1);
        assertFalse(result);
        logger.info("✓ SUCCESS: OrderService - Correctly rejected editing non-existent order (Order #999)");
    }

    @Test
    @DisplayName("Should fail to edit with invalid type")
    void testEditOrderQuantity_InvalidType() {
        orderService.addToCart(1001, 3);
        boolean result = orderService.editOrderQuantity(1, 1, 99); // Invalid type
        assertFalse(result);
        logger.info("✓ SUCCESS: OrderService - Correctly rejected invalid edit type (Type: 99, Valid: 1 or 2)");
    }
}
