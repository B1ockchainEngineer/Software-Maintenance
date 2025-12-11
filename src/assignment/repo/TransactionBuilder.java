package assignment.repo;

import assignment.model.Stock;
import assignment.model.Transaction;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for constructing Transaction objects during file parsing.
 * This is a helper class used by TransactionRepository for parsing transaction data.
 * 
 * Pattern: Builder Pattern (variant for sequential parsing)
 * - Accumulates state during parsing
 * - Builds Transaction objects
 * - Resets for reuse
 */
class TransactionBuilder {
    private double subtotal = 0.0;
    private double discount = 0.0;
    private double tax = 0.0;
    private double total = 0.0;
    private List<Stock> items = new ArrayList<>();
    private boolean hasData = false;

    /**
     * Sets the transaction amounts (subtotal, discount, tax, total).
     * @param subtotal The subtotal amount
     * @param discount The discount amount
     * @param tax The tax amount
     * @param total The total amount
     */
    void setAmounts(double subtotal, double discount, double tax, double total) {
        this.subtotal = subtotal;
        this.discount = discount;
        this.tax = tax;
        this.total = total;
        this.hasData = true;
    }

    /**
     * Adds an item to the transaction.
     * @param item The stock item to add
     */
    void addItem(Stock item) {
        this.items.add(item);
    }

    /**
     * Builds a Transaction object and resets the builder for reuse.
     * @return The constructed Transaction, or null if no data was set
     */
    Transaction buildAndReset() {
        if (!hasData && items.isEmpty()) {
            return null;
        }
        Transaction transaction = new Transaction(subtotal, discount, tax, total, items);
        reset();
        return transaction;
    }

    /**
     * Resets the builder to its initial state for reuse.
     */
    private void reset() {
        subtotal = 0.0;
        discount = 0.0;
        tax = 0.0;
        total = 0.0;
        items = new ArrayList<>();
        hasData = false;
    }
}







