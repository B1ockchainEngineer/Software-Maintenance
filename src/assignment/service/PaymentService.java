package assignment.service;

import assignment.model.Stock;
import assignment.repo.PaidItemRepository;
import assignment.repo.StockRepository;
import assignment.repo.TransactionRepository;
import java.util.List;

/**
 * Service layer for payment processing.
 * Handles business logic for payments, discounts, taxes, and transaction recording.
 */
public class PaymentService {
    private final StockRepository stockRepo;
    private final PaidItemRepository paidItemRepo;
    private final TransactionRepository transactionRepo;
    private static final double TAX_RATE = 0.06; // 6% tax rate

    public PaymentService(StockRepository stockRepo, PaidItemRepository paidItemRepo, TransactionRepository transactionRepo) {
        this.stockRepo = stockRepo;
        this.paidItemRepo = paidItemRepo;
        this.transactionRepo = transactionRepo;
    }

    /**
     * Calculates the subtotal of all items in the cart.
     */
    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (Stock item : stockRepo.getCart()) {
            subtotal += item.calculateTotalCost();
        }
        return subtotal;
    }

    /**
     * Calculates discount based on member type and subtotal.
     * @param discountRate The discount rate (e.g., 0.1 for 10%)
     * @param subtotal The subtotal amount
     * @return The discount amount
     */
    public double calculateDiscount(double discountRate, double subtotal) {
        return subtotal * discountRate;
    }

    /**
     * Calculates tax on the subtotal after discount.
     * @param subtotal The subtotal amount
     * @param discount The discount amount
     * @return The tax amount
     */
    public double calculateTax(double subtotal, double discount) {
        double amountAfterDiscount = subtotal - discount;
        return amountAfterDiscount * TAX_RATE;
    }

    /**
     * Processes payment for all items in the cart.
     * @param discountRate The discount rate to apply (0.0 if no member discount)
     * @return PaymentResult containing all payment details
     */
    public PaymentResult processPayment(double discountRate) {
        List<Stock> cart = stockRepo.getCart();
        
        if (cart.isEmpty()) {
            return null; // No items to pay for
        }

        double subtotal = calculateSubtotal();
        double discount = calculateDiscount(discountRate, subtotal);
        double tax = calculateTax(subtotal, discount);
        double total = subtotal - discount + tax;

        // Write paid items to file
        paidItemRepo.appendPaidItems(cart);

        // Write transaction to file
        transactionRepo.appendTransaction(subtotal, discount, tax, total);

        // Clear the cart after payment
        stockRepo.clearCart();

        return new PaymentResult(subtotal, discount, tax, total);
    }

    /**
     * Result object containing payment calculation details.
     */
    public static class PaymentResult {
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
}

