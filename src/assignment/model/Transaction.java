package assignment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a transaction with its items.
 */
public class Transaction {
    private final double subtotal;
    private final double discount;
    private final double tax;
    private final double total;
    private final List<Stock> items;

    public Transaction(double subtotal, double discount, double tax, double total, List<Stock> items) {
        this.subtotal = subtotal;
        this.discount = discount;
        this.tax = tax;
        this.total = total;
        this.items = new ArrayList<>(items);
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

    public List<Stock> getItems() {
        return new ArrayList<>(items);
    }
}






