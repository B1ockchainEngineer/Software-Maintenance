package assignment.repo;

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
 * Format: subtotal, discount, tax, total (tab-separated)
 */
public class TransactionRepository {
    private static final String TRANSACTION_FILE_PATH = "Transaction.txt";
    private static final Logger LOGGER = Logger.getLogger(TransactionRepository.class.getName());

    /**
     * Represents a transaction record.
     */
    public static class Transaction {
        private final double subtotal;
        private final double discount;
        private final double tax;
        private final double total;

        public Transaction(double subtotal, double discount, double tax, double total) {
            this.subtotal = subtotal;
            this.discount = discount;
            this.tax = tax;
            this.total = total;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public double getDiscount() {
            return discount;
        }

        public double getTax() {
            return tax;
        }

        public double getTotal() {
            return total;
        }
    }

    /**
     * Appends a transaction to the file.
     */
    public void appendTransaction(double subtotal, double discount, double tax, double total) {
        ensureFileExists();
        try (FileWriter writer = new FileWriter(TRANSACTION_FILE_PATH, true)) {
            writer.write(subtotal + "\t");
            writer.write(discount + "\t");
            writer.write(tax + "\t");
            writer.write(total + "\t");
            writer.write("\n");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing transaction", e);
        }
    }

    /**
     * Loads all transactions from the file.
     */
    public List<Transaction> loadAllTransactions() {
        ensureFileExists();
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTION_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                    try {
                        double subtotal = Double.parseDouble(parts[0]);
                        double discount = Double.parseDouble(parts[1]);
                        double tax = Double.parseDouble(parts[2]);
                        double total = Double.parseDouble(parts[3]);
                        transactions.add(new Transaction(subtotal, discount, tax, total));
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.WARNING, "Error parsing transaction line: " + line, e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading transaction file", e);
        }

        return transactions;
    }

    private void ensureFileExists() {
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

