package test.assignment.repo;

import assignment.model.Order;
import assignment.repo.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderRepository.
 * Tests file I/O operations for orders.
 */
@DisplayName("OrderRepository Tests")
class OrderRepositoryTest {

    private OrderRepository orderRepository;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
        // Note: In a real scenario, you might want to use reflection or dependency injection
        // to set a test data directory. For now, tests will use the actual data directory.
    }

    @Test
    @DisplayName("Should append order to file")
    void testAppendOrder() {
        Order order = new Order(1, 1001, "Product A", 2, 50.0);
        
        // Clear file first
        orderRepository.clearAllOrders();
        
        // Append order
        orderRepository.appendOrder(order);
        
        // Verify order was saved
        List<Order> orders = orderRepository.loadAllOrders();
        assertEquals(1, orders.size());
        assertEquals(1001, orders.get(0).getStockID());
        assertEquals("PRODUCT A", orders.get(0).getStockName()); // getStockName() returns uppercase
        assertEquals(2, orders.get(0).getQuantity());
        assertEquals(50.0, orders.get(0).getPrice(), 0.01);
    }

    @Test
    @DisplayName("Should append multiple orders to file")
    void testAppendOrders() {
        List<Order> orders = List.of(
            new Order(1, 1001, "Product A", 2, 50.0),
            new Order(2, 1002, "Product B", 1, 100.0),
            new Order(3, 1003, "Product C", 3, 25.0)
        );
        
        // Clear file first
        orderRepository.clearAllOrders();
        
        // Append orders
        orderRepository.appendOrders(orders);
        
        // Verify orders were saved
        List<Order> loadedOrders = orderRepository.loadAllOrders();
        assertEquals(3, loadedOrders.size());
        assertEquals(1001, loadedOrders.get(0).getStockID());
        assertEquals(1002, loadedOrders.get(1).getStockID());
        assertEquals(1003, loadedOrders.get(2).getStockID());
    }

    @Test
    @DisplayName("Should load all orders from file")
    void testLoadAllOrders() {
        // Clear and add test orders
        orderRepository.clearAllOrders();
        orderRepository.appendOrder(new Order(1, 1001, "Product A", 2, 50.0));
        orderRepository.appendOrder(new Order(2, 1002, "Product B", 1, 100.0));
        
        // Load orders
        List<Order> orders = orderRepository.loadAllOrders();
        
        assertNotNull(orders);
        assertEquals(2, orders.size());
        // Verify order numbers are assigned by position
        assertEquals(1, orders.get(0).getOrderNo());
        assertEquals(2, orders.get(1).getOrderNo());
    }

    @Test
    @DisplayName("Should assign order numbers by position")
    void testOrderNumberAssignment() {
        orderRepository.clearAllOrders();
        orderRepository.appendOrder(new Order(1, 1001, "Product A", 2, 50.0));
        orderRepository.appendOrder(new Order(2, 1002, "Product B", 1, 100.0));
        
        List<Order> orders = orderRepository.loadAllOrders();
        
        // Order numbers should be 1, 2 based on position
        assertEquals(1, orders.get(0).getOrderNo());
        assertEquals(2, orders.get(1).getOrderNo());
    }

    @Test
    @DisplayName("Should update order in file")
    void testUpdateOrder() {
        orderRepository.clearAllOrders();
        orderRepository.appendOrder(new Order(1, 1001, "Product A", 2, 50.0));
        orderRepository.appendOrder(new Order(2, 1002, "Product B", 1, 100.0));
        
        // Update first order
        Order updatedOrder = new Order(1, 1001, "Product A Updated", 5, 50.0);
        boolean result = orderRepository.updateOrder(updatedOrder);
        
        assertTrue(result);
        
        // Verify update
        List<Order> orders = orderRepository.loadAllOrders();
        assertEquals(2, orders.size());
        assertEquals("PRODUCT A UPDATED", orders.get(0).getStockName()); // getStockName() returns uppercase
        assertEquals(5, orders.get(0).getQuantity());
    }

    @Test
    @DisplayName("Should delete order from file")
    void testDeleteOrder() {
        orderRepository.clearAllOrders();
        orderRepository.appendOrder(new Order(1, 1001, "Product A", 2, 50.0));
        orderRepository.appendOrder(new Order(2, 1002, "Product B", 1, 100.0));
        orderRepository.appendOrder(new Order(3, 1003, "Product C", 3, 25.0));
        
        // Delete second order (orderNo = 2)
        boolean result = orderRepository.deleteOrder(2);
        
        assertTrue(result);
        
        // Verify deletion
        List<Order> orders = orderRepository.loadAllOrders();
        assertEquals(2, orders.size());
        assertEquals(1001, orders.get(0).getStockID());
        assertEquals(1003, orders.get(1).getStockID());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent order")
    void testDeleteNonExistentOrder() {
        orderRepository.clearAllOrders();
        orderRepository.appendOrder(new Order(1, 1001, "Product A", 2, 50.0));
        
        // Try to delete non-existent order
        boolean result = orderRepository.deleteOrder(999);
        
        assertFalse(result);
        
        // Verify no orders were deleted
        List<Order> orders = orderRepository.loadAllOrders();
        assertEquals(1, orders.size());
    }

    @Test
    @DisplayName("Should clear all orders from file")
    void testClearAllOrders() {
        orderRepository.appendOrder(new Order(1, 1001, "Product A", 2, 50.0));
        orderRepository.appendOrder(new Order(2, 1002, "Product B", 1, 100.0));
        
        // Clear all orders
        orderRepository.clearAllOrders();
        
        // Verify file is empty
        List<Order> orders = orderRepository.loadAllOrders();
        assertTrue(orders.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when file is empty")
    void testLoadAllOrdersEmptyFile() {
        orderRepository.clearAllOrders();
        
        List<Order> orders = orderRepository.loadAllOrders();
        
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    @DisplayName("Should get order count")
    void testGetOrderCount() {
        orderRepository.clearAllOrders();
        assertEquals(0, orderRepository.getOrderCount());
        
        orderRepository.appendOrder(new Order(1, 1001, "Product A", 2, 50.0));
        assertEquals(1, orderRepository.getOrderCount());
        
        orderRepository.appendOrder(new Order(2, 1002, "Product B", 1, 100.0));
        assertEquals(2, orderRepository.getOrderCount());
    }
}

