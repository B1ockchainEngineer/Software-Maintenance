package assignment.repo;

import assignment.model.Stock;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock implementation of StockRepository for unit testing.
 * Stores data in memory without file I/O.
 * Test-only utility class.
 * Maintains stocklist and cart for SalesService compatibility.
 */
public class MockStockRepository extends StockRepository {
    private final List<Stock> stocks = new ArrayList<>();
    private final List<Stock> stocklist = new ArrayList<>(); // In-memory stocklist for SalesService
    private final List<Stock> cart = new ArrayList<>(); // Cart for SalesService
    private int nextId = 10001;

    @Override
    public List<Stock> loadAllStock() {
        // Update stocklist to match stocks
        stocklist.clear();
        stocklist.addAll(stocks);
        return new ArrayList<>(stocks);
    }

    @Override
    public void appendStock(Stock stock) {
        if (stock.getStockID() == 0) {
            stock.setStockID(nextId++);
        }
        stocks.add(stock);
        stocklist.add(stock); // Keep stocklist in sync
    }

    @Override
    public void saveAllStock(List<Stock> stockList) {
        stocks.clear();
        stocks.addAll(stockList);
        stocklist.clear();
        stocklist.addAll(stockList); // Keep stocklist in sync
    }

    @Override
    public boolean deleteById(int stockIdToDelete) {
        boolean removed = stocks.removeIf(s -> s.getStockID() == stockIdToDelete);
        if (removed) {
            stocklist.removeIf(s -> s.getStockID() == stockIdToDelete); // Keep stocklist in sync
        }
        return removed;
    }

    @Override
    public boolean existsByName(String name) {
        String upperName = name.toUpperCase();
        for (Stock stock : stocks) {
            if (stock.getStockName().equals(upperName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int findMaxId() {
        int maxId = 10000;
        for (Stock stock : stocks) {
            if (stock.getStockID() > maxId) {
                maxId = stock.getStockID();
            }
        }
        return maxId;
    }

    // Override methods used by SalesService
    @Override
    public List<Stock> getStocklist() {
        // Ensure stocklist is synced with stocks
        if (stocklist.size() != stocks.size()) {
            loadAllStock();
        }
        return stocklist;
    }

    @Override
    public List<Stock> getCart() {
        return cart;
    }

    @Override
    public void saveStockToFile() {
        // In mock, this just ensures stocklist is synced with stocks
        // SalesService modifies stocklist directly, so we sync it back to stocks
        stocks.clear();
        stocks.addAll(stocklist);
    }

    @Override
    public List<Stock> loadStockFromFile() {
        return loadAllStock();
    }

    // Helper methods for testing
    public void clear() {
        stocks.clear();
        stocklist.clear();
        cart.clear();
        nextId = 10001;
    }

    public int getStockCount() {
        return stocks.size();
    }
}

