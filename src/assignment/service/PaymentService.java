package assignment.service;

import assignment.model.PaymentResult;
import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.repo.OrderRepository;
import assignment.repo.StockRepository;
import assignment.util.config.TransactionConfig;
import java.util.List;

/**
 * Service layer for payment processing.
 * Handles business logic for payment calculations (subtotal, discount, tax).
 * Does NOT handle transaction persistence - that's handled by TransactionService.
 */
public class PaymentService {
    private final StockRepository stockRepo;
    private final OrderRepository orderRepo;

    public PaymentService(StockRepository stockRepo, OrderRepository orderRepo) {
        this.stockRepo = stockRepo;
        this.orderRepo = orderRepo;
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
        return amountAfterDiscount * TransactionConfig.TAX_RATE;
    }

    /**
     * Calculates payment summary without processing the payment.
     * Used to preview payment details before confirmation.
     * @param discountRate The discount rate to apply (0.0 if no member discount)
     * @return PaymentResult containing calculated payment details (payment not processed)
     */
    public PaymentResult calculatePaymentSummary(double discountRate) {
        List<Stock> cart = stockRepo.getCart();
        
        if (cart.isEmpty()) {
            return null; // No items to calculate
        }

        double subtotal = calculateSubtotal();
        double discount = calculateDiscount(discountRate, subtotal);
        double tax = calculateTax(subtotal, discount);
        double total = subtotal - discount + tax;

        return new PaymentResult(subtotal, discount, tax, total);
    }

    /**
     * Clears the cart and orders after payment is processed.
     * This should be called after a transaction is successfully saved.
     */
    public void clearCart() {
        stockRepo.clearCart();
        // Clear orders from file since they're now part of the transaction
        orderRepo.clearAllOrders();
    }

    /**
     * Creates a Transaction object from the current cart.
     * This calculates payment details but does NOT persist the transaction.
     * Use TransactionService.saveTransaction() to persist.
     * 
     * @param discountRate The discount rate to apply (0.0 if no member discount)
     * @return Transaction object with calculated payment details, or null if cart is empty
     */
    public Transaction createTransaction(double discountRate) {
        List<Stock> cart = stockRepo.getCart();
        
        if (cart.isEmpty()) {
            return null; // No items to process
        }

        double subtotal = calculateSubtotal();
        double discount = calculateDiscount(discountRate, subtotal);
        double tax = calculateTax(subtotal, discount);
        double total = subtotal - discount + tax;

        return new Transaction(subtotal, discount, tax, total, cart);
    }
}

