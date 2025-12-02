package assignment.service;

import assignment.model.Stock;
import assignment.repo.StockRepository;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockService {
    private final StockRepository stockRepo;
    private static final Logger LOGGER = Logger.getLogger(StockService.class.getName());

    public StockService(StockRepository stockRepo) {
        this.stockRepo = stockRepo;
        this.stockRepo.loadStockFromFile(); // Ensure stock is loaded on service initialization
    }

    public List<Stock> getAvailableStock() {
        return stockRepo.getStocklist();
    }

    public Stock getStockByID(int id) {
        for (Stock stock : stockRepo.getStocklist()) {
            if (stock.getStockID() == id) {
                return stock;
            }
        }
        return null;
    }

    public int getNextStockID() {
        return stockRepo.findLastStockID() + 1;
    }

    public boolean isStockNameUnique(String stockName) {
        return !stockRepo.checkNameExists(stockName);
    }

    public boolean addNewStock(Stock newStock) {
        if (!isStockNameUnique(newStock.getStockName())) {
            return false;
        }

        try {
            // Set the correct ID before writing
            newStock.setStockID(getNextStockID());
            stockRepo.addStockToFile(newStock);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save new stock", e);
            return false;
        }
    }

    public boolean deleteStock(int productID) {
        Stock product = getStockByID(productID);
        if (product == null) {
            return false;
        }
        stockRepo.deleteProductFromFile(productID);
        return true;
    }
}