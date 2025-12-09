package assignment.service;

import assignment.model.Stock;
import assignment.repo.StockRepository;
import java.util.List;
import java.util.ArrayList;

/**
 * Service layer for Stock domain.
 * Encapsulates business rules on top of StockRepository.
 */
public class StockService {

    private final StockRepository stockRepo;

    public StockService(StockRepository stockRepo) {
        this.stockRepo = stockRepo;
    }

    /**
     * Gets a list of all stock items from the file.
     * Returns the list of stock items.
     */
    public List<Stock> getAllStock() {
        return stockRepo.loadAllStock();
    }

    /**
     * Gets available stock (alias for getAllStock for backward compatibility).
     * Returns the list of stock items.
     */
    public List<Stock> getAvailableStock() {
        return getAllStock();
    }

    /**
     * Finds a stock item by ID.
     * Returns the stock item if found, or null if not found.
     */
    public Stock getStockByID(int id) {
        for (Stock stock : stockRepo.loadAllStock()) {
            if (stock.getStockID() == id) {
                return stock;
            }
        }
        return null;
    }

    /**
     * Generates the next available stock ID.
     * Returns the next ID to use.
     */
    public int getNextStockID() {
        return stockRepo.findMaxId() + 1;
    }

    /**
     * Checks if a stock name is unique (case-insensitive).
     * Returns true if unique, false otherwise.
     */
    public boolean isStockNameUnique(String stockName) {
        return !stockRepo.existsByName(stockName);
    }

    /**
     * Checks if a stock name is unique for update (ignoring the current ID).
     * Returns true if unique, false otherwise.
     */
    public boolean isStockNameUniqueForUpdate(String stockName, int currentId) {
        for (Stock stock : stockRepo.loadAllStock()) {
            if (stock.getStockName().equalsIgnoreCase(stockName)
                    && stock.getStockID() != currentId) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a new stock item if name is unique.
     * Returns true if added, false if name already exists.
     */
    public boolean addNewStock(Stock newStock) {
        if (!isStockNameUnique(newStock.getStockName())) {
            return false;
        }

        // Set the correct ID before writing
        newStock.setStockID(getNextStockID());
        stockRepo.appendStock(newStock);
        
        // Reload to update in-memory list
        stockRepo.loadAllStock();
        return true;
    }

    /**
     * Deletes a stock item by ID.
     * Returns true if successful, false otherwise.
     */
    public boolean deleteStock(int productID) {
        boolean deleted = stockRepo.deleteById(productID);
        if (deleted) {
            // Reload to update in-memory list
            stockRepo.loadAllStock();
        }
        return deleted;
    }

    /**
     * Updates an existing stock item.
     * Returns true if successful, false otherwise.
     */
    public boolean updateStock(int stockId, String newName, int newQty, double newPrice) {
        // Work on a local copy to avoid concurrent modification and stale state
        List<Stock> stocks = new ArrayList<>(stockRepo.loadAllStock());
        boolean updated = false;

        // Name uniqueness check (ignore current record)
        for (Stock s : stocks) {
            if (s.getStockID() != stockId && s.getStockName().equalsIgnoreCase(newName)) {
                return false;
            }
        }

        for (Stock s : stocks) {
            if (s.getStockID() == stockId) {
                s.setStockName(newName);
                s.setQty(newQty);
                s.setPrice(newPrice);
                updated = true;
                break;
            }
        }

        if (!updated) {
            return false;
        }

        // Persist updates to file
        stockRepo.saveAllStock(stocks);

        // Reload to ensure in-memory state reflects file
        List<Stock> reloaded = stockRepo.loadAllStock();
        for (Stock s : reloaded) {
            if (s.getStockID() == stockId) {
                // If we find it, consider update successful
                return true;
            }
        }
        return false;
    }
}
