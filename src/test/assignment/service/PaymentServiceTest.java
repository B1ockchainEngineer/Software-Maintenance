package test.assignment.service;

import assignment.model.Order;
import assignment.model.PaymentResult;
import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.repo.OrderRepository;
import assignment.repo.StockRepository;
import assignment.repo.TransactionRepository;
import assignment.service.PaymentService;
import assignment.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PaymentService.
 * Tests payment calculations, transaction creation, and cart operations.
 */
@DisplayName("PaymentService Tests")
class PaymentServiceTest {
    private static final Logger LOGGER = Logger.getLogger(PaymentServiceTest.class.getName());

    private StockRepository stockRepo;
    private OrderRepository orderRepo;
    private TransactionRepository transactionRepo;
    private PaymentService paymentService;
    private TransactionService transactionService;
    private List<Transaction> savedTransactions;

    @BeforeEach
    void setUp() {
        // Setup mock StockRepository
        stockRepo = new StockRepository() {
            private final List<Order> cart = new ArrayList<>();

            @Override
            public List<Order> getCart() {
                return cart;
            }

            @Override
            public void clearCart() {
                cart.clear();
            }
        };

        // Setup mock OrderRepository
        orderRepo = new OrderRepository() {
            @Override
            public void clearAllOrders() {
                // Mock - do nothing
            }
        };

        // Setup mock TransactionRepository for integrated tests
        savedTransactions = new ArrayList<>();
        transactionRepo = new TransactionRepository() {
            @Override
            public void appendTransaction(double subtotal, double discount, double tax, 
                                        double total, List<Stock> items) {
                // Mock: Store transaction in memory
                List<Stock> itemsCopy = new ArrayList<>(items);
                Transaction transaction = new Transaction(subtotal, discount, tax, total, itemsCopy);
                savedTransactions.add(transaction);
            }

            @Override
            public List<Transaction> loadAllTransactions() {
                return new ArrayList<>(savedTransactions);
            }
        };

        paymentService = new PaymentService(stockRepo, orderRepo);
        transactionService = new TransactionService(transactionRepo);
    }

    @Test
    @DisplayName("Should calculate subtotal for single item")
    void testCalculateSubtotal_SingleItem() {
        Order item = new Order(1, 1001, "Product A", 2, 50.0);
        stockRepo.getCart().add(item);

        double subtotal = paymentService.calculateSubtotal();
        assertEquals(100.0, subtotal, 0.01); // 2 * 50.0
        LOGGER.info("✓ SUCCESS: PaymentService - Calculated subtotal for single item (2 × RM50.00 = RM100.00)");
    }

    @Test
    @DisplayName("Should calculate subtotal for multiple items")
    void testCalculateSubtotal_MultipleItems() {
        stockRepo.getCart().add(new Order(1, 1001, "Product A", 2, 50.0));  // 100.0
        stockRepo.getCart().add(new Order(2, 1002, "Product B", 1, 100.0)); // 100.0
        stockRepo.getCart().add(new Order(3, 1003, "Product C", 3, 25.0));  // 75.0

        double subtotal = paymentService.calculateSubtotal();
        assertEquals(275.0, subtotal, 0.01); // 100 + 100 + 75
        LOGGER.info("✓ SUCCESS: PaymentService - Calculated subtotal for multiple items (RM100.00 + RM100.00 + RM75.00 = RM275.00)");
    }

    @Test
    @DisplayName("Should return zero subtotal for empty cart")
    void testCalculateSubtotal_EmptyCart() {
        double subtotal = paymentService.calculateSubtotal();
        assertEquals(0.0, subtotal, 0.01);
        LOGGER.info("✓ SUCCESS: PaymentService - Returned zero subtotal for empty cart");
    }

    @Test
    @DisplayName("Should calculate discount correctly")
    void testCalculateDiscount() {
        double subtotal = 100.0;
        double discountRate = 0.10; // 10%

        double discount = paymentService.calculateDiscount(discountRate, subtotal);
        assertEquals(10.0, discount, 0.01);
        LOGGER.info("✓ SUCCESS: PaymentService - Calculated discount correctly (10% of RM100.00 = RM10.00)");
    }

    @Test
    @DisplayName("Should calculate zero discount when rate is zero")
    void testCalculateDiscount_ZeroRate() {
        double subtotal = 100.0;
        double discount = paymentService.calculateDiscount(0.0, subtotal);
        assertEquals(0.0, discount, 0.01);
        LOGGER.info("✓ SUCCESS: PaymentService - Calculated zero discount when rate is zero");
    }

