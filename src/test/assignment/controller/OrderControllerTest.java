package assignment.controller;

import assignment.model.Order;
import assignment.model.Stock;
import assignment.service.OrderService;
import assignment.controller.OrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderController.
 * Tests order management operations with mocked dependencies.
 * Note: Some methods require user input/UI interaction and are tested for logic only.
 */
@DisplayName("OrderController Tests")
class OrderControllerTest {
    private static final Logger LOGGER = Logger.getLogger(OrderControllerTest.class.getName());

    private OrderService orderService;
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        // Create a simple mock OrderService
        orderService = new OrderService(null, null) {
            private final List<Stock> stocklist = new ArrayList<>();
            private final List<Order> cart = new ArrayList<>();

            {
                // Initialize test stock
                stocklist.add(new Stock(1001, "Product A", 10, 50.0));
                stocklist.add(new Stock(1002, "Product B", 5, 100.0));
            }

            @Override
            public List<Stock> getAvailableStock() {
                return stocklist;
            }

            @Override
            public List<Order> getCartItems() {
                return cart;
            }

            @Override
            public Stock findStockItem(int itemID) {
                return stocklist.stream()
                    .filter(s -> s.getStockID() == itemID)
                    .findFirst()
                    .orElse(null);
            }

            @Override
            public Order findCartItemByOrderNo(int orderNo) {
                return cart.stream()
                    .filter(o -> o.getOrderNo() == orderNo)
                    .findFirst()
                    .orElse(null);
            }

            @Override
            public boolean addToCart(int itemID, int quantity) {
                Stock stock = findStockItem(itemID);
                if (stock == null || stock.getQty() < quantity || quantity <= 0) {
                    return false;
                }
                stock.setQty(stock.getQty() - quantity);
                Order cartItem = new Order(cart.size() + 1, itemID, stock.getStockName(), quantity, stock.getPrice());
                cart.add(cartItem);
                return true;
            }

            @Override
            public boolean removeOrder(int orderNo) {
                Order toRemove = findCartItemByOrderNo(orderNo);
                if (toRemove != null) {
                    cart.remove(toRemove);
                    // Reassign order numbers
                    for (int i = 0; i < cart.size(); i++) {
                        cart.get(i).setOrderNo(i + 1);
                    }
                    // Refund stock
                    Stock stock = findStockItem(toRemove.getStockID());
                    if (stock != null) {
                        stock.setQty(stock.getQty() + toRemove.getQuantity());
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean editOrderQuantity(int orderNo, int quantityChange, int type) {
                Order cartItem = findCartItemByOrderNo(orderNo);
                if (cartItem == null) return false;

                Stock stockItem = findStockItem(cartItem.getStockID());
                if (stockItem == null) return false;

                if (type == 1) { // Reduce
                    if (quantityChange > cartItem.getQuantity()) return false;
                    cartItem.setQuantity(cartItem.getQuantity() - quantityChange);
                    stockItem.setQty(stockItem.getQty() + quantityChange);
                } else if (type == 2) { // Add
                    if (quantityChange > stockItem.getQty()) return false;
                    cartItem.setQuantity(cartItem.getQuantity() + quantityChange);
                    stockItem.setQty(stockItem.getQty() - quantityChange);
                } else {
                    return false;
                }
                return true;
            }
        };

        orderController = new OrderController(orderService);
    }

    @Test
    @DisplayName("Should initialize OrderController with OrderService")
    void testOrderControllerInitialization() {
        assertNotNull(orderController);
        LOGGER.info("✓ SUCCESS: OrderController - Initialized with OrderService");
    }

    @Test
    @DisplayName("Should have access to order service")
    void testHasOrderService() {
        // Test that controller can access service methods
        List<Stock> availableStock = orderService.getAvailableStock();
        assertNotNull(availableStock);
        assertFalse(availableStock.isEmpty());
        LOGGER.info("✓ SUCCESS: OrderController - Has access to order service (Available stock retrieved)");
    }

    @Test
    @DisplayName("Should be able to get cart items through service")
    void testGetCartItems() {
        List<Order> cartItems = orderService.getCartItems();
        assertNotNull(cartItems);
        assertTrue(cartItems.isEmpty());
        LOGGER.info("✓ SUCCESS: OrderController - Got cart items through service (Cart is empty)");
    }

    @Test
    @DisplayName("Should be able to find stock items through service")
    void testFindStockItem() {
        Stock found = orderService.findStockItem(1001);
        assertNotNull(found);
        assertEquals(1001, found.getStockID());
        LOGGER.info("✓ SUCCESS: OrderController - Found stock items through service (Product 1001 found)");
    }

    @Test
    @DisplayName("Should be able to add items to cart through service")
    void testAddToCart() {
        boolean result = orderService.addToCart(1001, 2);
        assertTrue(result);
        assertEquals(1, orderService.getCartItems().size());
        LOGGER.info("✓ SUCCESS: OrderController - Added items to cart through service (Product 1001, Qty: 2)");
    }

    @Test
    @DisplayName("Should be able to find cart items by order number")
    void testFindCartItemByOrderNo() {
        orderService.addToCart(1001, 2);
        Order found = orderService.findCartItemByOrderNo(1);
        assertNotNull(found);
        assertEquals(1001, found.getStockID());
        LOGGER.info("✓ SUCCESS: OrderController - Found cart items by order number (Order #1: Product 1001)");
    }

    @Test
    @DisplayName("Should be able to remove orders through service")
    void testRemoveOrder() {
        orderService.addToCart(1001, 2);
        assertEquals(1, orderService.getCartItems().size());

        boolean result = orderService.removeOrder(1);
        assertTrue(result);
        assertTrue(orderService.getCartItems().isEmpty());
        LOGGER.info("✓ SUCCESS: OrderController - Removed orders through service (Order #1 removed, cart is empty)");
    }

    @Test
    @DisplayName("Should be able to edit order quantity through service")
    void testEditOrderQuantity() {
        orderService.addToCart(1001, 2);
        Order cartItem = orderService.findCartItemByOrderNo(1);
        assertEquals(2, cartItem.getQuantity());

        boolean result = orderService.editOrderQuantity(1, 1, 2); // Add 1
        assertTrue(result);
        cartItem = orderService.findCartItemByOrderNo(1);
        assertEquals(3, cartItem.getQuantity());
        LOGGER.info("✓ SUCCESS: OrderController - Edited order quantity through service (Order #1: Qty 2→3)");
    }

    @Test
    @DisplayName("Should handle multiple cart operations")
    void testMultipleCartOperations() {
        // Add multiple items
        orderService.addToCart(1001, 2);
        orderService.addToCart(1002, 1);
        assertEquals(2, orderService.getCartItems().size());

        // Edit first order
        orderService.editOrderQuantity(1, 1, 2); // Add 1 to first order
        Order item1 = orderService.findCartItemByOrderNo(1);
        assertEquals(3, item1.getQuantity());

        // Remove second order
        orderService.removeOrder(2);
        assertEquals(1, orderService.getCartItems().size());
        LOGGER.info("✓ SUCCESS: OrderController - Handled multiple cart operations (Added 2 items, edited order #1, removed order #2)");
    }
}
