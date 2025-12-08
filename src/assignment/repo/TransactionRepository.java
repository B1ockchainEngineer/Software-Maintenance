package assignment.repo;

import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.util.config.AppConfig;
import assignment.util.config.SalesConfig;
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
 * Repository responsible for reading/writing transactions to Transaction.txt.
 * This is a data access layer - only handles file I/O operations.
 * Format: 
 *   transaction \t subtotal \t discount \t tax \t total
 *   ITEM \t stockID \t stockName \t qty \t price
 *   ITEM \t stockID \t stockName \t qty \t price
 *   ...
 */
public class TransactionRepository {
    private static final String TRANSACTION_FILE_PATH = SalesConfig.TRANSACTION_FILE_PATH;
    private static final Logger LOGGER = Logger.getLogger(TransactionRepository.class.getName());

    /**
     * Appends a transaction with its items to the file.
     * Format: transaction header followed by ITEM lines
     * @param subtotal The subtotal amount
     * @param discount The discount amount
     * @param tax The tax amount
     * @param total The total amount
     * @param items The list of items in this transaction
     */
    public void appendTransaction(double subtotal, double discount, double tax, double total, List<Stock> items) {
        ensureFileExists();
        try (FileWriter writer = new FileWriter(TRANSACTION_FILE_PATH, true)) {
            // Write transaction header
            writer.write(SalesConfig.FILE_MARKER_TRANSACTION + "\t");
            writer.write(subtotal + "\t");
            writer.write(discount + "\t");
            writer.write(tax + "\t");
            writer.write(total + "\t");
            writer.write("\n");
            
            // Write all items for this transaction
            for (Stock item : items) {
                writer.write("ITEM\t");
                writer.write(item.getStockID() + "\t");
                writer.write(item.getStockName() + "\t");
                writer.write(item.getQty() + "\t");
                writer.write(item.getPrice() + "\t");
                writer.write("\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing transaction", e);
        }
    }

    /**
     * Loads all transactions with their items from the file.
     * Returns Transaction model objects.
     */
    public List<Transaction> loadAllTransactions() {
        ensureFileExists();
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTION_FILE_PATH))) {
            TransactionBuilder builder = new TransactionBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                String[] parts = line.split("\t");
                
                if (isTransactionHeader(parts)) {
                    // Save previous transaction if exists
                    Transaction completed = builder.buildAndReset();
                    if (completed != null) {
                        transactions.add(completed);
                    }
                    // Start new transaction
                    parseTransactionHeader(parts, builder);
                } else if (isItemLine(parts)) {
                    parseItemLine(parts, builder);
                } else if (isOldFormatTransaction(parts)) {
                    // Save previous transaction if exists
                    Transaction completed = builder.buildAndReset();
                    if (completed != null) {
                        transactions.add(completed);
                    }
                    // Parse old format (no items)
                    parseOldFormatTransaction(parts, builder);
                    Transaction oldTransaction = builder.buildAndReset();
                    if (oldTransaction != null) {
                        transactions.add(oldTransaction);
                    }
                }
            }

            // Don't forget the last transaction
            Transaction lastTransaction = builder.buildAndReset();
            if (lastTransaction != null) {
                transactions.add(lastTransaction);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading transaction file", e);
        }

        return transactions;
    }


    private boolean isTransactionHeader(String[] parts) {
        return parts.length >= 5 && 
               (parts[0].equals(SalesConfig.FILE_MARKER_TRANSACTION) ||
                parts[0].equals("TRANSACTION")); // Backward compatibility
    }

    private boolean isItemLine(String[] parts) {
        return parts.length >= 5 && parts[0].equals("ITEM");
    }

    private boolean isOldFormatTransaction(String[] parts) {
        return parts.length >= 4 && 
               !parts[0].equals("ITEM") && 
               !parts[0].equals(SalesConfig.FILE_MARKER_TRANSACTION) &&
               !parts[0].equals("TRANSACTION");
    }

    private void parseTransactionHeader(String[] parts, TransactionBuilder builder) {
        try {
            double subtotal = Double.parseDouble(parts[1]);
            double discount = Double.parseDouble(parts[2]);
            double tax = Double.parseDouble(parts[3]);
            double total = Double.parseDouble(parts[4]);
            builder.setAmounts(subtotal, discount, tax, total);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Error parsing transaction header: " + String.join("\t", parts), e);
        }
    }

    private void parseItemLine(String[] parts, TransactionBuilder builder) {
        try {
            int stockID = Integer.parseInt(parts[1]);
            String stockName = parts[2];
            int qty = Integer.parseInt(parts[3]);
            double price = Double.parseDouble(parts[4]);
            builder.addItem(new Stock(stockID, stockName, qty, price));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Error parsing item line: " + String.join("\t", parts), e);
        }
    }

    private void parseOldFormatTransaction(String[] parts, TransactionBuilder builder) {
        try {
            double subtotal = Double.parseDouble(parts[0]);
            double discount = Double.parseDouble(parts[1]);
            double tax = Double.parseDouble(parts[2]);
            double total = Double.parseDouble(parts[3]);
            builder.setAmounts(subtotal, discount, tax, total);
            // Old format has no items, so items list remains empty
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Error parsing old format transaction: " + String.join("\t", parts), e);
        }
    }

    private void ensureFileExists() {
        // Ensure data directory exists
        File dataDir = new File(AppConfig.DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Ensure file exists
        File file = new File(TRANSACTION_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error creating transaction file", e);
            }
        }
    }
}

