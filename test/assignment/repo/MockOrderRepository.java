package assignment.repo;

import assignment.model.Stock;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock implementation of OrderRepository for unit testing.
 * Stores orders in memory without file I/O.
 * Test-only utility class.
 */
public class MockOrderRepository extends OrderRepository {
    private final List<Stock> orders = new ArrayList<>();

    @Override
    public List<Stock> loadAllOrders() {
        List<Stock> result = new ArrayList<>();
        int orderNo = 1;
        for (Stock order : orders) {
            // Create a copy with orderNo assigned by position
            Stock orderWithNo = new Stock(orderNo, order.getStockID(), 
                    order.getStockName(), order.getQty(), order.getPrice());
            result.add(orderWithNo);
            orderNo++;
        }
        return result;
    }

    @Override
    public void appendOrder(Stock order) {
        orders.add(order);
    }

    @Override
    public boolean updateOrder(Stock order) {
        // Find order by orderNo (position-based)
        if (order.getOrderNo() > 0 && order.getOrderNo() <= orders.size()) {
            int index = order.getOrderNo() - 1;
            orders.set(index, order);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteOrder(int orderNo) {
        // Delete by position (orderNo represents position: 1, 2, 3, ...)
        if (orderNo > 0 && orderNo <= orders.size()) {
            int index = orderNo - 1;
            orders.remove(index);
            return true;
        }
        return false;
    }

    @Override
    public void clearAllOrders() {
        orders.clear();
    }

    @Override
    public int getOrderCount() {
        return orders.size();
    }

    // Helper methods for testing
    public void clear() {
        orders.clear();
    }

    public int getOrderCountDirect() {
        return orders.size();
    }
}

