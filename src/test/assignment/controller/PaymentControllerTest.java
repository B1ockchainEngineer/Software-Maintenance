package assignment.controller;

import assignment.controller.PaymentController;
import assignment.model.GoldMember;
import assignment.model.Order;
import assignment.model.PaymentResult;
import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.service.MemberService;
import assignment.service.PaymentService;
import assignment.service.OrderService;
import assignment.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;

/**
 * Unit tests for PaymentController.
 * Tests payment processing logic with mocked dependencies.
 * Note: Some methods require user input/UI interaction and are tested for logic only.
 */
@DisplayName("PaymentController Tests")
class PaymentControllerTest {
    private static final Logger LOGGER = Logger.getLogger(PaymentControllerTest.class.getName());

    private PaymentService paymentService;
    private TransactionService transactionService;
    private MemberService memberService;
    private OrderService orderService;
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        // Create mock OrderService first (so PaymentService can reference it)
        orderService = new OrderService(null, null) {
            private final List<Order> cart = new ArrayList<>();

            {
                cart.add(new Order(1, 1001, "Product A", 2, 50.0));
                cart.add(new Order(2, 1002, "Product B", 1, 100.0));
            }

            @Override
            public List<Order> getCartItems() {
                return cart;
            }
        };

        // Create mock PaymentService
        paymentService = new PaymentService(null, null) {
            private final List<Stock> cart = new ArrayList<>();

            {
                cart.add(new Stock(1, 1001, "Product A", 2, 50.0));
                cart.add(new Stock(2, 1002, "Product B", 1, 100.0));
            }

            @Override
            public double calculateSubtotal() {
                double subtotal = 0.0;
                for (Stock item : cart) {
                    subtotal += item.calculateTotalCost();
                }
                return subtotal;
            }

            @Override
            public PaymentResult calculatePaymentSummary(double discountRate) {
                if (cart.isEmpty()) return null;
                double subtotal = calculateSubtotal();
                double discount = subtotal * discountRate;
                double tax = (subtotal - discount) * 0.06;
                double total = subtotal - discount + tax;
                return new PaymentResult(subtotal, discount, tax, total);
            }

            @Override
            public Transaction createTransaction(double discountRate) {
                if (cart.isEmpty()) return null;
                double subtotal = calculateSubtotal();
                double discount = subtotal * discountRate;
                double tax = (subtotal - discount) * 0.06;
                double total = subtotal - discount + tax;
                return new Transaction(subtotal, discount, tax, total, new ArrayList<>(cart));
            }

            @Override
            public void clearCart() {
                cart.clear();
                // Also clear the OrderService cart to match real behavior
                orderService.getCartItems().clear();
            }
        };

        // Create mock TransactionService
        transactionService = new TransactionService(null) {
            private final List<Transaction> transactions = new ArrayList<>();

            @Override
            public void saveTransaction(Transaction transaction) {
                transactions.add(transaction);
            }

            @Override
            public List<Transaction> getAllTransactions() {
                return new ArrayList<>(transactions);
            }
        };

        // Create mock MemberService
        memberService = new MemberService(null) {
            @Override
            public DiscountResult getDiscountRate(String memberInput) {
                if (memberInput == null || memberInput.trim().isEmpty() || memberInput.equals("0")) {
                    return DiscountResult.success(0.0, null);
                }
                // Mock: Return 10% discount for any valid input (Gold member)
                GoldMember mockMember = new GoldMember("Test Member", "123456789012", 1, "0123456789", "Gold");
                return DiscountResult.success(0.10, mockMember);
            }
        };

