package assignment.model;

/**
 * Model class representing the result of a payment calculation.
 * Contains all payment details (subtotal, discount, tax, total).
 */
public class PaymentResult {
    private final double subtotal;
    private final double discount;
    private final double tax;
    private final double total;

    public PaymentResult(double subtotal, double discount, double tax, double total) {
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






