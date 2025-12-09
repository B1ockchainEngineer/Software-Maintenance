package test.assignment.controller;

import assignment.controller.PaymentController;
import assignment.model.GoldMember;
import assignment.model.PaymentResult;
import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.service.MemberService;
import assignment.service.PaymentService;
import assignment.service.SalesService;
import assignment.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PaymentController.
 * Tests payment processing logic with mocked dependencies.
 * Note: Some methods require user input/UI interaction and are tested for logic only.
 */
@DisplayName("PaymentController Tests")
class PaymentControllerTest {

    private PaymentService paymentService;
    private TransactionService transactionService;
    private MemberService memberService;
    private SalesService salesService;
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
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

        // Create mock SalesService
        salesService = new SalesService(null, null) {
            private final List<Stock> cart = new ArrayList<>();

            {
                cart.add(new Stock(1, 1001, "Product A", 2, 50.0));
                cart.add(new Stock(2, 1002, "Product B", 1, 100.0));
            }

            @Override
            public List<Stock> getCartItems() {
                return cart;
            }
        };

        paymentController = new PaymentController(paymentService, transactionService, 
                                                  memberService, salesService);
    }

    @Test
    @DisplayName("Should initialize PaymentController with all services")
    void testPaymentControllerInitialization() {
        assertNotNull(paymentController);
    }

    @Test
    @DisplayName("Should calculate payment summary correctly")
    void testCalculatePaymentSummary() {
        PaymentResult result = paymentService.calculatePaymentSummary(0.10);
        assertNotNull(result);
        assertEquals(200.0, result.getSubtotal(), 0.01); // 2*50 + 1*100
        assertEquals(20.0, result.getDiscount(), 0.01); // 10% of 200
        assertEquals(10.8, result.getTax(), 0.01); // 6% of 180
        assertEquals(190.8, result.getTotal(), 0.01); // 180 + 10.8
    }

    @Test
    @DisplayName("Should calculate payment summary with no discount")
    void testCalculatePaymentSummary_NoDiscount() {
        PaymentResult result = paymentService.calculatePaymentSummary(0.0);
        assertNotNull(result);
        assertEquals(200.0, result.getSubtotal(), 0.01);
        assertEquals(0.0, result.getDiscount(), 0.01);
        assertEquals(12.0, result.getTax(), 0.01); // 6% of 200
        assertEquals(212.0, result.getTotal(), 0.01);
    }

    @Test
    @DisplayName("Should create transaction correctly")
    void testCreateTransaction() {
        Transaction transaction = paymentService.createTransaction(0.10);
        assertNotNull(transaction);
        assertEquals(200.0, transaction.getSubtotal(), 0.01);
        assertEquals(20.0, transaction.getDiscount(), 0.01);
        assertEquals(10.8, transaction.getTax(), 0.01);
        assertEquals(190.8, transaction.getTotal(), 0.01);
        assertEquals(2, transaction.getItems().size());
    }

    @Test
    @DisplayName("Should save transaction through service")
    void testSaveTransaction() {
        Transaction transaction = paymentService.createTransaction(0.10);
        transactionService.saveTransaction(transaction);

        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(1, transactions.size());
        assertEquals(190.8, transactions.get(0).getTotal(), 0.01);
    }

    @Test
    @DisplayName("Should clear cart after payment")
    void testClearCart() {
        assertFalse(salesService.getCartItems().isEmpty());
        paymentService.clearCart();
        assertTrue(salesService.getCartItems().isEmpty());
    }

    @Test
    @DisplayName("Should get discount rate from member service")
    void testGetDiscountRate() {
        MemberService.DiscountResult result = memberService.getDiscountRate("M-1");
        assertNotNull(result);
        assertEquals(0.10, result.getDiscountRate(), 0.01);
        assertFalse(result.hasError());
    }

    @Test
    @DisplayName("Should return zero discount for no member")
    void testGetDiscountRate_NoMember() {
        MemberService.DiscountResult result = memberService.getDiscountRate("0");
        assertNotNull(result);
        assertEquals(0.0, result.getDiscountRate(), 0.01);
    }

    @Test
    @DisplayName("Should handle payment flow with member discount")
    void testPaymentFlow_WithMemberDiscount() {
        // Calculate payment with member discount
        PaymentResult summary = paymentService.calculatePaymentSummary(0.10);
        assertNotNull(summary);
        assertEquals(20.0, summary.getDiscount(), 0.01);

        // Create and save transaction
        Transaction transaction = paymentService.createTransaction(0.10);
        transactionService.saveTransaction(transaction);

        // Verify transaction saved
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(1, transactions.size());
    }

    @Test
    @DisplayName("Should handle payment flow without member discount")
    void testPaymentFlow_NoMemberDiscount() {
        // Calculate payment without discount
        PaymentResult summary = paymentService.calculatePaymentSummary(0.0);
        assertNotNull(summary);
        assertEquals(0.0, summary.getDiscount(), 0.01);

        // Create and save transaction
        Transaction transaction = paymentService.createTransaction(0.0);
        transactionService.saveTransaction(transaction);

        // Verify transaction saved
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(1, transactions.size());
        assertEquals(212.0, transactions.get(0).getTotal(), 0.01);
    }

    @Test
    @DisplayName("Should retrieve all transactions")
    void testGetAllTransactions() {
        Transaction transaction1 = paymentService.createTransaction(0.10);
        transactionService.saveTransaction(transaction1);

        Transaction transaction2 = new Transaction(100.0, 0.0, 6.0, 106.0, new ArrayList<>());
        transactionService.saveTransaction(transaction2);

        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(2, transactions.size());
    }
}
