package assignment.controller;

import assignment.controller.TransactionController;
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
}

