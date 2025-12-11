package assignment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Test fixture for creating Transaction objects in tests.
 * Provides factory methods and builders for easy test data creation.
 */
public class TransactionModelTest {
    
    /**
     * Creates a default test transaction with standard values.
     * @return Transaction with subtotal=200.0, discount=20.0, tax=10.8, total=190.8, 2 items
     */
    public static Transaction createDefault() {
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", 2, 50.0));
        items.add(new Stock(1002, "Product B", 1, 100.0));
        return new Transaction(200.0, 20.0, 10.8, 190.8, items);
    }
    
    /**
     * Creates a test transaction with specified values.
     * @param subtotal The subtotal amount
     * @param discount The discount amount
     * @param tax The tax amount
     * @param total The total amount
     * @param items The list of items in the transaction
     * @return Transaction with specified values
     */
    public static Transaction create(double subtotal, double discount, double tax, double total, List<Stock> items) {
        return new Transaction(subtotal, discount, tax, total, items);
    }
    
    /**
     * Creates a test transaction with no discount.
     * @param subtotal The subtotal amount
     * @return Transaction with no discount (discount=0.0, tax=6% of subtotal, total=subtotal+tax)
     */
    public static Transaction createWithoutDiscount(double subtotal) {
        double tax = subtotal * 0.06;
        double total = subtotal + tax;
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", (int)(subtotal / 50.0), 50.0));
        return new Transaction(subtotal, 0.0, tax, total, items);
    }
    
    /**
     * Creates a test transaction with discount.
     * @param subtotal The subtotal amount
     * @param discountRate The discount rate (e.g., 0.10 for 10%)
     * @return Transaction with discount applied
     */
    public static Transaction createWithDiscount(double subtotal, double discountRate) {
        double discount = subtotal * discountRate;
        double amountAfterDiscount = subtotal - discount;
        double tax = amountAfterDiscount * 0.06;
        double total = amountAfterDiscount + tax;
        
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(1001, "Product A", (int)(subtotal / 50.0), 50.0));
        return new Transaction(subtotal, discount, tax, total, items);
    }
    
    /**
     * Creates an empty transaction (no items).
     * @return Transaction with all values set to 0.0 and empty items list
     */
    public static Transaction createEmpty() {
        return new Transaction(0.0, 0.0, 0.0, 0.0, new ArrayList<>());
    }
    
    /**
     * Creates a test transaction with a single item.
     * @param stockID The stock ID of the item
     * @param stockName The stock name
     * @param quantity The quantity
     * @param price The price per unit
     * @param discountRate The discount rate (e.g., 0.10 for 10%)
     * @return Transaction with single item
     */
    public static Transaction createSingleItem(int stockID, String stockName, int quantity, double price, double discountRate) {
        double subtotal = quantity * price;
        double discount = subtotal * discountRate;
        double amountAfterDiscount = subtotal - discount;
        double tax = amountAfterDiscount * 0.06;
        double total = amountAfterDiscount + tax;
        
        List<Stock> items = new ArrayList<>();
        items.add(new Stock(stockID, stockName, quantity, price));
        return new Transaction(subtotal, discount, tax, total, items);
    }
    
    /**
     * Builder class for creating Transaction objects with fluent API.
     */
    public static class Builder {
        private double subtotal = 200.0;
        private double discount = 20.0;
        private double tax = 10.8;
        private double total = 190.8;
        private List<Stock> items = new ArrayList<>();
        
        public Builder withSubtotal(double subtotal) {
            this.subtotal = subtotal;
            return this;
        }
        
        public Builder withDiscount(double discount) {
            this.discount = discount;
            return this;
        }
        
        public Builder withTax(double tax) {
            this.tax = tax;
            return this;
        }
        
        public Builder withTotal(double total) {
            this.total = total;
            return this;
        }
        
        public Builder withItems(List<Stock> items) {
            this.items = new ArrayList<>(items);
            return this;
        }
        
        public Builder addItem(Stock item) {
            this.items.add(item);
            return this;
        }
        
        public Builder calculateTax() {
            double amountAfterDiscount = subtotal - discount;
            this.tax = amountAfterDiscount * 0.06;
            this.total = amountAfterDiscount + tax;
            return this;
        }
        
        public Transaction build() {
            return new Transaction(subtotal, discount, tax, total, items);
        }
    }
    
    /**
     * Creates a new builder instance.
     * @return Builder for creating Transaction objects
     */
    public static Builder builder() {
        return new Builder();
    }
}

