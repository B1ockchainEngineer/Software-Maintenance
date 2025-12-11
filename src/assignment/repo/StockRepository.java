package assignment.repo;

import assignment.model.Order;
import assignment.model.Stock;
import assignment.util.config.StockConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository responsible for all file I/O for stock data (stock.txt).
 * Refactored to remove static state and match MemberRepository pattern.
 */
public class StockRepository {

    private static final Logger LOGGER = Logger.getLogger(StockRepository.class.getName());

    // Instance-based state (no longer static)
    private List<Stock> stocklist = new ArrayList<>();

    // Cart management (kept for backward compatibility with SalesService)
    // TODO: Move to Sales module in future refactoring
    private List<Order> cart = new ArrayList<>();

    /**
     * Checks if the file exists.
     * Creates a new file if it does not exist.
     */
    private void ensureFileExists() {
        ensureDirectoriesExist();
        File file = new File(StockConfig.STOCK_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, StockConfig.ErrorMessage.FILE_CREATE_ERROR, e);
            }
        }
    }

    private void ensureDirectoriesExist() {
        File dataDir = new File(StockConfig.DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        File tempDir = new File(StockConfig.TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
    }

    /**
     * Reads all stock items from the text file.
     * Returns a list of Stock objects.
     */
    public List<Stock> loadAllStock() {
        ensureFileExists();
        stocklist.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(StockConfig.STOCK_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                    try {
                        int stockId = Integer.parseInt(parts[0]);
                        String stockName = parts[1];
                        int stockQty = Integer.parseInt(parts[2]);
                        double stockPrice = Double.parseDouble(parts[3]);
                        stocklist.add(new Stock(stockId, stockName, stockQty, stockPrice));
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.WARNING, "Skipping invalid stock line: " + line, e);
                        // Continue processing other lines
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, StockConfig.ErrorMessage.FILE_READ_ERROR, e);
        }

        return stocklist;
    }

    /**
     * Adds a new stock item to the end of the file.
     */
    public void appendStock(Stock stock) {
        ensureFileExists();

        try (FileWriter writer = new FileWriter(StockConfig.STOCK_FILE_PATH, true)) {
            writer.write(stock.toFileString() + "\n");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, StockConfig.ErrorMessage.FILE_WRITE_ERROR, e);
        }
    }

    /**
     * Saves the list of stock items to the file.
     * This overwrites all existing data in the file.
     */
    public void saveAllStock(List<Stock> stockList) {
        ensureFileExists();

        try (FileWriter fw = new FileWriter(StockConfig.STOCK_FILE_PATH, false)) { // overwrite file
            for (Stock stock : stockList) {
                String line = stock.toFileString() + System.lineSeparator();
                fw.write(line);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, StockConfig.ErrorMessage.FILE_UPDATE_ERROR, e);
        }
    }

    /**
     * Deletes a stock item from the file by ID.
     * Returns true if the stock was deleted.
     */
    public boolean deleteById(int stockIdToDelete) {
        ensureFileExists();
        File inputFile = new File(StockConfig.STOCK_FILE_PATH);
        File tempFile = new File(StockConfig.TEMP_DELETE_FILE_PATH);

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");

                if (data.length >= 1) {
                    try {
                        int productID = Integer.parseInt(data[0]);
                        if (productID == stockIdToDelete) {
                            found = true;
                            continue; // skip this record
                        }
                    } catch (NumberFormatException ignored) {
                        // Keep invalid lines
                    }
                }

                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, StockConfig.ErrorMessage.FILE_DELETE_ERROR, e);
            return false;
        }

        if (found) {
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                LOGGER.severe(StockConfig.ErrorMessage.FILE_DELETE_ERROR);
            }
        } else {
            tempFile.delete();
        }
        return found;
    }

    /**
     * Checks if a product name already exists in the file (case-insensitive).
     * Returns true if found, false otherwise.
     */
    public boolean existsByName(String name) {
        ensureFileExists();
        String upperName = name.toUpperCase();

        try (BufferedReader br = new BufferedReader(new FileReader(StockConfig.STOCK_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String stockName = parts[1];
                    if (stockName.equals(upperName)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, StockConfig.ErrorMessage.FILE_READ_ERROR, e);
        }
        return false;
    }

    /**
     * Finds the largest Stock ID currently in the file.
     * Returns the maximum ID, or BASE_STOCK_ID if no stocks exist.
     */
    public int findMaxId() {
        ensureFileExists();
        int maxId = StockConfig.BASE_STOCK_ID;

        try (BufferedReader reader = new BufferedReader(new FileReader(StockConfig.STOCK_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length > 0) {
                    try {
                        int stockID = Integer.parseInt(parts[0]);
                        if (stockID > maxId) {
                            maxId = stockID;
                        }
                    } catch (NumberFormatException ignored) {
                        // Ignore lines with invalid ID format
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, StockConfig.ErrorMessage.FILE_READ_ERROR, e);
        }
        return maxId;
    }

    // ========== Legacy methods for backward compatibility with SalesService ==========

    /**
     * Legacy alias for loadAllStock() kept for backward compatibility.
     */
    public List<Stock> getStocklist() {
        if (stocklist.isEmpty()) {
            loadAllStock();
        }
        return stocklist;
    }

    /**
     * Legacy alias for loadAllStock().
     */
    public List<Stock> loadStockFromFile() {
        return loadAllStock();
    }

    /**
     * Cart management used by Sales module.
     */
    public List<Order> getCart() {
        return cart;
    }

    /**
     * Clears cart (used by Sales module).
     */
    public void clearCart() {
        cart.clear();
    }

    /**
     * @deprecated Use findMaxId() instead. Kept for backward compatibility.
     */
    @Deprecated
    public int findLastStockID() {
        return findMaxId();
    }

    /**
     * @deprecated Use existsByName() instead. Kept for backward compatibility.
     */
    @Deprecated
    public boolean checkNameExists(String name) {
        return existsByName(name);
    }

    /**
     * @deprecated Use appendStock() instead. Kept for backward compatibility.
     */
    @Deprecated
    public void addStockToFile(Stock newStock) throws IOException {
        appendStock(newStock);
        // Reload to update in-memory list
        loadAllStock();
    }

    /**
     * @deprecated Use deleteById() instead. Kept for backward compatibility.
     */
    @Deprecated
    public void deleteProductFromFile(int productIDToDelete) {
        deleteById(productIDToDelete);
        // Reload to update in-memory list
        loadAllStock();
    }

    /**
     * Saves the current state of the in-memory stocklist back to the file.
     * Used by SalesService to persist stock quantity changes.
     */
    public void saveStockToFile() {
        saveAllStock(stocklist);
    }
}
