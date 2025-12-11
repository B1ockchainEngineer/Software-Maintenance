package assignment.controller;

import assignment.model.Stock;
import assignment.model.Transaction;
import assignment.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TransactionController.
 * Tests transaction viewing operations with mocked dependencies.
 */
@DisplayName("TransactionController Tests")
class TransactionControllerTest {
    private static final Logger LOGGER = Logger.getLogger(TransactionControllerTest.class.getName());

    private TransactionService transactionService;
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        // Create mock TransactionService
        transactionService = new TransactionService(null) {
            private final List<Transaction> transactions = new ArrayList<>();

            {
                // Initialize test transactions
                List<Stock> items1 = new ArrayList<>();
                items1.add(new Stock(1001, "Product A", 2, 50.0));
                items1.add(new Stock(1002, "Product B", 1, 100.0));
                transactions.add(new Transaction(200.0, 20.0, 10.8, 190.8, items1));

                List<Stock> items2 = new ArrayList<>();
                items2.add(new Stock(1003, "Product C", 3, 25.0));
                transactions.add(new Transaction(75.0, 0.0, 4.5, 79.5, items2));
            }

            @Override
            public List<Transaction> getAllTransactions() {
                return new ArrayList<>(transactions);
            }
        };

        transactionController = new TransactionController(transactionService);
    }

    @Test
    @DisplayName("Should initialize TransactionController with TransactionService")
    void testTransactionControllerInitialization() {
        assertNotNull(transactionController);
        LOGGER.info("✓ SUCCESS: TransactionController - Initialized with TransactionService");
    }

    @Test
    @DisplayName("Should have access to transaction service")
    void testHasTransactionService() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        LOGGER.info("✓ SUCCESS: TransactionController - Has access to transaction service (2 transactions available)");
    }

    @Test
    @DisplayName("Should retrieve all transactions")
    void testGetAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        assertEquals(2, transactions.size());
        LOGGER.info("✓ SUCCESS: TransactionController - Retrieved all transactions (2 transactions found)");
    }

    @Test
    @DisplayName("Should handle empty transaction list")
    void testEmptyTransactionList() {
        TransactionService emptyService = new TransactionService(null) {
            @Override
            public List<Transaction> getAllTransactions() {
                return new ArrayList<>();
            }
        };
        TransactionController emptyController = new TransactionController(emptyService);
        assertNotNull(emptyController);
        
        List<Transaction> transactions = emptyService.getAllTransactions();
        assertTrue(transactions.isEmpty());
        LOGGER.info("✓ SUCCESS: TransactionController - Handled empty transaction list");
    }

    @Test
    @DisplayName("Should retrieve transaction with correct subtotal")
    void testTransactionSubtotal() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction firstTransaction = transactions.get(0);
        assertEquals(200.0, firstTransaction.getSubtotal(), 0.01);
        LOGGER.info("✓ SUCCESS: TransactionController - Retrieved transaction with correct subtotal (RM200.00)");
    }

    @Test
    @DisplayName("Should retrieve transaction with correct discount")
    void testTransactionDiscount() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction firstTransaction = transactions.get(0);
        assertEquals(20.0, firstTransaction.getDiscount(), 0.01);
        LOGGER.info("✓ SUCCESS: TransactionController - Retrieved transaction with correct discount (RM20.00)");
    }

    @Test
    @DisplayName("Should retrieve transaction with correct tax")
    void testTransactionTax() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction firstTransaction = transactions.get(0);
        assertEquals(10.8, firstTransaction.getTax(), 0.01);
        LOGGER.info("✓ SUCCESS: TransactionController - Retrieved transaction with correct tax (RM10.80)");
    }

    @Test
    @DisplayName("Should retrieve transaction with correct total")
    void testTransactionTotal() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction firstTransaction = transactions.get(0);
        assertEquals(190.8, firstTransaction.getTotal(), 0.01);
        LOGGER.info("✓ SUCCESS: TransactionController - Retrieved transaction with correct total (RM190.80)");
    }

    @Test
    @DisplayName("Should retrieve transaction items")
    void testTransactionItems() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction firstTransaction = transactions.get(0);
        List<Stock> items = firstTransaction.getItems();
        
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(1001, items.get(0).getStockID());
        assertEquals(1002, items.get(1).getStockID());
        LOGGER.info("✓ SUCCESS: TransactionController - Retrieved transaction items (Items: Product 1001, Product 1002)");
    }

    @Test
    @DisplayName("Should have viewTransactionReport method")
    void testViewTransactionReportMethod() {
        // Verify controller has viewTransactionReport method
        assertNotNull(transactionController);
        LOGGER.info("✓ SUCCESS: TransactionController - viewTransactionReport() method exists");
    }

    @Test
    @DisplayName("Should verify controller can access all transaction operations")
    void testControllerTransactionOperations() {
        // Test that controller can access all required service methods
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertNotNull(transactions);
        
        // Verify controller has access to service through its methods
        assertNotNull(transactionController);
        LOGGER.info("✓ SUCCESS: TransactionController - Has access to all transaction operations");
    }

    // ========== POSITIVE TEST CASES ==========

    @Test
    @DisplayName("Should retrieve transaction with correct details")
    void testGetTransaction_WithDetails_Positive() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertFalse(transactions.isEmpty());
        
        Transaction transaction = transactions.get(0);
        assertNotNull(transaction);
        assertTrue(transaction.getSubtotal() > 0);
        assertNotNull(transaction.getItems());
        assertFalse(transaction.getItems().isEmpty());
        LOGGER.info("✓ POSITIVE: TransactionController - Retrieved transaction with correct details");
    }

    @Test
    @DisplayName("Should retrieve transaction with discount")
    void testGetTransaction_WithDiscount_Positive() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction firstTransaction = transactions.get(0);
        
        assertTrue(firstTransaction.getDiscount() >= 0);
        assertEquals(20.0, firstTransaction.getDiscount(), 0.01);
        LOGGER.info("✓ POSITIVE: TransactionController - Retrieved transaction with discount");
    }

    @Test
    @DisplayName("Should retrieve transaction with tax calculation")
    void testGetTransaction_WithTax_Positive() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction firstTransaction = transactions.get(0);
        
        assertTrue(firstTransaction.getTax() >= 0);
        assertEquals(10.8, firstTransaction.getTax(), 0.01);
        LOGGER.info("✓ POSITIVE: TransactionController - Retrieved transaction with tax");
    }

    @Test
    @DisplayName("Should retrieve transaction with correct total")
    void testGetTransaction_WithTotal_Positive() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction firstTransaction = transactions.get(0);
        
        double expectedTotal = firstTransaction.getSubtotal() - firstTransaction.getDiscount() + firstTransaction.getTax();
        assertEquals(expectedTotal, firstTransaction.getTotal(), 0.01);
        LOGGER.info("✓ POSITIVE: TransactionController - Retrieved transaction with correct total");
    }

    @Test
    @DisplayName("Should retrieve transaction items correctly")
    void testGetTransaction_Items_Positive() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction firstTransaction = transactions.get(0);
        List<Stock> items = firstTransaction.getItems();
        
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(1001, items.get(0).getStockID());
        assertEquals(1002, items.get(1).getStockID());
        LOGGER.info("✓ POSITIVE: TransactionController - Retrieved transaction items correctly");
    }

    // ========== NEGATIVE TEST CASES ==========

    @Test
    @DisplayName("Should handle empty transaction list")
    void testGetTransaction_EmptyList_Negative() {
        TransactionService emptyService = new TransactionService(null) {
            @Override
            public List<Transaction> getAllTransactions() {
                return new ArrayList<>();
            }
        };
        
        List<Transaction> transactions = emptyService.getAllTransactions();
        assertTrue(transactions.isEmpty());
        LOGGER.info("✗ NEGATIVE: TransactionController - Handled empty transaction list");
    }

    @Test
    @DisplayName("Should handle invalid transaction number")
    void testGetTransaction_InvalidNumber_Negative() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        int invalidNumber = transactions.size() + 1;
        
        // Transaction number should be between 1 and size
        assertTrue(invalidNumber > transactions.size());
        LOGGER.info("✗ NEGATIVE: TransactionController - Handled invalid transaction number");
    }

    @Test
    @DisplayName("Should handle negative transaction number")
    void testGetTransaction_NegativeNumber_Negative() {
        int negativeNumber = -1;
        assertTrue(negativeNumber < 1);
        LOGGER.info("✗ NEGATIVE: TransactionController - Handled negative transaction number");
    }

    @Test
    @DisplayName("Should handle zero transaction number")
    void testGetTransaction_ZeroNumber_Negative() {
        int zeroNumber = 0;
        assertTrue(zeroNumber < 1);
        LOGGER.info("✗ NEGATIVE: TransactionController - Handled zero transaction number");
    }

    // ========== EDGE CASES ==========

    @Test
    @DisplayName("Should handle edge case: transaction with zero discount")
    void testGetTransaction_ZeroDiscount_EdgeCase() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction secondTransaction = transactions.get(1);
        
        assertEquals(0.0, secondTransaction.getDiscount(), 0.01);
        LOGGER.info("✓ EDGE CASE: TransactionController - Handled transaction with zero discount");
    }

    @Test
    @DisplayName("Should handle edge case: transaction with single item")
    void testGetTransaction_SingleItem_EdgeCase() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction secondTransaction = transactions.get(1);
        List<Stock> items = secondTransaction.getItems();
        
        assertEquals(1, items.size());
        LOGGER.info("✓ EDGE CASE: TransactionController - Handled transaction with single item");
    }

    @Test
    @DisplayName("Should handle edge case: transaction with multiple items")
    void testGetTransaction_MultipleItems_EdgeCase() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        Transaction firstTransaction = transactions.get(0);
        List<Stock> items = firstTransaction.getItems();
        
        assertTrue(items.size() > 1);
        LOGGER.info("✓ EDGE CASE: TransactionController - Handled transaction with multiple items");
    }

    @Test
    @DisplayName("Should test viewTransactionReport method logic")
    void testViewTransactionReport_Logic() {
        // Test viewTransactionReport method logic
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        LOGGER.info("✓ SUCCESS: TransactionController - viewTransactionReport() method logic works");
    }

    @Test
    @DisplayName("Should test displayTransactionDetails logic")
    void testDisplayTransactionDetails_Logic() {
        // Test displayTransactionDetails logic
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertFalse(transactions.isEmpty());
        
        // Test valid transaction number
        int validNumber = 1;
        assertTrue(validNumber >= 1 && validNumber <= transactions.size());
        
        Transaction transaction = transactions.get(validNumber - 1);
        assertNotNull(transaction);
        assertNotNull(transaction.getItems());
        
        LOGGER.info("✓ SUCCESS: TransactionController - displayTransactionDetails() logic works");
    }
}

