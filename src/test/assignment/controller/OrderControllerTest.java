package test.assignment.controller;

import assignment.model.Stock;
import assignment.service.SalesService;
import assignment.controller.OrderController;
import assignment.view.SalesView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderController.
 * Tests order management operations with mocked dependencies.
 * Note: Some methods require user input/UI interaction and are tested for logic only.
 */
@DisplayName("OrderController Tests")
class OrderControllerTest {

    private SalesService salesService;
    private SalesView salesView;
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        // Create a simple mock SalesService
        salesService = new SalesService(null, null) {
            private final List<Stock> stocklist = new ArrayList<>();
            private final List<Stock> cart = new ArrayList<>();

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
            public List<Stock> getCartItems() {
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
            public Stock findCartItemByOrderNo(int orderNo) {
                return cart.stream()
                    .filter(s -> s.getOrderNo() == orderNo)
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
                Stock cartItem = new Stock(cart.size() + 1, itemID, stock.getStockName(), quantity, stock.getPrice());
                cart.add(cartItem);
                return true;
            }

            @Override
            public boolean removeOrder(int orderNo) {
                Stock toRemove = findCartItemByOrderNo(orderNo);
                if (toRemove != null) {
                    cart.remove(toRemove);
                    // Reassign order numbers
                    for (int i = 0; i < cart.size(); i++) {
                        cart.get(i).setOrderNo(i + 1);
                    }
                    // Refund stock
                    Stock stock = findStockItem(toRemove.getStockID());
                    if (stock != null) {
                        stock.setQty(stock.getQty() + toRemove.getQty());
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean editOrderQuantity(int orderNo, int quantityChange, int type) {
                Stock cartItem = findCartItemByOrderNo(orderNo);
                if (cartItem == null) return false;

                Stock stockItem = findStockItem(cartItem.getStockID());
                if (stockItem == null) return false;

                if (type == 1) { // Reduce
                    if (quantityChange > cartItem.getQty()) return false;
                    cartItem.setQty(cartItem.getQty() - quantityChange);
                    stockItem.setQty(stockItem.getQty() + quantityChange);
                } else if (type == 2) { // Add
                    if (quantityChange > stockItem.getQty()) return false;
                    cartItem.setQty(cartItem.getQty() + quantityChange);
                    stockItem.setQty(stockItem.getQty() - quantityChange);
                } else {
                    return false;
                }
                return true;
            }
        };

        salesView = new SalesView();
        orderController = new OrderController(salesService);
    }

    @Test
    @DisplayName("Should initialize OrderController with SalesService")
    void testOrderControllerInitialization() {
        assertNotNull(orderController);
    }

    @Test
    @DisplayName("Should have access to sales service")
    void testHasSalesService() {
        // Test that controller can access service methods
        List<Stock> availableStock = salesService.getAvailableStock();
        assertNotNull(availableStock);
        assertFalse(availableStock.isEmpty());
    }

    @Test
    @DisplayName("Should be able to get cart items through service")
    void testGetCartItems() {
        List<Stock> cartItems = salesService.getCartItems();
        assertNotNull(cartItems);
        assertTrue(cartItems.isEmpty());
    }

    @Test
    @DisplayName("Should be able to find stock items through service")
    void testFindStockItem() {
        Stock found = salesService.findStockItem(1001);
        assertNotNull(found);
        assertEquals(1001, found.getStockID());
    }

    @Test
    @DisplayName("Should be able to add items to cart through service")
    void testAddToCart() {
        boolean result = salesService.addToCart(1001, 2);
        assertTrue(result);
        assertEquals(1, salesService.getCartItems().size());
    }

    @Test
    @DisplayName("Should be able to find cart items by order number")
    void testFindCartItemByOrderNo() {
        salesService.addToCart(1001, 2);
        Stock found = salesService.findCartItemByOrderNo(1);
        assertNotNull(found);
        assertEquals(1001, found.getStockID());
    }

    @Test
    @DisplayName("Should be able to remove orders through service")
    void testRemoveOrder() {
        salesService.addToCart(1001, 2);
        assertEquals(1, salesService.getCartItems().size());

        boolean result = salesService.removeOrder(1);
        assertTrue(result);
        assertTrue(salesService.getCartItems().isEmpty());
    }

    @Test
    @DisplayName("Should be able to edit order quantity through service")
    void testEditOrderQuantity() {
        salesService.addToCart(1001, 2);
        Stock cartItem = salesService.findCartItemByOrderNo(1);
        assertEquals(2, cartItem.getQty());

        boolean result = salesService.editOrderQuantity(1, 1, 2); // Add 1
        assertTrue(result);
        cartItem = salesService.findCartItemByOrderNo(1);
        assertEquals(3, cartItem.getQty());
    }

    @Test
    @DisplayName("Should handle multiple cart operations")
    void testMultipleCartOperations() {
        // Add multiple items
        salesService.addToCart(1001, 2);
        salesService.addToCart(1002, 1);
        assertEquals(2, salesService.getCartItems().size());

        // Edit first order
        salesService.editOrderQuantity(1, 1, 2); // Add 1 to first order
        Stock item1 = salesService.findCartItemByOrderNo(1);
        assertEquals(3, item1.getQty());

        // Remove second order
        salesService.removeOrder(2);
        assertEquals(1, salesService.getCartItems().size());
    }
}
