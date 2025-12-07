package assignment.repo;

import assignment.model.Stock;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository responsible for reading/writing orders to order.txt.
 * This is a data access layer - only handles file I/O operations.
 * Format: 
 *   ORDER \t orderNo \t stockID \t stockName \t qty \t price
 */
public class OrderRepository {
    private static final String DATA_DIR = "data/";
    private static final String ORDER_FILE_PATH = DATA_DIR + "order.txt";
    private static final Logger LOGGER = Logger.getLogger(OrderRepository.class.getName());
    private static final String ORDER_MARKER = "ORDER";

    /**
     * Appends an order to the file.
     * @param order The order (Stock item with orderNo) to save
     */
    public void appendOrder(Stock order) {
        ensureFileExists();
        try (FileWriter writer = new FileWriter(ORDER_FILE_PATH, true)) {
            writer.write(ORDER_MARKER + "\t");
            writer.write(order.getOrderNo() + "\t");
            writer.write(order.getStockID() + "\t");
            writer.write(order.getStockName() + "\t");
            writer.write(order.getQty() + "\t");
            writer.write(order.getPrice() + "\t");
            writer.write("\n");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing order", e);
        }
    }

    /**
     * Appends multiple orders to the file.
     * @param orders List of orders to save
     */
    public void appendOrders(List<Stock> orders) {
        ensureFileExists();
        try (FileWriter writer = new FileWriter(ORDER_FILE_PATH, true)) {
            for (Stock order : orders) {
                writer.write(ORDER_MARKER + "\t");
                writer.write(order.getOrderNo() + "\t");
                writer.write(order.getStockID() + "\t");
                writer.write(order.getStockName() + "\t");
                writer.write(order.getQty() + "\t");
                writer.write(order.getPrice() + "\t");
                writer.write("\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing orders", e);
        }
    }

    /**
     * Loads all orders from the file.
     * @return List of all orders
     */
    public List<Stock> loadAllOrders() {
        ensureFileExists();
        List<Stock> orders = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ORDER_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                String[] parts = line.split("\t");
                
                if (parts.length >= 6 && parts[0].equals(ORDER_MARKER)) {
                    try {
                        int orderNo = Integer.parseInt(parts[1]);
                        int stockID = Integer.parseInt(parts[2]);
                        String stockName = parts[3];
                        int qty = Integer.parseInt(parts[4]);
                        double price = Double.parseDouble(parts[5]);
                        orders.add(new Stock(orderNo, stockID, stockName, qty, price));
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.WARNING, "Error parsing order line: " + line, e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading order file", e);
        }

        return orders;
    }

    /**
     * Updates an order in the file by order number.
     * Uses temporary file approach for safe update.
     * @param order The updated order
     * @return true if order was found and updated, false otherwise
     */
    public boolean updateOrder(Stock order) {
        ensureFileExists();
        File inputFile = new File(ORDER_FILE_PATH);
        File tempFile = new File(DATA_DIR + "temp/orderUpdateTemp.txt");
        
        // Ensure temp directory exists
        File tempDir = new File(DATA_DIR + "temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        boolean orderFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(tempFile)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    writer.write(line + "\n");
                    continue;
                }

                String[] parts = line.split("\t");
                if (parts.length >= 6 && parts[0].equals(ORDER_MARKER)) {
                    try {
                        int currentOrderNo = Integer.parseInt(parts[1]);
                        if (currentOrderNo == order.getOrderNo()) {
                            // Write updated order
                            writer.write(ORDER_MARKER + "\t");
                            writer.write(order.getOrderNo() + "\t");
                            writer.write(order.getStockID() + "\t");
                            writer.write(order.getStockName() + "\t");
                            writer.write(order.getQty() + "\t");
                            writer.write(order.getPrice() + "\t");
                            writer.write("\n");
                            orderFound = true;
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        // Invalid line, keep it as is
                    }
                }
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error updating order", e);
            return false;
        }

        if (orderFound) {
            // Replace original file with temp file
            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                return true;
            } else {
                LOGGER.log(Level.SEVERE, "Error replacing order file after update");
                return false;
            }
        }

        // Order not found, delete temp file
        tempFile.delete();
        return false;
    }

    /**
     * Deletes an order from the file by order number.
     * Uses temporary file approach for safe deletion.
     * @param orderNo The order number to delete
     * @return true if order was found and deleted, false otherwise
     */
    public boolean deleteOrder(int orderNo) {
        ensureFileExists();
        File inputFile = new File(ORDER_FILE_PATH);
        File tempFile = new File(DATA_DIR + "temp/orderDeleteTemp.txt");
        
        // Ensure temp directory exists
        File tempDir = new File(DATA_DIR + "temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        boolean orderFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(tempFile)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    writer.write(line + "\n");
                    continue;
                }

                String[] parts = line.split("\t");
                if (parts.length >= 6 && parts[0].equals(ORDER_MARKER)) {
                    try {
                        int currentOrderNo = Integer.parseInt(parts[1]);
                        if (currentOrderNo == orderNo) {
                            orderFound = true;
                            // Skip this line (delete it)
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        // Invalid line, keep it
                    }
                }
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deleting order", e);
            return false;
        }

        if (orderFound) {
            // Replace original file with temp file
            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                return true;
            } else {
                LOGGER.log(Level.SEVERE, "Error replacing order file after deletion");
                return false;
            }
        }

        // Order not found, delete temp file
        tempFile.delete();
        return false;
    }

    /**
     * Clears all orders from the file.
     * Useful when cart is cleared after payment.
     */
    public void clearAllOrders() {
        ensureFileExists();
        try (FileWriter writer = new FileWriter(ORDER_FILE_PATH, false)) {
            // Open in overwrite mode (false) - clears file
            writer.write("");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error clearing orders file", e);
        }
    }

    /**
     * Gets the highest order number from the file.
     * Used to ensure unique order numbers.
     * @return The highest order number, or 0 if no orders exist
     */
    public int getLastOrderNo() {
        List<Stock> orders = loadAllOrders();
        if (orders.isEmpty()) {
            return 0;
        }
        
        int maxOrderNo = 0;
        for (Stock order : orders) {
            if (order.getOrderNo() > maxOrderNo) {
                maxOrderNo = order.getOrderNo();
            }
        }
        return maxOrderNo;
    }

    private void ensureFileExists() {
        // Ensure data directory exists
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Ensure file exists
        File file = new File(ORDER_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error creating order file", e);
            }
        }
    }
}

