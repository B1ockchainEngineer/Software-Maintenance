package assignment.repo;

import assignment.model.Order;
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
 *   ORDER \t stockID \t stockName \t qty \t price
 * Note: Order numbers are determined by line position (1, 2, 3, ...)
 */
public class OrderRepository {
    private static final String DATA_DIR = "data/";
    private static final String ORDER_FILE_PATH = DATA_DIR + "order.txt";
    private final Logger logger = Logger.getLogger(OrderRepository.class.getName());
    private static final String ORDER_MARKER = "ORDER";

    /**
     * Appends an order to the file.
     * Order number is determined by line position (not stored in file).
     * @param order The order to save
     */
    public void appendOrder(Order order) {
        ensureFileExists();
        try (FileWriter writer = new FileWriter(ORDER_FILE_PATH, true)) {
            writer.write(ORDER_MARKER + "\t");
            writer.write(order.getStockID() + "\t");
            writer.write(order.getStockName() + "\t");
            writer.write(order.getQuantity() + "\t");
            writer.write(order.getPrice() + "\t");
            writer.write("\n");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing order", e);
        }
    }

    /**
     * Appends multiple orders to the file.
     * Order numbers are determined by line position (not stored in file).
     * @param orders List of orders to save
     */
    public void appendOrders(List<Order> orders) {
        ensureFileExists();
        try (FileWriter writer = new FileWriter(ORDER_FILE_PATH, true)) {
            for (Order order : orders) {
                writer.write(ORDER_MARKER + "\t");
                writer.write(order.getStockID() + "\t");
                writer.write(order.getStockName() + "\t");
                writer.write(order.getQuantity() + "\t");
                writer.write(order.getPrice() + "\t");
                writer.write("\n");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing orders", e);
        }
    }

    /**
     * Loads all orders from the file.
     * Order numbers are assigned based on line position (1, 2, 3, ...).
     * @return List of all orders with orderNo assigned by position
     */
    public List<Order> loadAllOrders() {
        ensureFileExists();
        List<Order> orders = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ORDER_FILE_PATH))) {
            String line;
            int orderNo = 1; // First order is 1, second is 2, etc.
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                String[] parts = line.split("\t");
                
                // New format: ORDER \t stockID \t stockName \t quantity \t price (5 parts)
                // Old format (backward compatibility): ORDER \t orderNo \t stockID \t stockName \t qty \t price (6 parts)
                if (parts.length >= 5 && parts[0].equals(ORDER_MARKER)) {
                    try {
                        int stockID;
                        String stockName;
                        int quantity;
                        double price;
                        
                        if (parts.length >= 6) {
                            // Old format with orderNo - ignore stored orderNo, use position instead
                            stockID = Integer.parseInt(parts[2]);
                            stockName = parts[3];
                            quantity = Integer.parseInt(parts[4]);
                            price = Double.parseDouble(parts[5]);
                        } else {
                            // New format without orderNo
                            stockID = Integer.parseInt(parts[1]);
                            stockName = parts[2];
                            quantity = Integer.parseInt(parts[3]);
                            price = Double.parseDouble(parts[4]);
                        }
                        
                        // Assign orderNo based on position in file
                        orders.add(new Order(orderNo, stockID, stockName, quantity, price));
                        orderNo++;
                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, "Error parsing order line: " + line, e);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading order file", e);
        }

        return orders;
    }

    /**
     * Updates an order in the file by order number (position-based).
     * Uses temporary file approach for safe update.
     * @param order The updated order (orderNo represents position: 1, 2, 3, ...)
     * @return true if order was found and updated, false otherwise
     */
    public boolean updateOrder(Order order) {
        ensureFileExists();
        File inputFile = new File(ORDER_FILE_PATH);
        File tempFile = new File(DATA_DIR + "temp/orderUpdateTemp.txt");
        
        // Ensure temp directory exists
        File tempDir = new File(DATA_DIR + "temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        boolean orderFound = false;
        int currentPosition = 1; // Track position in file

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(tempFile)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    writer.write(line + "\n");
                    continue;
                }

                String[] parts = line.split("\t");
                if (parts.length >= 5 && parts[0].equals(ORDER_MARKER)) {
                    // Check if this is the order to update (by position)
                    if (currentPosition == order.getOrderNo()) {
                        // Write updated order (without orderNo)
                        writer.write(ORDER_MARKER + "\t");
                        writer.write(order.getStockID() + "\t");
                        writer.write(order.getStockName() + "\t");
                        writer.write(order.getQuantity() + "\t");
                        writer.write(order.getPrice() + "\t");
                        writer.write("\n");
                        orderFound = true;
                        currentPosition++;
                        continue;
                    }
                    currentPosition++;
                }
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error updating order", e);
            return false;
        }

        if (orderFound) {
            // Replace original file with temp file
            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                return true;
            } else {
                logger.log(Level.SEVERE, "Error replacing order file after update");
                return false;
            }
        }

        // Order not found, delete temp file
        tempFile.delete();
        return false;
    }

    /**
     * Deletes an order from the file by order number (position-based).
     * Uses temporary file approach for safe deletion.
     * @param orderNo The order number (position: 1, 2, 3, ...) to delete
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
        int currentPosition = 1; // Track position in file

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(tempFile)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    writer.write(line + "\n");
                    continue;
                }

                String[] parts = line.split("\t");
                if (parts.length >= 5 && parts[0].equals(ORDER_MARKER)) {
                    // Check if this is the order to delete (by position)
                    if (currentPosition == orderNo) {
                        orderFound = true;
                        // Skip this line (delete it)
                        currentPosition++;
                        continue;
                    }
                    currentPosition++;
                }
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error deleting order", e);
            return false;
        }

        if (orderFound) {
            // Replace original file with temp file
            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                return true;
            } else {
                logger.log(Level.SEVERE, "Error replacing order file after deletion");
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
            logger.log(Level.SEVERE, "Error clearing orders file", e);
        }
    }

    /**
     * Gets the count of orders in the file.
     * Used to determine the next order number (count + 1).
     * @return The number of orders, or 0 if no orders exist
     */
    public int getOrderCount() {
        List<Order> orders = loadAllOrders();
        return orders.size();
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
                logger.log(Level.SEVERE, "Error creating order file", e);
            }
        }
    }
}

