package assignment.repo;

import assignment.model.Stock;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository responsible for writing paid items to PaidItem.txt.
 */
public class PaidItemRepository {
    private static final String PAID_ITEM_FILE_PATH = "PaidItem.txt";
    private static final Logger LOGGER = Logger.getLogger(PaidItemRepository.class.getName());

    /**
     * Appends a paid item to the file.
     */
    public void appendPaidItem(Stock item) {
        ensureFileExists();
        try (FileWriter writer = new FileWriter(PAID_ITEM_FILE_PATH, true)) {
            writer.write(item.getStockID() + "\t");
            writer.write(item.getStockName() + "\t");
            writer.write(item.getQty() + "\t");
            writer.write(item.getPrice() + "\t");
            writer.write("\n");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing paid item", e);
        }
    }

    /**
     * Appends multiple paid items to the file.
     */
    public void appendPaidItems(java.util.List<Stock> items) {
        for (Stock item : items) {
            appendPaidItem(item);
        }
    }

    private void ensureFileExists() {
        File file = new File(PAID_ITEM_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error creating paid item file", e);
            }
        }
    }
}

