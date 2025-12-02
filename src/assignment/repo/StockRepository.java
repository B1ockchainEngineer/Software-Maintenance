package assignment.repo;

import assignment.model.Stock;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockRepository {
    private static final Logger LOGGER = Logger.getLogger(StockRepository.class.getName());
    private static final String STOCK_FILE_PATH = "stock.txt";
    // In-memory data structures for fast lookups (Sales logic needs this)
    private static List<Stock> stocklist = new ArrayList<>();
    private static List<Stock> cart = new ArrayList<>();
    private static boolean stockLoaded = false; // Flag to prevent repeated loading

    public List<Stock> getStocklist() {
        return stocklist;
    }

    public List<Stock> getCart() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }

    // Loads stock data from the file into the in-memory stocklist
    public List<Stock> loadStockFromFile() {
        if (stockLoaded) {
            // Return current list if already loaded (prevents duplicate loading on multiple calls)
            return stocklist;
        }

        stocklist.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(STOCK_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                    int stockId = Integer.parseInt(parts[0]);
                    String stockName = parts[1];
                    int stockQty = Integer.parseInt(parts[2]);
                    double stockPrice = Double.parseDouble(parts[3]);
                    stocklist.add(new Stock(stockId, stockName, stockQty, stockPrice));
                }
            }
            stockLoaded = true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading stock file. Creating new file if necessary", e);
            // Attempt to create file if it doesn't exist
            try {
                new File(STOCK_FILE_PATH).createNewFile();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Fatal: Could not create stock file.", ex);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error parsing stock data in file", e);
        }
        return stocklist;
    }

    // Finds the largest Stock ID currently in the file
    public int findLastStockID() {
        int lastStockID = 10000;
        try (BufferedReader reader = new BufferedReader(new FileReader(STOCK_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length > 0) {
                    try {
                        int stockID = Integer.parseInt(parts[0]);
                        if (stockID > lastStockID) {
                            lastStockID = stockID;
                        }
                    } catch (NumberFormatException ignored) {
                        // Ignore lines with invalid ID format
                    }
                }
            }
        } catch (IOException e) {
            return 10000; // File error, return base ID
        }
        return lastStockID;
    }

    // Checks if a product name already exists in the file (reads directly from file for safety)
    public boolean checkNameExists(String name) {
        String upperName = name.toUpperCase();
        try (Scanner fileScanner = new Scanner(new File(STOCK_FILE_PATH))) {
            while (fileScanner.hasNextLine()) {
                String stockLine = fileScanner.nextLine();
                String[] stockDetails = stockLine.split("\t");
                if (stockDetails.length >= 2) {
                    String searchStockName = stockDetails[1];
                    if (searchStockName.equals(upperName)) {
                        return true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }

    // Writes a new stock item to the file
    public void addStockToFile(Stock newStock) throws IOException {
        File stockFile = new File(STOCK_FILE_PATH);
        if (!stockFile.exists()) {
            stockFile.createNewFile();
        }

        try (FileWriter writer = new FileWriter(STOCK_FILE_PATH, true)) {
            writer.write(newStock.toFileString() + "\n");
        }

        // Update the in-memory list as well
        // We reload the stock list to ensure ID consistency after file write
        loadStockFromFile();
    }

    // Deletes a product by ID by rewriting the file
    public void deleteProductFromFile(int productIDToDelete) {
        File inputFile = new File(STOCK_FILE_PATH);
        File tempFile = new File("dltStkTemp.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                if (data.length > 0) {
                    try {
                        int productID = Integer.parseInt(data[0]);
                        if (productID == productIDToDelete) {
                            continue;
                        }
                        writer.write(line + System.getProperty("line.separator"));
                    } catch (NumberFormatException ignored) {
                        writer.write(line + System.getProperty("line.separator"));
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "ERROR during file deletion process", e);
        }

        // Rename the temp file
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                LOGGER.severe("Error renaming the temp file.");
            }
        }

        // Update the in-memory list
        loadStockFromFile();
    }

    // Saves the current state of the in-memory stocklist back to the file
    public void saveStockToFile() {
        File inputFile = new File(STOCK_FILE_PATH);
        File tempFile = new File("stkUpdateTemp.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (Stock stock : stocklist) {
                writer.write(stock.toFileString() + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "ERROR saving stock updates to file", e);
            return;
        }

        // Rename the temp file
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                LOGGER.severe("Error renaming the stock update file.");
            }
        }
    }
}