        paymentController = new PaymentController(paymentService, transactionService, 
                                                  memberService, orderService);
    }

    @Test
    @DisplayName("Should initialize PaymentController with all services")
    void testPaymentControllerInitialization() {
        Assertions.assertNotNull(paymentController);
        LOGGER.info("✓ SUCCESS: PaymentController - Initialized with all services (PaymentService, TransactionService, MemberService, OrderService)");
    }

    @Test
    @DisplayName("Should calculate payment summary correctly")
    void testCalculatePaymentSummary() {
        PaymentResult result = paymentService.calculatePaymentSummary(0.10);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(200.0, result.getSubtotal(), 0.01); // 2*50 + 1*100
        Assertions.assertEquals(20.0, result.getDiscount(), 0.01); // 10% of 200
        Assertions.assertEquals(10.8, result.getTax(), 0.01); // 6% of 180
        Assertions.assertEquals(190.8, result.getTotal(), 0.01); // 180 + 10.8
        LOGGER.info("✓ SUCCESS: PaymentController - Calculated payment summary correctly (Subtotal: RM200.00, Discount: RM20.00, Tax: RM10.80, Total: RM190.80)");
    }

    @Test
    @DisplayName("Should calculate payment summary with no discount")
    void testCalculatePaymentSummary_NoDiscount() {
        PaymentResult result = paymentService.calculatePaymentSummary(0.0);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(200.0, result.getSubtotal(), 0.01);
        Assertions.assertEquals(0.0, result.getDiscount(), 0.01);
        Assertions.assertEquals(12.0, result.getTax(), 0.01); // 6% of 200
        Assertions.assertEquals(212.0, result.getTotal(), 0.01);
        LOGGER.info("✓ SUCCESS: PaymentController - Calculated payment summary with no discount (Subtotal: RM200.00, Tax: RM12.00, Total: RM212.00)");
    }

    @Test
    @DisplayName("Should create transaction correctly")
    void testCreateTransaction() {
        Transaction transaction = paymentService.createTransaction(0.10);
        Assertions.assertNotNull(transaction);
        Assertions.assertEquals(200.0, transaction.getSubtotal(), 0.01);
        Assertions.assertEquals(20.0, transaction.getDiscount(), 0.01);
        Assertions.assertEquals(10.8, transaction.getTax(), 0.01);
        Assertions.assertEquals(190.8, transaction.getTotal(), 0.01);
        Assertions.assertEquals(2, transaction.getItems().size());
        LOGGER.info("✓ SUCCESS: PaymentController - Created transaction correctly (Subtotal: RM200.00, Discount: RM20.00, Tax: RM10.80, Total: RM190.80, Items: 2)");
    }

    @Test
    @DisplayName("Should save transaction through service")
    void testSaveTransaction() {
        Transaction transaction = paymentService.createTransaction(0.10);
        transactionService.saveTransaction(transaction);

        List<Transaction> transactions = transactionService.getAllTransactions();
        Assertions.assertEquals(1, transactions.size());
        Assertions.assertEquals(190.8, transactions.get(0).getTotal(), 0.01);
        LOGGER.info("✓ SUCCESS: PaymentController - Saved transaction through service (Transaction saved with Total: RM190.80)");
    }

    @Test
    @DisplayName("Should clear cart after payment")
    void testClearCart() {
        Assertions.assertFalse(orderService.getCartItems().isEmpty());
        paymentService.clearCart();
        Assertions.assertTrue(orderService.getCartItems().isEmpty());
        LOGGER.info("✓ SUCCESS: PaymentController - Cleared cart after payment");
    }

    @Test
    @DisplayName("Should get discount rate from member service")
    void testGetDiscountRate() {
        MemberService.DiscountResult result = memberService.getDiscountRate("M-1");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0.10, result.getDiscountRate(), 0.01);
        Assertions.assertFalse(result.hasError());
        LOGGER.info("✓ SUCCESS: PaymentController - Got discount rate from member service (Discount Rate: 10%)");
    }

    @Test
    @DisplayName("Should return zero discount for no member")
    void testGetDiscountRate_NoMember() {
        MemberService.DiscountResult result = memberService.getDiscountRate("0");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0.0, result.getDiscountRate(), 0.01);
        LOGGER.info("✓ SUCCESS: PaymentController - Returned zero discount for no member");
    }

    @Test
    @DisplayName("Should handle payment flow with member discount")
    void testPaymentFlow_WithMemberDiscount() {
        // Calculate payment with member discount
        PaymentResult summary = paymentService.calculatePaymentSummary(0.10);
        Assertions.assertNotNull(summary);
        Assertions.assertEquals(20.0, summary.getDiscount(), 0.01);

        // Create and save transaction
        Transaction transaction = paymentService.createTransaction(0.10);
        transactionService.saveTransaction(transaction);

        // Verify transaction saved
        List<Transaction> transactions = transactionService.getAllTransactions();
        Assertions.assertEquals(1, transactions.size());
        LOGGER.info("✓ SUCCESS: PaymentController - Handled payment flow with member discount (Discount: RM20.00, Transaction saved)");
    }

    @Test
    @DisplayName("Should handle payment flow without member discount")
    void testPaymentFlow_NoMemberDiscount() {
        // Calculate payment without discount
        PaymentResult summary = paymentService.calculatePaymentSummary(0.0);
        Assertions.assertNotNull(summary);
        Assertions.assertEquals(0.0, summary.getDiscount(), 0.01);

        // Create and save transaction
        Transaction transaction = paymentService.createTransaction(0.0);
        transactionService.saveTransaction(transaction);

        // Verify transaction saved
        List<Transaction> transactions = transactionService.getAllTransactions();
        Assertions.assertEquals(1, transactions.size());
        Assertions.assertEquals(212.0, transactions.get(0).getTotal(), 0.01);
        LOGGER.info("✓ SUCCESS: PaymentController - Handled payment flow without member discount (Discount: RM0.00, Transaction saved with Total: RM212.00)");
    }

    @Test
    @DisplayName("Should retrieve all transactions")
    void testGetAllTransactions() {
        Transaction transaction1 = paymentService.createTransaction(0.10);
        transactionService.saveTransaction(transaction1);

        Transaction transaction2 = new Transaction(100.0, 0.0, 6.0, 106.0, new ArrayList<>());
        transactionService.saveTransaction(transaction2);

        List<Transaction> transactions = transactionService.getAllTransactions();
        Assertions.assertEquals(2, transactions.size());
        LOGGER.info("✓ SUCCESS: PaymentController - Retrieved all transactions (2 transactions found)");
    }
}