    @Test
    @DisplayName("Should calculate discount for premium member (15%)")
    void testCalculateDiscount_PremiumMember() {
        double subtotal = 200.0;
        double discountRate = 0.15; // 15% premium

        double discount = paymentService.calculateDiscount(discountRate, subtotal);
        assertEquals(30.0, discount, 0.01);
        LOGGER.info("✓ SUCCESS: PaymentService - Calculated premium member discount (15% of RM200.00 = RM30.00)");
    }

    @Test
    @DisplayName("Should calculate tax correctly")
    void testCalculateTax() {
        double subtotal = 100.0;
        double discount = 10.0;
        // Tax rate is 6% (0.06)
        // Amount after discount: 100 - 10 = 90
        // Tax: 90 * 0.06 = 5.4

        double tax = paymentService.calculateTax(subtotal, discount);
        assertEquals(5.4, tax, 0.01);
        LOGGER.info("✓ SUCCESS: PaymentService - Calculated tax correctly (6% of RM90.00 after discount = RM5.40)");
    }

    @Test
    @DisplayName("Should calculate tax with no discount")
    void testCalculateTax_NoDiscount() {
        double subtotal = 100.0;
        double discount = 0.0;
        // Tax: 100 * 0.06 = 6.0

        double tax = paymentService.calculateTax(subtotal, discount);
        assertEquals(6.0, tax, 0.01);
        LOGGER.info("✓ SUCCESS: PaymentService - Calculated tax with no discount (6% of RM100.00 = RM6.00)");
    }

    @Test
    @DisplayName("Should calculate payment summary correctly")
    void testCalculatePaymentSummary() {
        stockRepo.getCart().add(new Order(1, 1001, "Product A", 2, 50.0)); // 100.0
        stockRepo.getCart().add(new Order(2, 1002, "Product B", 1, 100.0)); // 100.0
        // Subtotal: 200.0
        // Discount (10%): 20.0
        // Amount after discount: 180.0
        // Tax (6%): 10.8
        // Total: 180.0 + 10.8 = 190.8

        PaymentResult result = paymentService.calculatePaymentSummary(0.10);

        assertNotNull(result);
        assertEquals(200.0, result.getSubtotal(), 0.01);
        assertEquals(20.0, result.getDiscount(), 0.01);
        assertEquals(10.8, result.getTax(), 0.01);
        assertEquals(190.8, result.getTotal(), 0.01);
        LOGGER.info("✓ SUCCESS: PaymentService - Calculated payment summary correctly (Subtotal: RM200.00, Discount: RM20.00, Tax: RM10.80, Total: RM190.80)");
    }

    @Test
    @DisplayName("Should return null payment summary for empty cart")
    void testCalculatePaymentSummary_EmptyCart() {
        PaymentResult result = paymentService.calculatePaymentSummary(0.10);
        assertNull(result);
        LOGGER.info("✓ SUCCESS: PaymentService - Returned null payment summary for empty cart");
    }

    @Test
    @DisplayName("Should calculate payment summary with no discount")
    void testCalculatePaymentSummary_NoDiscount() {
        stockRepo.getCart().add(new Order(1, 1001, "Product A", 2, 50.0)); // 100.0
        // Subtotal: 100.0
        // Discount: 0.0
        // Tax (6%): 6.0
        // Total: 106.0

        PaymentResult result = paymentService.calculatePaymentSummary(0.0);

        assertNotNull(result);
        assertEquals(100.0, result.getSubtotal(), 0.01);
        assertEquals(0.0, result.getDiscount(), 0.01);
        assertEquals(6.0, result.getTax(), 0.01);
        assertEquals(106.0, result.getTotal(), 0.01);
        LOGGER.info("✓ SUCCESS: PaymentService - Calculated payment summary with no discount (Subtotal: RM100.00, Tax: RM6.00, Total: RM106.00)");
    }

    @Test
    @DisplayName("Should create transaction correctly")
    void testCreateTransaction() {
        Order item1 = new Order(1, 1001, "Product A", 2, 50.0);
        Order item2 = new Order(2, 1002, "Product B", 1, 100.0);
        stockRepo.getCart().add(item1);
        stockRepo.getCart().add(item2);
        // Subtotal: 200.0
        // Discount (10%): 20.0
        // Tax: 10.8
        // Total: 190.8

        Transaction transaction = paymentService.createTransaction(0.10);

        assertNotNull(transaction);
        assertEquals(200.0, transaction.getSubtotal(), 0.01);
        assertEquals(20.0, transaction.getDiscount(), 0.01);
        assertEquals(10.8, transaction.getTax(), 0.01);
        assertEquals(190.8, transaction.getTotal(), 0.01);
        assertEquals(2, transaction.getItems().size());
        LOGGER.info("✓ SUCCESS: PaymentService - Created transaction correctly (Subtotal: RM200.00, Discount: RM20.00, Tax: RM10.80, Total: RM190.80, Items: 2)");
    }

    @Test
    @DisplayName("Should return null transaction for empty cart")
    void testCreateTransaction_EmptyCart() {
        Transaction transaction = paymentService.createTransaction(0.10);
        assertNull(transaction);
        LOGGER.info("✓ SUCCESS: PaymentService - Returned null transaction for empty cart");
    }

    @Test
    @DisplayName("Should clear cart and orders")
    void testClearCart() {
        stockRepo.getCart().add(new Order(1, 1001, "Product A", 2, 50.0));
        assertFalse(stockRepo.getCart().isEmpty());

        paymentService.clearCart();

        assertTrue(stockRepo.getCart().isEmpty());
        LOGGER.info("✓ SUCCESS: PaymentService - Cleared cart and orders successfully");
    }

    @Test
    @DisplayName("Should handle complex payment calculation with multiple items and discount")
    void testComplexPaymentCalculation() {
        stockRepo.getCart().add(new Order(1, 1001, "Product A", 5, 20.0));  // 100.0
        stockRepo.getCart().add(new Order(2, 1002, "Product B", 3, 30.0));  // 90.0
        stockRepo.getCart().add(new Order(3, 1003, "Product C", 2, 15.0)); // 30.0
        // Subtotal: 220.0
        // Discount (15% premium): 33.0
        // Amount after discount: 187.0
        // Tax (6%): 11.22
        // Total: 198.22

        PaymentResult result = paymentService.calculatePaymentSummary(0.15);

        assertNotNull(result);
        assertEquals(220.0, result.getSubtotal(), 0.01);
        assertEquals(33.0, result.getDiscount(), 0.01);
        assertEquals(11.22, result.getTax(), 0.01);
        assertEquals(198.22, result.getTotal(), 0.01);
        LOGGER.info("✓ SUCCESS: PaymentService - Handled complex payment calculation (Subtotal: RM220.00, Discount: RM33.00, Tax: RM11.22, Total: RM198.22)");
    }


    @Test
    @DisplayName("Should create and save transaction in complete payment flow")
    void testCompletePaymentFlow() {
        // Add items to cart
        stockRepo.getCart().add(new Order(1, 1001, "Product A", 2, 50.0));
        stockRepo.getCart().add(new Order(2, 1002, "Product B", 1, 100.0));

        // Calculate payment summary
        PaymentResult summary = paymentService.calculatePaymentSummary(0.10);
        assertNotNull(summary);
        assertEquals(200.0, summary.getSubtotal(), 0.01);

        // Create transaction
        Transaction transaction = paymentService.createTransaction(0.10);
        assertNotNull(transaction);

        // Save transaction
        transactionService.saveTransaction(transaction);

        // Verify transaction was saved
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(1, transactions.size());
        assertEquals(190.8, transactions.get(0).getTotal(), 0.01);

        // Clear cart after payment
        paymentService.clearCart();
        assertTrue(stockRepo.getCart().isEmpty());
        LOGGER.info("✓ SUCCESS: PaymentService - Completed full payment flow (Summary calculated, transaction created and saved, cart cleared)");
    }

    @Test
    @DisplayName("Should handle multiple payment transactions")
    void testMultiplePaymentTransactions() {
        // First transaction
        stockRepo.getCart().add(new Order(1, 1001, "Product A", 2, 50.0));
        Transaction transaction1 = paymentService.createTransaction(0.10);
        transactionService.saveTransaction(transaction1);
        paymentService.clearCart();

        // Second transaction
        stockRepo.getCart().add(new Order(1, 1002, "Product B", 1, 100.0));
        Transaction transaction2 = paymentService.createTransaction(0.0);
        transactionService.saveTransaction(transaction2);
        paymentService.clearCart();

        // Verify both transactions saved
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(2, transactions.size());
        assertEquals(95.4, transactions.get(0).getTotal(), 0.01); // With discount
        assertEquals(106.0, transactions.get(1).getTotal(), 0.01); // Without discount
        LOGGER.info("✓ SUCCESS: PaymentService - Handled multiple payment transactions (Transaction 1: RM95.40 with discount, Transaction 2: RM106.00 without discount)");
    }
}

